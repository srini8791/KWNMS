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

/**
 * 
 */
package org.opennms.netmgt.provision.service;

import org.opennms.core.tasks.BatchTask;
import org.opennms.core.tasks.RunInBatch;
import org.opennms.netmgt.config.api.SnmpAgentConfigFactory;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.monitoringLocations.OnmsMonitoringLocation;
import org.opennms.netmgt.provision.NodePolicy;
import org.opennms.netmgt.provision.service.snmp.KWPSystemGroup;
import org.opennms.netmgt.provision.service.snmp.KwpConfigurationGroup;
import org.opennms.netmgt.provision.service.snmp.KwpInventoryGroup;
import org.opennms.netmgt.provision.service.snmp.SystemGroup;
import org.opennms.netmgt.snmp.NamedSnmpVar;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

final class NodeKWPDiscoveryScan implements RunInBatch {
    private static final Logger LOG = LoggerFactory.getLogger(NodeKWPDiscoveryScan.class);

    private final SnmpAgentConfigFactory m_agentConfigFactory;
    private final InetAddress m_agentAddress;
    private final String m_foreignSource;
    private OnmsNode m_node;
    private Integer m_nodeId;
    private final OnmsMonitoringLocation m_location;
    private boolean restoreCategories = false;
    private final ProvisionService m_provisionService;
    private final ScanProgress m_scanProgress;
    private boolean isKwpDevice;

    NodeKWPDiscoveryScan(OnmsNode node, InetAddress agentAddress, String foreignSource, final OnmsMonitoringLocation location, ScanProgress scanProgress, SnmpAgentConfigFactory agentConfigFactory, ProvisionService provisionService, Integer nodeId){
        m_node = node;
        m_agentAddress = agentAddress;
        m_foreignSource = foreignSource;
        m_location = location;
        m_scanProgress = scanProgress;
        m_agentConfigFactory = agentConfigFactory;
        m_provisionService = provisionService;
        m_nodeId = nodeId;
        isKwpDevice = false;
    }

    /** {@inheritDoc} */
    @Override
    public void run(BatchTask phase) {
        
        phase.getBuilder().addSequence(
                new RunInBatch() {
                    @Override
                    public void run(BatchTask batch) {
                        collectObjectIdInfo();
                    }
                },
                new RunInBatch() {
                    @Override
                    public void run(BatchTask phase) {
                        doPersistNodeInfo();
                    }
                });
    }

    private InetAddress getAgentAddress() {
        return m_agentAddress;
    }

    private SnmpAgentConfig getAgentConfig(InetAddress primaryAddress) {
        return getAgentConfigFactory().getAgentConfig(primaryAddress,
                (m_location == null) ? null : m_location.getLocationName());
    }

    private SnmpAgentConfigFactory getAgentConfigFactory() {
        return m_agentConfigFactory;
    }

    private String getForeignSource() {
        return m_foreignSource;
    }

    public OnmsMonitoringLocation getLocation() {
        return m_location;
    }

    private String getLocationName() {
        return m_location == null ? null : m_location.getLocationName();
    }

    private ProvisionService getProvisionService() {
        return m_provisionService;
    }

    private void abort(String reason) {
        m_scanProgress.abort(reason);
    }

    private OnmsNode getNode() {
        return m_node;
    }
    
    private Integer getNodeId() {
        return m_nodeId;
    }

    private void setNode(OnmsNode node) {
        m_node = node;
    }


    private void collectObjectIdInfo() {
        InetAddress primaryAddress = getAgentAddress();
        SnmpAgentConfig agentConfig = getAgentConfig(primaryAddress);
        KwpInventoryGroup inventoryGroup = new KwpInventoryGroup(primaryAddress);
        List<SnmpObjId> idList = new ArrayList<SnmpObjId>();
        NamedSnmpVar var = new NamedSnmpVar(NamedSnmpVar.SNMPOBJECTID, "sysObjectID", ".1.3.6.1.2.1.1.2.0");
        idList.add(var.getSnmpObjId());
        try {

            final CompletableFuture<List<SnmpValue>> future = m_provisionService.getLocationAwareSnmpClient().get(agentConfig,idList)
                    .withDescription("inventoryGroup")
                    .withLocation(getLocationName())
                    .execute();
            //.get();
            List<SnmpValue> values = null;
            try {
                values = future.get();
            } catch(ExecutionException ex) {

            }

            if (values != null && values.size() > 0 && values.get(0) != null) {
                SnmpValue value = values.get(0);
                String str = value.toString();
                int subId = value.toSnmpObjId().getLastSubId();
                if (".1.3.6.1.4.1.52619".equalsIgnoreCase(str)) {
                    isKwpDevice = true;

                } else {
                    m_scanProgress.abort("Ignoring non keywest device " + primaryAddress);
                }
            } else {
                m_scanProgress.abort("Ignoring non keywest device " + primaryAddress);
            }
            //configGroup.updateSnmpDataForNode(getNode());,
        } catch (final InterruptedException e) {
            abort("Aborting node scan : Scan thread interrupted!");
            Thread.currentThread().interrupt();
        }
    }

