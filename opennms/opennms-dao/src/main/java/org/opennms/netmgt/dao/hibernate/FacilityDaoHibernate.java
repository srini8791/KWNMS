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

import org.opennms.netmgt.dao.api.FacilityDao;
import org.opennms.netmgt.dao.api.FacilityDao;
import org.opennms.netmgt.model.OnmsFacility;
import org.opennms.netmgt.model.OnmsFacility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>FacilityDaoHibernate class.</p>
 *
 * @author ndarbha
 */
public class FacilityDaoHibernate extends AbstractDaoHibernate<OnmsFacility, Integer> implements FacilityDao {
    private static final Logger LOG = LoggerFactory.getLogger(FacilityDaoHibernate.class);

    /**
     * <p>Constructor for FacilityDaoHibernate.</p>
     */
    public FacilityDaoHibernate() {
        super(OnmsFacility.class);
    }

    /** {@inheritDoc} */
    @Override
    public OnmsFacility get(Integer facilityId) {
        return findUnique("from OnmsFacility f where f.id  = ?", facilityId);
    }

    @Override
    public OnmsFacility findFacilityByName(String name) {
        return findUnique("from OnmsFacility f where f.name = ?", name);
    }


    /**
     * <p>findAll</p>
     *
     * @return a {@link List} of {@link OnmsFacility} objects.
     */
    @Override
    public List<OnmsFacility> findAll() {
        return find("from OnmsFacility order by name");
    }

}
