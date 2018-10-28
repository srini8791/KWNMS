/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.provision.service;

import org.opennms.core.tasks.BatchTask;
import org.opennms.core.tasks.RunInBatch;
import org.opennms.core.tasks.Task;
import org.opennms.core.tasks.TaskCoordinator;
import org.opennms.netmgt.config.api.SnmpAgentConfigFactory;
import org.opennms.netmgt.events.api.EventForwarder;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.snmp4j.Snmp4JValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.opennms.core.utils.InetAddressUtils.str;

/**
 * <p>NewSuspectScan class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class NodeApplyProfileScan implements Scan {
    private static final Logger LOG = LoggerFactory.getLogger(NodeApplyProfileScan.class);
    private String m_location;
    private Integer m_nodeId;
    private Integer m_profileId;
    private ProvisionService m_provisionService;
    private EventForwarder m_eventForwarder;
    private SnmpAgentConfigFactory m_agentConfigFactory;
    private TaskCoordinator m_taskCoordinator;
	private String m_foreignSource;
    private OnmsIpInterface iface;
    private int applyStatus = 0;

    /**
     * <p>Constructor for NewSuspectScan.</p>
     *
     * @param provisionService a {@link ProvisionService} object.
     * @param eventForwarder a {@link EventForwarder} object.
     * @param agentConfigFactory a {@link SnmpAgentConfigFactory} object.
     * @param taskCoordinator a {@link TaskCoordinator} object.
     */
    public NodeApplyProfileScan(final Integer nodeId, final Integer profileId, final ProvisionService provisionService, final EventForwarder eventForwarder, final SnmpAgentConfigFactory agentConfigFactory, final TaskCoordinator taskCoordinator) {
        m_provisionService = provisionService;
        m_eventForwarder = eventForwarder;
        m_agentConfigFactory = agentConfigFactory;
        m_taskCoordinator = taskCoordinator;
        m_nodeId = nodeId;
        m_profileId = profileId;
    }

    @Override
    public Task createTask() {
        return m_taskCoordinator.createBatch().add(this).get();
    }

    /** {@inheritDoc} */
    @Override
    public void run(final BatchTask phase) {
        applyProfileToNodeNode(phase);
    }

    protected void applyProfileToNodeNode(final BatchTask phase) {

        final OnmsNode node = m_provisionService.getNode(m_nodeId);
        iface = m_provisionService.getPrimaryInterfaceForNode(node);
        if (iface == null) { // NMS-6380, a discovered node added with wrong SNMP settings doesn't have a primary interface yet.
            iface = node.getIpInterfaces().isEmpty() ? null : node.getIpInterfaces().iterator().next();
        } else {
            LOG.info("The node with ID OP does not have a primary interface", m_nodeId);
        }
        final SnmpAgentConfig config = m_agentConfigFactory.getAgentConfig(iface.getIpAddress(),null);
        phase.getBuilder().addSequence(new RunInBatch() {
            @Override
            public void run(BatchTask batch) {
                applyProfile(node.getPrimaryIP(),config);
            }
        }, new RunInBatch() {
            @Override
            public void run(BatchTask batch) {
                if (applyStatus == 0) {
                    applyProfileStatus(config);
                }
            }
        }, new RunInBatch() {
            @Override
            public void run(BatchTask batch) {
                if (applyStatus == 0) {
                    deleteProfileDetailsAfterApplying();
                }
            }
        });
    }

    private void applyProfile(String ipAddress,SnmpAgentConfig config) {
        String tftpAddress = "";
        SnmpObjId oid = SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.6.0");
        SnmpObjId[] oids = {oid,SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.2.0"),SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.3.0"),SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.4.0")};
        Snmp4JValueFactory factory = new Snmp4JValueFactory();
        SnmpValue[] values = {
                factory.getOctetString(tftpAddress.getBytes()),
                factory.getOctetString(ipAddress.replaceAll(".","_").getBytes()),
                factory.getInt32(7),
                factory.getInt32(2)
        };

        CompletableFuture<List<SnmpValue>> future =  m_provisionService.getLocationAwareSnmpClient().set(config,Arrays.asList(oids),Arrays.asList(values)).execute();
        try {
            List<SnmpValue> resultValues = future.whenComplete((m,ex) -> {
                if(ex != null) {
                    applyStatus = 1;
                } else {
                    if(m == null || m.size() != oids.length) {
                        applyStatus = 1;
                    }
                }
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyProfileStatus(SnmpAgentConfig config) {
        CompletableFuture<SnmpValue> futureStatus = asynRerun(config);
        try {
            futureStatus.whenComplete((m,ex)-> {
                if(ex != null) {
                  applyStatus = 1;
                }
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private CompletableFuture<SnmpValue> asynRerun(final SnmpAgentConfig config) {
        SnmpObjId statusOid = SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.5.0");
        CompletableFuture<SnmpValue> future = m_provisionService.getLocationAwareSnmpClient().get(config,statusOid).execute();
        SnmpValue value = null;
        try {
            value = future.get();

        } catch (Exception e) {
            future.completeExceptionally(e);
            return future;
        }
        if (value.toLong() == 2) {
            return asynRerun(config);
        } else if(value.toLong() == 4) {
            future.completeExceptionally(new Exception("failed to download profile"));
            return future;
        } else {
            CompletableFuture<SnmpValue> response = new CompletableFuture<>();
            response.complete(value);
            return response;
        }
    }

    private void deleteProfileDetailsAfterApplying() {
        m_provisionService.deleteAfterProfileApply(m_provisionService.getNode(m_nodeId),m_provisionService.getProfile(m_profileId));
    }

    private ScanProgress createScanProgress() {
        return new ScanProgress() {
            private boolean m_aborted = false;
            @Override
            public void abort(final String message) {
                m_aborted = true;
                LOG.info(message);
            }

            @Override
            public boolean isAborted() {
                return m_aborted;
            }};
    }
}
