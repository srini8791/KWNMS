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

import org.opennms.netmgt.dao.api.RegionDao;
import org.opennms.netmgt.model.OnmsRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>RegionDaoHibernate class.</p>
 *
 * @author ndarbha
 */
public class RegionDaoHibernate extends AbstractDaoHibernate<OnmsRegion, Integer> implements RegionDao {
    private static final Logger LOG = LoggerFactory.getLogger(RegionDaoHibernate.class);

    /**
     * <p>Constructor for RegionDaoHibernate.</p>
     */
    public RegionDaoHibernate() {
        super(OnmsRegion.class);
    }

    /** {@inheritDoc} */
    @Override
    public OnmsRegion get(Integer regionId) {
        return findUnique("from OnmsRegion r where r.id  = ?", regionId);
    }


    /**
     * <p>findAll</p>
     *
     * @return a {@link List} of {@link OnmsRegion} objects.
     */
    @Override
    public List<OnmsRegion> findAll() {
        return find("from OnmsRegion order by name");
    }

}
