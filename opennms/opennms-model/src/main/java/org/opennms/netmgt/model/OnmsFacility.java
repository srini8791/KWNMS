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

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.opennms.netmgt.model.monitoringLocations.OnmsMonitoringLocation;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "facilities")
@XmlRootElement(name = "facility")
@XmlAccessorType(XmlAccessType.NONE)
public class OnmsFacility implements Serializable {

    private static final long serialVersionUID = -4522570092361215659L;

    @Id
    @Column(name="id")
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    @XmlAttribute(name = "id")
    private Integer m_id;

    @XmlAttribute(name = "name")
    @Column(name = "name", nullable = false, unique = true)
    private String m_name;

    @XmlAttribute(name = "latitude")
    @Column(name = "latitude")
    private Float m_latitude;

    @XmlAttribute(name = "longitude")
    @Column(name = "longitude")
    private Float m_longitude;

    @XmlElement(name = "region")
    @ManyToOne
    @JoinColumn(name = "region")
    @JsonBackReference
    private OnmsRegion region;

    public OnmsFacility() {}

    public OnmsFacility(Integer id, String name) {
        this.m_id = id;
        this.m_name = name;
    }

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
     * <p>getName</p>
     *
     * @return a {@link String} object.
     */
    public String getName() {
        return m_name;
    }

    /**
     * <p>setName</p>
     *
     * @param name a {@link String} object.
     */
    public void setName(String name) {
        m_name = name;
    }


    /**
     * <p>getLatitude</p>
     *
     * @return a {@link Float} object.
     */
    public Float getLatitude() {
        return m_latitude;
    }

    /**
     * <p>setLatitude</p>
     *
     * @param latitude a {@link Float} object.
     */
    public void setLatitude(Float latitude) {
        this.m_latitude = latitude;
    }

    /**
     * <p>getLongitude</p>
     *
     * @return a {@link Float} object.
     */
    public Float getLongitude() {
        return m_longitude;
    }

    /**
     * <p>setLongitude</p>
     *
     * @param longitude a {@link Float} object.
     */
    public void setLongitude(Float longitude) {
        this.m_longitude = longitude;
    }

    /**
     * <p>getRegion</p>
     *
     * @return a {@link OnmsRegion} object.
     * @since 1.8.1
     */
    public OnmsRegion getRegion() {
        return region;
    }

    /**
     * <p>setRegion</p>
     *
     * @param region a {@link OnmsRegion} object.
     * @since 1.8.1
     */
    public void setRegion(OnmsRegion region) {
        region = region;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        ToStringCreator creator = new ToStringCreator(this);
        creator.append("id", getId());
        creator.append("name", getName());
        return creator.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OnmsFacility) {
            OnmsFacility app = (OnmsFacility) obj;
            return getName().equals(app.getName());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }


}
