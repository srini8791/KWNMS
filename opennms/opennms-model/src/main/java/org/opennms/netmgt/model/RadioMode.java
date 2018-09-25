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

import java.util.HashMap;
import java.util.Map;

/**
 * OpenNMS Radio Mode.
 */
public enum RadioMode {

    OUTDOOR_BASE(1, "Outdoor Base"),
    OUTDOOR_SUBSCRIBER(2, "Outdoor Subscriber"),
    ACCESS_POINT(3, "Access Point"),
    CLIENT(4, "Client");

    private static final Map<Integer, RadioMode> m_idMap;

    private int m_id;
    private String m_label;

    static {
        m_idMap = new HashMap<>(values().length);
        for (final RadioMode radioMode : values()) {
            m_idMap.put(radioMode.getId(), radioMode);
        }
    }

    RadioMode(int id, String label) {
        m_id = id;
        m_label = label;
    }

    public int getId() {
        return m_id;
    }

    public String getLabel() {
        return m_label;
    }

    public static RadioMode get(final int id) {
        if (m_idMap.containsKey(id)) {
            return m_idMap.get(id);
        } else {
            throw new IllegalArgumentException("Cannot create RadioMode from unknown ID " + id);
        }
    }

    public static RadioMode get(final String label) {
        for (final Integer key : m_idMap.keySet()) {
            if (m_idMap.get(key).getLabel().equalsIgnoreCase(label)) {
                return m_idMap.get(key);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return m_label;
    }
}
