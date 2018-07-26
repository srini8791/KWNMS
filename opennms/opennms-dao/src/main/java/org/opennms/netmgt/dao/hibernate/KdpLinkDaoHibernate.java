/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.api.KdpLinkDao;
import org.opennms.netmgt.model.KdpLink;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * <p>KdpLinkDaoHibernate class.</p>
 *
 * @author antonio
 */
public class KdpLinkDaoHibernate extends AbstractDaoHibernate<KdpLink, Integer> implements KdpLinkDao {

    /**
     * <p>Constructor for IpInterfaceDaoHibernate.</p>
     */
    public KdpLinkDaoHibernate() {
        super(KdpLink.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KdpLink get(OnmsNode node) {
        Assert.notNull(node, "node cannot be null");
        return findUnique("from KdpLink as KdpLink where kdpLink.node = ?", node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KdpLink get(Integer nodeId) {
        Assert.notNull(nodeId, "nodeId cannot be null");
        return findUnique("from KdpLink as kdpLink where kdpLink.node.id = ?", nodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KdpLink> findByNodeId(Integer nodeId) {
        Assert.notNull(nodeId, "nodeId cannot be null");
        return find("from KdpLink kdpLink where kdpLink.node.id = ?", nodeId);
    }

    @Override
    public void deleteByNodeIdOlderThen(Integer nodeId, Date now) {
        getHibernateTemplate().bulkUpdate("delete from KdpLink kdpLink where kdpLink.node.id = ? and kdpLink.kdpLinkLastPollTime < ?",
                new Object[]{nodeId, now});
    }

    @Override
    public void deleteByNodeId(Integer nodeId) {
        getHibernateTemplate().bulkUpdate("delete from KdpLink kdpLink where kdpLink.node.id = ? ",
                new Object[]{nodeId});
    }

}
