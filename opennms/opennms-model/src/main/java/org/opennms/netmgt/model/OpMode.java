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

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

/**
 * OpenNMS Operation Mode.
 */
@JsonDeserialize(using = OpModeDeserializer.class)
@JsonSerialize(using = OpModeSerializer.class)
public enum OpMode {

    MODE_UNKNOWN(0, "Unknown"),
    MODE_11G(1, "11g"),
    MODE_11NG(2, "11ng"),
    MODE_11A(3, "11a"),
    MODE_11NA(4, "11na"),
    MODE_11AC(5, "11ac");

    private static final Map<Integer, OpMode> m_idMap;

    private Integer m_id;

    private String m_label;

    static {
        m_idMap = new HashMap<>(values().length);
        for (final OpMode opMode : values()) {
            m_idMap.put(opMode.getValue(), opMode);
        }
    }

    OpMode(int id, String label) {
        m_id = id;
        m_label = label;
    }

    public Integer getId() {
        return m_id;
    }

    public String getLabel() {
        return m_label;
    }

    @JsonCreator
    public static OpMode get(final int id) {
        if (m_idMap.containsKey(id)) {
            return m_idMap.get(id);
        } else {
            return MODE_UNKNOWN;
        }
    }

    public Integer getValue() {
        return m_id;
    }

    @Override
    public String toString() {
        return m_label;
    }
}
