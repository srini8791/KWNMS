/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2014 The OpenNMS Group, Inc.
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
import org.opennms.netmgt.kwp.KwpPacketHeader;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.monitoringLocations.OnmsMonitoringLocation;
import org.opennms.netmgt.provision.service.kwp.KwpConfiguration;
import org.opennms.netmgt.provision.service.kwp.KwpInventory;
import org.opennms.netmgt.provision.service.kwp.KwpSysInfo;

import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

public class NodeKwpInfoScan implements RunInBatch {

    private OnmsNode m_node;
    private InetAddress m_agentAddress;
    private String m_foreignSource;
    private final OnmsMonitoringLocation m_location;
    private ScanProgress m_scanProgress;
    private ProvisionService m_provisionService;
    private Integer m_nodeId;


    NodeKwpInfoScan(OnmsNode node, InetAddress agentAddress, String foreignSource, final OnmsMonitoringLocation location, ScanProgress scanProgress,  ProvisionService provisionService, Integer nodeId){
        m_node = node;
        m_agentAddress = agentAddress;
        m_foreignSource = foreignSource;
        m_location = location;
        m_scanProgress = scanProgress;
        //m_agentConfigFactory = agentConfigFactory;
        m_provisionService = provisionService;
        m_nodeId = nodeId;
    }

    public OnmsNode getNode() {
        return m_node;
    }

    @Override
    public void run(BatchTask phase) {

        if (!m_scanProgress.isAborted()) {
            phase.getBuilder().addSequence(new RunInBatch() {
                @Override
                public void run(BatchTask batch) {
                    discoveryKwpService();
                }
            }, new RunInBatch() {
                @Override
                public void run(BatchTask batch) {
                    retreiveKwpDeviceSysInfo();
                }
            }, new RunInBatch() {
                @Override
                public void run(BatchTask batch) {
                    retreiveKwpDeviceInventory();
                }
            }, new RunInBatch() {
                @Override
                public void run(BatchTask batch) {
                    doPersistNodeInfo();
                }
            });
        }
    }

    private void discoveryKwpService() {
        KwpConfiguration configuration = new KwpConfiguration();
        try {
            m_provisionService.getLocationAwareKwpClient().get(m_agentAddress.getHostAddress(),
                    new KwpPacketHeader((byte)1,(byte)1,(byte)1,(byte)1),configuration)
                    .buildRequest()
                    .execute().get();
            configuration.updateKwpDataforNode(getNode());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void retreiveKwpDeviceSysInfo() {

        try {
            KwpSysInfo sysInfo = new KwpSysInfo();
            m_provisionService.getLocationAwareKwpClient().get(m_agentAddress.getHostAddress(),
                    new KwpPacketHeader((byte)1,(byte)1,(byte)1,(byte)2),sysInfo)
                    .buildRequest()
                    .execute().get();
            sysInfo.updateKwpDataforNode(getNode());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void retreiveKwpDeviceInventory() {
        //m_provisionService.getLocationAwareSnmpClient().

        try {
            KwpInventory inventory = new KwpInventory();
            m_provisionService.getLocationAwareKwpClient().get(m_agentAddress.getHostAddress(),
                    new KwpPacketHeader((byte)1,(byte)1,(byte)1,(byte)3),inventory)
                    .buildRequest()
                    .execute().get();
            inventory.updateKwpDataforNode(getNode());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public ProvisionService getProvisionService() {
        return m_provisionService;
    }

    private void doPersistNodeInfo() {
        getProvisionService().updateNodeAttributes(getNode());
    }
}
