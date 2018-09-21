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
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "profiles")
@XmlRootElement(name = "profile")
@XmlAccessorType(XmlAccessType.NONE)
public class OnmsProfile implements Serializable {

    private static final long serialVersionUID = -5772069175604961577L;

    @Id
    @Column(name="id")
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    @XmlAttribute(name = "id")
    private Integer m_id;

    @XmlAttribute(name = "name")
    @Column(name = "name", nullable = false, unique = true)
    private String m_name;

    @XmlAttribute(name = "ssid")
    @Column(name = "ssid")
    private String m_ssid;

    @XmlAttribute(name = "op-mode")
    @Column(name = "opMode")
    private Integer m_opMode;

    @XmlAttribute(name = "bandwidth")
    @Column(name = "bandwidth")
    private Integer m_bandwidth;

    @XmlAttribute(name = "channel")
    @Column(name = "channel")
    private Integer m_channel;

    @XmlAttribute(name = "ip-address")
    @Column(name = "ipAddress")
    private String m_ipAddress;

    @XmlAttribute(name = "country-code")
    @Column(name = "countryCode")
    private Integer m_countryCode;

    @XmlAttribute(name = "file-path")
    @Column(name = "filePath")
    private String m_filePath;

    @XmlAttribute(name = "modified-on")
    @Column(name = "modifiedOn")
    private Date m_modifiedOn;


    public OnmsProfile() {}

    public OnmsProfile(Integer id, String name) {
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
     * <p>getSsid</p>
     *
     * @return a {@link String} object.
     */
    public String getSsid() {
        return m_ssid;
    }

    /**
     * <p>setSsid</p>
     *
     * @param ssid a {@link String} object.
     */
    public void setSsid(String ssid) {
        this.m_ssid = ssid;
    }

    /**
     * <p>getOpMode</p>
     *
     * @return a {@link Integer} object.
     */
    public Integer getOpMode() {
        return m_opMode;
    }

    /**
     * <p>setOpMode</p>
     *
     * @param opMode a {@link Integer} object.
     */
    public void setOpMode(Integer opMode) {
        this.m_opMode = opMode;
    }

    /**
     * <p>getBandwidth</p>
     *
     * @return a {@link Integer} object.
     */
    public Integer getBandwidth() {
        return m_bandwidth;
    }

    /**
     * <p>setBandwidth</p>
     *
     * @param bandwidth a {@link Integer} object.
     */
    public void setBandwidth(Integer bandwidth) {
        this.m_bandwidth = bandwidth;
    }

    /**
     * <p>getChannel</p>
     *
     * @return a {@link Integer} object.
     */
    public Integer getChannel() {
        return m_channel;
    }

    /**
     * <p>setChannel</p>
     *
     * @param channel a {@link Integer} object.
     */
    public void setChannel(Integer channel) {
        this.m_channel = channel;
    }

    /**
     * <p>getIpAddress</p>
     *
     * @return a {@link String} object.
     */
    public String getIpAddress() {
        return m_ipAddress;
    }

    /**
     * <p>setIpAddress</p>
     *
     * @param ipAddress a {@link String} object.
     */
    public void setIpAddress(String ipAddress) {
        this.m_ipAddress = ipAddress;
    }

    /**
     * <p>getCountryCode</p>
     *
     * @return a {@link Integer} object.
     */
    public Integer getCountryCode() {
        return m_countryCode;
    }

    /**
     * <p>setCountryCode</p>
     *
     * @param countryCode a {@link Integer} object.
     */
    public void setCountryCode(Integer countryCode) {
        this.m_countryCode = countryCode;
    }

    /**
     * <p>getFilePath</p>
     *
     * @return a {@link String} object.
     */
    public String getFilePath() {
        return m_filePath;
    }

    /**
     * <p>setFilePath</p>
     *
     * @param filePath a {@link String} object.
     */
    public void setFilePath(String filePath) {
        this.m_filePath = filePath;
    }

    /**
     * <p>getModifiedOn</p>
     *
     * @return a {@link Date} object.
     */
    public Date getModifiedOn() {
        return m_modifiedOn;
    }

    /**
     * <p>setModifiedOn</p>
     *
     * @param modifiedOn a {@link Date} object.
     */
    public void setModifiedOn(Date modifiedOn) {
        this.m_modifiedOn = modifiedOn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        ToStringCreator creator = new ToStringCreator(this);
        creator.append("id", getId());
        creator.append("name", getName());
        creator.append("ssid", getSsid());
        creator.append("opMode", getOpMode());
        creator.append("bandwidth", getBandwidth());
        creator.append("channel", getChannel());
        creator.append("ipAddress", getIpAddress());
        creator.append("countryCode", getCountryCode());
        creator.append("filePath", getFilePath());
        creator.append("modifiedOn", getModifiedOn());
        return creator.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OnmsProfile) {
            OnmsProfile app = (OnmsProfile) obj;
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
