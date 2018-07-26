/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.model.topology.Topology;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="kdpElement")
public final class KdpElement implements Serializable, Topology {

    private static final long serialVersionUID = -291478133935237640L;

    private Integer m_id;
    private Date m_kdpNodeCreateTime = new Date();
    private Date m_kdpNodeLastPollTime;
    private OnmsNode m_node;

    public KdpElement() {
    }

    public KdpElement(OnmsNode node) {
        setNode(node);
    }

    /**
     * <p>getId</p>
     *
     * @return a {@link Integer} object.
     */
    @Id
    @Column(nullable = false)
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    public Integer getId() {
        return m_id;
    }

    /**
     * The node this asset information belongs to.
     *
     * @return a {@link OnmsNode} object.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeId")
    public OnmsNode getNode() {
        return m_node;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "kdpNodeCreateTime", nullable = false)
    public Date getKdpNodeCreateTime() {
        return m_kdpNodeCreateTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "kdpNodeLastPollTime", nullable = false)
    public Date getKdpNodeLastPollTime() {
        return m_kdpNodeLastPollTime;
    }

    /**
     * <p>setId</p>
     *
     * @param id a {@link Integer} object.
     */
    public void setId(final Integer id) {
        m_id = id;
    }

    /**
     * Set the node associated with the Lldp Element record
     *
     * @param node a {@link OnmsNode} object.
     */
    public void setNode(OnmsNode node) {
        m_node = node;
    }

    public void setKdpNodeCreateTime(Date kdpNodeCreateTime) {
        m_kdpNodeCreateTime = kdpNodeCreateTime;
    }

    public void setKdpNodeLastPollTime(Date kdpNodeLastPollTime) {
        m_kdpNodeLastPollTime = kdpNodeLastPollTime;
    }


    /**
     * <p>toString</p>
     *
     * @return a {@link String} object.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("Nodeid", m_node.getId())
                .toString();
    }

    @Transient
    public String printTopology() {
        StringBuffer strb = new StringBuffer();
        strb.append("kdpelement: nodeid:[");
        strb.append(getNode().getId());
        strb.append("]");
        return strb.toString();
    }

    public void merge(KdpElement element) {
        if (element == null)
            return;
        setKdpNodeLastPollTime(element.getKdpNodeCreateTime());
    }

}
