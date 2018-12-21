/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2014 The OpenNMS Group, Inc.
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

import org.opennms.netmgt.dao.api.SystemPropDao;
import org.opennms.netmgt.model.OnmsSystemProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>SystemPropDaoHibernate class.</p>
 *
 * @author ndarbha
 */
public class SystemPropDaoHibernate extends AbstractDaoHibernate<OnmsSystemProp, Integer> implements SystemPropDao {
    private static final Logger LOG = LoggerFactory.getLogger(SystemPropDaoHibernate.class);

    /**
     * <p>Constructor for SystemPropDaoHibernate.</p>
     */
    public SystemPropDaoHibernate() {
        super(OnmsSystemProp.class);
    }

    /** {@inheritDoc} */
    @Override
    public OnmsSystemProp get(Integer propId) {
        return findUnique("from OnmsSystemProp r where r.id  = ?", propId);
    }

    /** {@inheritDoc} */
    @Override
    public OnmsSystemProp get(String propKey) {
        return findUnique("from OnmsSystemProp r where r.key  = ?", propKey);
    }

    /**
     * <p>findAll</p>
     *
     * @return a {@link List} of {@link OnmsSystemProp} objects.
     */
    @Override
    public List<OnmsSystemProp> findAll() {
        return find("from OnmsSystemProp order by key");
    }

}
