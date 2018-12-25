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

package org.opennms.netmgt.model;

import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "systemprops")
@XmlRootElement(name = "systemprop")
@XmlAccessorType(XmlAccessType.NONE)
public class OnmsSystemProp implements Serializable {

    private static final long serialVersionUID = 6706158883402462409L;

    @Id
    @Column(name="id")
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    @XmlAttribute(name = "id")
    private Integer m_id;

    @XmlAttribute(name = "key")
    @Column(name = "propkey", nullable = false, unique = true)
    private String m_key;

    @XmlAttribute(name = "value")
    @Column(name = "propvalue", nullable = false)
    private String m_value;

    /**
     * <p>getId</p>
     *
     * @return a {@link Integer} object.
     */
    public Integer getId() {
        return m_id;
    }

    /**
     * <p>setId</p>
     *
     * @param id a {@link Integer} object.
     */
    public void setId(Integer id) {
        m_id = id;
    }

    /**
     * <p>getKey</p>
     *
     * @return a {@link String} object.
     */
    public String getKey() {
        return m_key;
    }

    /**
     * <p>setKey</p>
     *
     * @param key a {@link String} object.
     */
    public void setKey(String key) {
        m_key = key;
    }

    /**
     * <p>getValue</p>
     *
     * @return a {@link String} object.
     */
    public String getValue() {
        return m_value;
    }

    /**
     * <p>setValue</p>
     *
     * @param value a {@link String} object.
     */
    public void setValue(String value) {
        m_value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        ToStringCreator creator = new ToStringCreator(this);
        creator.append("id", getId());
        creator.append("key", getKey());
        creator.append("value", getValue());
        return creator.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OnmsSystemProp) {
            OnmsSystemProp app = (OnmsSystemProp) obj;
            return getKey().equals(app.getKey());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getKey().hashCode();
    }


}
