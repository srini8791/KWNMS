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

package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.api.ProfileDao;
import org.opennms.netmgt.model.OnmsProfile;

/**
 * The Class ProfileDaoHibernate.
 */
public class ProfileDaoHibernate extends AbstractDaoHibernate<OnmsProfile, Integer> implements ProfileDao {

    /**
     * The Constructor.
     */
    public ProfileDaoHibernate() {
        super(OnmsProfile.class);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.api.ProfileDao#findProfileById(java.lang.Integer)
     */
    @Override
    public OnmsProfile findProfileById(Integer profileId) {
        return (OnmsProfile) findUnique("from OnmsProfile e where e.id = ?", profileId);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.api.ProfileDao#findProfileByName(java.lang.String)
     */
    @Override
    public OnmsProfile findProfileByName(String name) {
        return (OnmsProfile) findUnique("from OnmsProfile e where e.name = ?", name);
    }

}
