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

package org.opennms.netmgt.dao.api;

import org.opennms.netmgt.model.OnmsProfile;

/**
 * The Interface ProfileDao.
 */
public interface ProfileDao extends OnmsDao<OnmsProfile, Integer> {

    /**
     * Find Profile by id.
     *
     * @param profileId the profile id
     * @return the OpenNMS profile entity
     */
    public OnmsProfile findProfileById(Integer profileId);

    /**
     * Find profile by name.
     *
     * @param name the profile name
     * @return the OpenNMS profile entity
     */
    public OnmsProfile findProfileByName(String name);

    /**
     * Delete the record of profile and node identifiers
     * after applying the profile to the given node
     * @param profileId profile identifier
     * @param nodeId node identifier
     * @return
     */
    public int deleteProcessedProfileNode(Integer profileId, Integer nodeId);

}