   /* private void collectNodeConfigInfo() {
        InetAddress primaryAddress = getAgentAddress();
        SnmpAgentConfig agentConfig = getAgentConfig(primaryAddress);
        KwpConfigurationGroup configGroup = new KwpConfigurationGroup(primaryAddress);
        try {
            KwpConfigurationGroup configGroup1 = new KwpConfigurationGroup(primaryAddress);
            final CompletableFuture<List<SnmpValue>> future = m_provisionService.getLocationAwareSnmpClient().get(agentConfig,configGroup1.getGetList())
                    .withDescription("configGroup")
                    .withLocation(getLocationName())
                    .execute();
            //.get();
            List<SnmpValue> values = null;
            try {
                values = future.get();
            } catch(ExecutionException ex) {

            }

            if (values != null && configGroup1.getGetList().size() == values.size()) {
                configGroup1.updateSnmpDataForNode(getNode(),values);
            }
            //configGroup.updateSnmpDataForNode(getNode());,
        } catch (final InterruptedException e) {
            abort("Aborting node scan : Scan thread interrupted!");
            Thread.currentThread().interrupt();
        }
    }*/

  /*  private void collectNodeInfo() {
        Assert.notNull(getAgentConfigFactory(), "agentConfigFactory was not injected");
        InetAddress primaryAddress = getAgentAddress();
        SnmpAgentConfig agentConfig = getAgentConfig(primaryAddress);
        
        SystemGroup systemGroup = new SystemGroup(primaryAddress);

        try {
            try {
                m_provisionService.getLocationAwareSnmpClient().walk(agentConfig, systemGroup)
                    .withDescription("systemGroup")
                    .withLocation(getLocationName())
                    .execute()
                    .get();
                systemGroup.updateSnmpDataForNode(getNode());
            } catch (ExecutionException e) {
                abort("Aborting node scan : Agent failed while scanning the system table: " + e.getMessage());
            }

            KWPSystemGroup kwpSystemGroup = new KWPSystemGroup(primaryAddress);
                try {
                    m_provisionService.getLocationAwareSnmpClient().walk(agentConfig, kwpSystemGroup)
                            .withDescription("systemGroup")
                            .withLocation(getLocationName())
                            .execute()
                            .get();
                    kwpSystemGroup.updateSnmpDataForNode(getNode());
                } catch (ExecutionException e) {
                    abort("Aborting node scan : Agent failed while scanning the system table: " + e.getMessage());
                }

            List<NodePolicy> nodePolicies = getProvisionService().getNodePoliciesForForeignSource(getEffectiveForeignSource());

            OnmsNode node = null;
            if (isAborted()) {
                if (getNodeId() != null && nodePolicies.size() > 0) {
                    restoreCategories = true;
                    node = m_provisionService.getDbNodeInitCat(getNodeId());
                    LOG.debug("collectNodeInfo: checking {} node policies for restoration of categories", nodePolicies.size());
                }
            } else {
                node = getNode();
            }

        
            if (node == null) {
                restoreCategories = false;
                if (!isAborted()) {
                    String reason = "Aborted scan of node due to configured policy";
                    abort(reason);
                }
            } else {
                setNode(node);
            }
        
        } catch (final InterruptedException e) {
            abort("Aborting node scan : Scan thread interrupted!");
            Thread.currentThread().interrupt();
        }
    }*/

    private String getEffectiveForeignSource() {
        return getForeignSource()  == null ? "default" : getForeignSource();
    }

    private void doPersistNodeInfo() {
        /*if (restoreCategories) {
            LOG.debug("doPersistNodeInfo: Restoring {} categories to DB", getNode().getCategories().size());
        }*/
        if (!isAborted() && isKwpDevice) {
            getProvisionService().insertNode(m_node);
        }
    }

    private boolean isAborted() {
        return m_scanProgress.isAborted();
    }
}
