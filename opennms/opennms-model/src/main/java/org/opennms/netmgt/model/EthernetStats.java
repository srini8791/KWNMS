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
 * Model class for Ethernet Statistics.
 */
@Entity
@Table(name="ethernetStats")
public class EthernetStats implements Serializable {

    private static final long serialVersionUID = 9222937204234277803L;

    private Integer m_id;
    private OnmsNode m_node;
    private BigInteger m_txPackets;
    private BigInteger m_rxPackets;
    private BigInteger m_txBytes;
    private BigInteger m_rxBytes;
    private Integer m_txErrors;
    private Integer m_rxErrors;

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

    public BigInteger getTxBytes() {
        return m_txBytes;
    }

    public void setTxBytes(BigInteger txBytes) {
        this.m_txBytes = txBytes;
    }

    public BigInteger getRxBytes() {
        return m_rxBytes;
    }

    public void setRxBytes(BigInteger rxBytes) {
        this.m_rxBytes = rxBytes;
    }

    public Integer getTxErrors() {
        return m_txErrors;
    }

    public void setTxErrors(Integer txErrors) {
        this.m_txErrors = txErrors;
    }

    public Integer getRxErrors() {
        return m_rxErrors;
    }

    public void setRxErrors(Integer rxErrors) {
        this.m_rxErrors = rxErrors;
    }

    @Override
    public String toString() {
        return "EthernetStats{" +
                "m_id=" + m_id +
                ", m_node=" + m_node +
                ", m_txPackets=" + m_txPackets +
                ", m_rxPackets=" + m_rxPackets +
                ", m_txBytes=" + m_txBytes +
                ", m_rxBytes=" + m_rxBytes +
                ", m_txErrors=" + m_txErrors +
                ", m_rxErrors=" + m_rxErrors +
                '}';
    }
}
