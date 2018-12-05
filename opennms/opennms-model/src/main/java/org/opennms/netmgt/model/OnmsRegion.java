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
@Table(name = "regions")
@XmlRootElement(name = "region")
@XmlAccessorType(XmlAccessType.NONE)
public class OnmsRegion implements Serializable {

    private static final long serialVersionUID = -8906476327527789591L;

    @Id
    @Column(name="id")
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    @XmlAttribute(name = "id")
    private Integer m_id;

    @XmlAttribute(name = "name")
    @Column(name = "name", nullable = false, unique = true)
    private String m_name;

/* Uncomment when mapped to monitoringLocations via minions
    @XmlIDREF
    @XmlElementWrapper(name = "monitoringLocations")
    @XmlElement(name = "monitoringLocation")
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<OnmsMonitoringLocation> m_monitoringLocations = new LinkedHashSet<>();
*/

    @XmlIDREF
    @XmlElementWrapper(name = "facilities")
    @XmlElement(name = "facility")
    @OneToMany(mappedBy = "region")
    @JsonManagedReference
    private Set<OnmsFacility> m_facilities = new LinkedHashSet<>();


    public OnmsRegion() {}

    public OnmsRegion(Integer id) {
        this.m_id = id;
    }

    public OnmsRegion(Integer id, String name) {
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
     * <p>getFacilities</p>
     *
     * @return a {@link Set} object.
     */
    public Set<OnmsFacility> getFacilities() {
        return m_facilities;
    }

    /**
     * <p>setFacilities</p>
     *
     * @param facilities a {@link Set} object.
     */
    public void setFacilities(Set<OnmsFacility> facilities) {
        m_facilities = facilities;
    }

    /**
     * <p>addMonitoringLocation</p>
     *
     * @param facility a {@link OnmsMonitoringLocation} object.
     * @since 1.8.1
     */
    public void addFacility(OnmsFacility facility) {
        getFacilities().add(facility);
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
        if (obj instanceof OnmsRegion) {
            OnmsRegion app = (OnmsRegion) obj;
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
