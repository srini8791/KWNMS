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
import java.math.BigInteger;

/**
 * Model class for Wireless Statistics.
 */
@Entity
@Table(name="wirelessStats")
public class WirelessStats implements Serializable {

    private static final long serialVersionUID = -8097595404928147800L;

    private Integer m_id;
    private OnmsNode m_node;
    private BigInteger m_txPackets;
    private BigInteger m_rxPackets;
    private BigInteger m_txMgmtPackets;
    private BigInteger m_rxMgmtPackets;
    private Integer m_mpduErrors;
    private Integer m_physicalErrors;

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

    public BigInteger getTxPackets() {
        return m_txPackets;
    }

    public void setTxPackets(BigInteger txPackets) {
        this.m_txPackets = txPackets;
    }

    public BigInteger getRxPackets() {
        return m_rxPackets;
    }

    public void setRxPackets(BigInteger rxPackets) {
        this.m_rxPackets = rxPackets;
    }

    public BigInteger getTxMgmtPackets() {
        return m_txMgmtPackets;
    }

    public void setTxMgmtPackets(BigInteger txMgmtPackets) {
        this.m_txMgmtPackets = txMgmtPackets;
    }

    public BigInteger getRxMgmtPackets() {
        return m_rxMgmtPackets;
    }

    public void setRxMgmtPackets(BigInteger rxMgmtPackets) {
        this.m_rxMgmtPackets = rxMgmtPackets;
    }

    public Integer getMpduErrors() {
        return m_mpduErrors;
    }

    public void setMpduErrors(Integer mpduErrors) {
        this.m_mpduErrors = mpduErrors;
    }

    public Integer getPhysicalErrors() {
        return m_physicalErrors;
    }

    public void setPhysicalErrors(Integer physicalErrors) {
        this.m_physicalErrors = physicalErrors;
    }

    @Override
    public String toString() {
        return "WirelessStats{" +
                "m_id=" + m_id +
                ", m_node=" + m_node +
                ", m_txPackets=" + m_txPackets +
                ", m_rxPackets=" + m_rxPackets +
                ", m_txMgmtPackets=" + m_txMgmtPackets +
                ", m_rxMgmtPackets=" + m_rxMgmtPackets +
                ", m_mpduErrors=" + m_mpduErrors +
                ", m_physicalErrors=" + m_physicalErrors +
                '}';
    }
}
