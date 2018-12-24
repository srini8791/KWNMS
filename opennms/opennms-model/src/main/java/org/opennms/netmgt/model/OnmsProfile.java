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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "profiles")
@XmlRootElement(name = "profile")
@XmlAccessorType(XmlAccessType.NONE)
public class OnmsProfile implements Serializable {

    private static final long serialVersionUID = -5772069175604961577L;

    @Id
    @Column(name = "id")
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

    @XmlElement(name = "opMode")
    //@XmlJavaTypeAdapter(OpModeAdapter.class)
    @Column(name = "opMode")
    private String m_opMode;

    @XmlElement(name = "bandwidth")
    @Column(name = "bandwidth")
    //@XmlJavaTypeAdapter(BandwidthAdapter.class)
    private String m_bandwidth;

    @XmlAttribute(name = "channel")
    @Column(name = "channel")
    private String m_channel;

    @XmlAttribute(name = "nmsServerAddress")
    @Column(name = "nmsserveraddress")
    private String m_nmsServerAddress;

    @XmlAttribute(name = "tftpServerAddress")
    @Column(name = "tftpserveraddress")
    private String m_tftpServerAddress;

    @XmlAttribute(name = "minimumFirmware")
    @Column(name = "min_firmware")
    private String m_minimumFirmware;

    @XmlAttribute(name = "countryCode")
    @Column(name = "countryCode")
    private Integer m_countryCode;

    @XmlAttribute(name = "filePath")
    @Column(name = "filePath")
    private String m_filePath;

    @XmlAttribute(name = "modifiedOn")
    @Column(name = "modifiedOn", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date m_modifiedOn;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="profile_nodes",
            joinColumns={@JoinColumn(name="profileid", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="nodeid", referencedColumnName="nodeid")})
    private Set<OnmsNode> m_nodes;

    public OnmsProfile() {
    }

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
     * @return a {@link OpMode} object.
     */
    public String getOpMode() {
        return m_opMode;
    }

    /**
     * <p>setOpMode</p>
     *
     * @param opMode a {@link OpMode} object.
     */
    public void setOpMode(String opMode) {
        this.m_opMode = opMode;
    }

    /**
     * <p>getBandwidth</p>
     *
     * @return a {@link Bandwidth} object.
     */
    public String getBandwidth() {
        return m_bandwidth;
    }

    /**
     * <p>setBandwidth</p>
     *
     * @param bandwidth a {@link Bandwidth} object.
     */
    public void setBandwidth(String bandwidth) {
        this.m_bandwidth = bandwidth;
    }

    /**
     * <p>getChannel</p>
     *
     * @return a {@link Integer} object.
     */
    public String getChannel() {
        return m_channel;
    }

    /**
     * <p>setChannel</p>
     *
     * @param channel a {@link Integer} object.
     */
    public void setChannel(String channel) {
        this.m_channel = channel;
    }

    /**
     * <p>getNmsServerAddress</p>
     *
     * @return a {@link Integer} object.
     */
    public String getNmsServerAddress() {
        return m_nmsServerAddress;
    }

    /**
     * <p>setNmsServerAddress</p>
     *
     * @param channel a {@link Integer} object.
     */
    public void setNmsServerAddress(String nmsServerAddress) {
        this.m_nmsServerAddress = nmsServerAddress;
    }


    /**
     * <p>setNmsServerAddress</p>
     *
     * @param channel a {@link Integer} object.
     */
    public void setTftpServerAddress(String tftpServerAddress) {
        this.m_tftpServerAddress = tftpServerAddress;
    }

    /**
     * <p>getNmsServerAddress</p>
     *
     * @return a {@link Integer} object.
     */
    public String getTftpServerAddress() {
        return m_tftpServerAddress;
    }


    /**
     * <p>getMinimumFirmware</p>
     *
     * @return a {@link String} object.
     */
    public String getMinimumFirmware() {
        return m_minimumFirmware;
    }

    /**
     * <p>setMinimumFirmware</p>
     *
     * @param minimumFirmware a {@link String} object.
     */
    public void setMinimumFirmware(String minimumFirmware) {
        this.m_minimumFirmware = minimumFirmware;
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
     * <p>getNodes</p>
     *
     * @return set of {@link OnmsNode} objects.
     */
    public Set<OnmsNode> getNodes() {
        return m_nodes;
    }

    /**
     * <p>setNodes</p>
     *
     * @param nodes set of {@link OnmsNode} objects.
     */
    public void setNodes(Set<OnmsNode> nodes) {
        this.m_nodes = nodes;
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
        creator.append("minFirmware", getMinimumFirmware());
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
