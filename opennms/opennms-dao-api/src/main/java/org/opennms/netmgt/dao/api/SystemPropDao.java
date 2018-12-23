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

package org.opennms.netmgt.dao.api;

import org.opennms.netmgt.model.OnmsSystemProp;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/**
 * <p>SystemPropDao interface.</p>
 */
public interface SystemPropDao extends OnmsDao<OnmsSystemProp, Integer> {

    /**
     * Get a system property based on it's ID
     *
     * @param propId property identifier
     * @return the SystemProp
     */
    @Override
    OnmsSystemProp get(Integer propId);

    /**
     * Returns a list of properties ordered by key.
     *
     * @return a {@link List} of {@link OnmsSystemProp} objects.
     */
    @Override
    List<OnmsSystemProp> findAll();

    /**
     * Get a system property based on it's key
     *
     * @param propKey property key
     * @return the SystemProp
     */
    OnmsSystemProp get(String propKey);

}
