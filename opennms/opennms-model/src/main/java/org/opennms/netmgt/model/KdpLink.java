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
import org.hibernate.annotations.Type;
import org.opennms.netmgt.model.topology.Topology;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "kdpLink")
public class KdpLink implements Serializable, Topology {

    private static final long serialVersionUID = -8202964898258673730L;

    private Integer m_id;
    private OnmsNode m_node;
    private String m_kdpInterfaceName;

    private Date m_kdpLinkCreateTime = new Date();
    private Date m_kdpLinkLastPollTime;
    private String m_kdpLinkMacAddress;
    private String m_kdpLinkRemoteAddress;

    public KdpLink() {
    }

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

    @Column(name = "kdpInterfaceName", length = 96, nullable = true)
    public String getKdpInterfaceName() {
        return m_kdpInterfaceName;
    }

    public void setKdpInterfaceName(String kdpInterfaceName) {
        m_kdpInterfaceName = kdpInterfaceName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "kdpLinkCreateTime", nullable = false)
    public Date getKdpLinkCreateTime() {
        return m_kdpLinkCreateTime;
    }

    public void setKdpLinkCreateTime(Date kdpLinkCreateTime) {
        m_kdpLinkCreateTime = kdpLinkCreateTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "kdpLinkLastPollTime", nullable = false)
    public Date getKdpLinkLastPollTime() {
        return m_kdpLinkLastPollTime;
    }

    public void setKdpLinkLastPollTime(Date kdpLinkLastPollTime) {
        m_kdpLinkLastPollTime = kdpLinkLastPollTime;
    }

    @Column(name = "kdpLinkMacAddress", length = 20, nullable = true)
    public String getKdpLinkMacAddress() {
        return m_kdpLinkMacAddress;
    }

    public void setKdpLinkMacAddress(String kdpLinkMacAddress) {
        m_kdpLinkMacAddress = kdpLinkMacAddress;
    }

    @Column(name = "kdpLinkRemoteAddress", length = 20, nullable = true)
    public String getKdpLinkRemoteAddress() {
        return m_kdpLinkRemoteAddress;
    }

    public void setKdpLinkRemoteAddress(String kdpLinkRemoteAddress) {
        m_kdpLinkRemoteAddress = kdpLinkRemoteAddress;
    }

    public void merge(KdpLink link) {
        if (link == null) return;
        setKdpLinkLastPollTime(link.getKdpLinkCreateTime());
        setKdpLinkMacAddress(link.getKdpLinkMacAddress());
        setKdpLinkRemoteAddress(link.getKdpLinkRemoteAddress());
    }

    /**
     * <p>toString</p>
     *
     * @return a {@link String} object.
     */
    public String toString() {
        Integer nodeid = null;
        if (m_node != null)
            nodeid = m_node.getId();
        return new ToStringBuilder(this)
                .append("NodeId", nodeid)
                .append("kdpInterfaceName", m_kdpInterfaceName)
                .append("kdpLinkMacAddress", m_kdpLinkMacAddress)
                .append("kdpLinkRemoteAddress", m_kdpLinkRemoteAddress)
                .append("createTime", m_kdpLinkCreateTime)
                .append("lastPollTime", m_kdpLinkLastPollTime)
                .toString();
    }

    @Transient
    public String printTopology() {
        StringBuffer strb = new StringBuffer();
        strb.append("kdplink: nodeid:[");
        strb.append(getNode().getId());
        strb.append("], interfacename:[");
        strb.append(getKdpInterfaceName());
        strb.append("], macaddress:[");
        strb.append(getKdpLinkMacAddress());
        strb.append("], remoteaddress:[");
        strb.append(getKdpLinkRemoteAddress());
        strb.append("]");

        return strb.toString();
    }


}
