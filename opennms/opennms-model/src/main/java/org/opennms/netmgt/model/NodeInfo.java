/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Model class for Ethernet Statistics.
 */
@Entity
@Table(name="nodeInfo")
public class NodeInfo implements Serializable {

    private static final long serialVersionUID = -1742862200316871105L;

    private Integer m_id;
    private OnmsNode m_node;
    private String m_serialNumber;
    private String m_macAddress;
    private String m_model;
    private String m_firmware;
    private String m_ipAddress;
    private String m_systemName;
    private Integer m_frequencyChannel;
    private Integer m_bandwidth;
    private String m_ethernetSpeed;
    private String m_ioBandwidthLimit;
    private Integer m_modulation;
    private Integer m_opMode;
    private Boolean m_provisioned;
    private Boolean m_active;

    @Id
    @Column(nullable = false)
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    public Integer getId() {
        return m_id;
    }

    public void setId(Integer id) {
        m_id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeId")
    public OnmsNode getNode() {
        return m_node;
    }

    public void setNode(OnmsNode node) {
        m_node = node;
    }

    public String getSerialNumber() {
        return m_serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.m_serialNumber = serialNumber;
    }

    public String getMacAddress() {
        return m_macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.m_macAddress = macAddress;
    }

    public String getModel() {
        return m_model;
    }

    public void setModel(String model) {
        this.m_model = model;
    }

    public String getFirmware() {
        return m_firmware;
    }

    public void setFirmware(String firmware) {
        this.m_firmware = firmware;
    }

    public String getIpAddress() {
        return m_ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.m_ipAddress = ipAddress;
    }

    public String getSystemName() {
        return m_systemName;
    }

    public void setSystemName(String systemName) {
        this.m_systemName = systemName;
    }

    public Integer getFrequencyChannel() {
        return m_frequencyChannel;
    }

    public void setFrequencyChannel(Integer frequencyChannel) {
        this.m_frequencyChannel = frequencyChannel;
    }

    public Integer getBandwidth() {
        return m_bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.m_bandwidth = bandwidth;
    }

    public String getEthernetSpeed() {
        return m_ethernetSpeed;
    }

    public void setEthernetSpeed(String ethernetSpeed) {
        this.m_ethernetSpeed = ethernetSpeed;
    }

    public String getIoBandwidthLimit() {
        return m_ioBandwidthLimit;
    }

    public void setIoBandwidthLimit(String ioBandwidthLimit) {
        this.m_ioBandwidthLimit = ioBandwidthLimit;
    }

    public Integer getModulation() {
        return m_modulation;
    }

    public void setModulation(Integer modulation) {
        this.m_modulation = modulation;
    }

    public Integer getOpMode() {
        return m_opMode;
    }

    public void setOpMode(Integer opMode) {
        this.m_opMode = opMode;
    }

    public Boolean getProvisioned() {
        return m_provisioned;
    }

    public void setProvisioned(Boolean provisioned) {
        this.m_provisioned = provisioned;
    }

    public Boolean getActive() {
        return m_active;
    }

    public void setActive(Boolean active) {
        this.m_active = active;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "m_id=" + m_id +
                ", m_node=" + m_node +
                ", m_serialNumber='" + m_serialNumber + '\'' +
                ", m_macAddress='" + m_macAddress + '\'' +
                ", m_model='" + m_model + '\'' +
                ", m_firmware='" + m_firmware + '\'' +
                ", m_ipAddress='" + m_ipAddress + '\'' +
                ", m_systemName='" + m_systemName + '\'' +
                ", m_frequencyChannel=" + m_frequencyChannel +
                ", m_bandwidth=" + m_bandwidth +
                ", m_ethernetSpeed='" + m_ethernetSpeed + '\'' +
                ", m_ioBandwidthLimit='" + m_ioBandwidthLimit + '\'' +
                ", m_modulation=" + m_modulation +
                ", m_opMode=" + m_opMode +
                ", m_provisioned=" + m_provisioned +
                ", m_active=" + m_active +
                '}';
    }
}
