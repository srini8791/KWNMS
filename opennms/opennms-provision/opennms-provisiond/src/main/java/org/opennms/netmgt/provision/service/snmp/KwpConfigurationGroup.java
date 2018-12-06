/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.service.snmp;


import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.provision.service.operations.ConfigScanResource;
import org.opennms.netmgt.provision.service.operations.ScanResource;
import org.opennms.netmgt.snmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * SystemGroup holds the system group properties It implements the SnmpHandler
 * to receive notifications when a reply is received/error occurs in the
 * SnmpSession used to send requests/receive replies.
 * </P>
 *
 * @author <A HREF="mailto:sowmya@opennms.org">Sowmya </A>
 * @author <A HREF="mailto:weave@oculan.com">Weave </A>
 * @see <A HREF="http://www.ietf.org/rfc/rfc1213.txt">RFC1213 </A>
 */
public final class KwpConfigurationGroup extends AggregateTracker {
    private static final Logger LOG = LoggerFactory.getLogger(KwpConfigurationGroup.class);



    //
    // Lookup strings for specific table entries
    //
    /** Constant <code>SYS_OBJECTID_ALIAS="sysObjectID"</code> */
    public static final String RADIO_MODE_ALIAS = "wirelessRadioMode";
    private static final String  RADIO_MODE = " .1.3.6.1.4.1.52619.1.1.1.1.1.2.2";


    /** Constant <code>SYS_OBJECTID_ALIAS="sysObjectID"</code> */
    public static final String    WIRELESS_SSID_ALIAS = "wirelessSSID";
    private static final String WIRELESS_SSID = " .1.3.6.1.4.1.52619.1.1.1.1.1.3.2";

    /** Constant <code>SYS_UPTIME_ALIAS="sysUptime"</code> */
    public static final String WIRELESS_COUNTRY_ALIAS = "wirelessCountry";
    private static final String WIRELESS_COUNTRY = " .1.3.6.1.4.1.52619.1.1.1.1.1.4.2";

    /** Constant <code>SYS_NAME_ALIAS="sysName"</code> */
    public static final String WIRELESS_OPMODE_ALIAS = "wirelessOpmode";
    private static final String WIRELESS_OPMODE = " .1.3.6.1.4.1.52619.1.1.1.1.1.5.2";

    /** Constant <code>SYS_DESCR_ALIAS="sysDescr"</code> */
    public static final String WIRELESS_BANDWIDTH_ALIAS = "wirelessBandwidth";
    private static final String WIRELESS_BANDWIDTH = ".1.3.6.1.4.1.52619.1.1.1.1.1.7.2";

    /** Constant <code>SYS_LOCATION_ALIAS="sysLocation"</code> */
    public static final String WIRELESS_ACTIVE_CHANNEL_ALIAS = "wirelessActiveChannel";
    private static final String WIRELESS_ACTIVE_CHANNEL = ".1.3.6.1.4.1.52619.1.1.1.1.1.23.2";

    /** Constant <code>SYS_LOCATION_ALIAS="sysLocation"</code> */
    public static final String WIRELESS_FREQUENCY_ALIAS = "wirelessFrequency";
    private static final String WIRELESS_FREQUENCY = ".1.3.6.1.4.1.52619.1.1.1.1.1.25.2";

    /** Constant <code>SYS_LOCATION_ALIAS="sysLocation"</code> */
    public static final String WIRELESS_BANDWIDTH_LIMIT_INPUT_ALIAS = "wirelessUplinkLimit";
    private static final String WIRELESS_BANDWIDTH_LIMIT_INPUT = ".1.3.6.1.4.1.52619.1.1.1.1.1.11.2";

    /** Constant <code>SYS_LOCATION_ALIAS="sysLocation"</code> */
    public static final String WIRELESS_BANDWIDTH_LIMIT_OUTPUT_ALIAS = "wirelessUplinkLimit";
    private static final String WIRELESS_BANDWIDTH_LIMIT_OUTPUT = ".1.3.6.1.4.1.52619.1.1.1.1.1.12.2";


    public static final String WIRELESS_BANDWIDTH_LIMIT_IO_ALIAS = "wirelessBandwidhIO";


    /** Constant <code>SYS_CONTACT_ALIAS="sysContact"</code> */
    public static final String ETHERNET_MAC_ALIAS = "ethernetMACAddress";
    private static final String ETHERNET_MAC = ".1.3.6.1.4.1.52619.1.3.2.1.3.1";

    /** Constant <code>SYS_CONTACT_ALIAS="sysContact"</code> */
    public static final String ETHERNET_SPEED_ALIAS = "ethernetSpeed";
    private static final String ETHERNET_SPEED = ".1.3.6.1.4.1.52619.1.3.2.1.4.1";


    /**
     * <P>
     * The keys that will be supported by default from the TreeMap base class.
     * Each of the elements in the list are an instance of the SNMP Interface
     * table. Objects in this list should be used by multiple instances of this
     * class.
     * </P>
     */
    public static NamedSnmpVar[] ms_elemList = null;

    /**
     * <P>
     * Initialize the element list for the class. This is class wide data, but
     * will be used by each instance.
     * </P>
     */
    static {

        ms_elemList = new NamedSnmpVar[11];
        int ndx = 0;

        /**
         * <P>
         * A description of the remote entity. For example this may include
         * hardware, operating system, and various version information. This
         * should be a US-ASCII display string.
         * </P>
         */
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, RADIO_MODE_ALIAS, RADIO_MODE);

        /**
         * <P>
         * The vendor's authoritative identification of the network management
         * subsystem. This can often be used to identify the vendor, and often
         * times the specific vendor's hardware platform.
         * </P>
         */
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, WIRELESS_SSID_ALIAS, WIRELESS_SSID);

        /**
         * <P>
         * The vendor's authoritative identification of the network management
         * subsystem. This can often be used to identify the vendor, and often
         * times the specific vendor's hardware platform.
         * </P>
         */
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPINT32, WIRELESS_COUNTRY_ALIAS, WIRELESS_COUNTRY);

        /**
         * <P>
         * The time since the network management portion of the system was last
         * initialized. This will be in 1/100th of a second increments.
         * </P>
         */
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, WIRELESS_OPMODE_ALIAS, WIRELESS_OPMODE);

        /**
         * <P>
         * The identification and contact information for the person that is
         * managing this node. While the contact information is often used to
         * store contact information about the person managing the node, it is a
         * free form US-ASCII field that may contain additional information
         * depending on the environment.
         * </P>
         */
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, WIRELESS_BANDWIDTH_ALIAS, WIRELESS_BANDWIDTH);

        /**
         * <P>
         * The administratively assigned name for this particular node. This may
         * often be the same as the hostname, but it can differ depending on the
         * site's implementation.
         * </P>
         */
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPINT32, WIRELESS_ACTIVE_CHANNEL_ALIAS, WIRELESS_ACTIVE_CHANNEL);

        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPINT32, WIRELESS_BANDWIDTH_LIMIT_INPUT_ALIAS, WIRELESS_BANDWIDTH_LIMIT_INPUT);

        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPINT32, WIRELESS_BANDWIDTH_LIMIT_OUTPUT_ALIAS, WIRELESS_BANDWIDTH_LIMIT_OUTPUT);

        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, ETHERNET_MAC_ALIAS, ETHERNET_MAC);

        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, ETHERNET_SPEED_ALIAS, ETHERNET_SPEED);

        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPINT32, WIRELESS_FREQUENCY_ALIAS, WIRELESS_FREQUENCY);


        /**
         * <P>
         * A value that indicates the set of services that this entity provides.
         * This is a bit encode integer that allows the management entity to
         * determine if the agent supports the following standards
         * </P>
         *
         * <UL>
         * <LI>physical (e.g. repeaters)</LI>
         * <LI>datalink/subnetwork (e.g. bridges)</LI>
         * <LI>internet (e.g. routers)</LI>
         * <LI>end-to-end (e.g. IP hosts)</LI>
         * <LI>applications (e.g. mail relays)</LI>
         * </UL>
         *
         * <P>
         * To get more information about the encoding see Page 123 of "SNMP,
         * SNMPv2, SNMPv3 and RMON 1 and 2 3rd Ed." by William Stallings [ISBN
         * 0-201-48534-6]
         * </P>
         */
        // ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPINT32,
        // "sysServices", ".1.3.6.1.2.1.1.7");
    }

    /**
     * <P>
     * The SYSTEM_OID is the object identifier that represents the root of the
     * system information in the MIB forest. Each of the system elements can be
     * retrieved by adding their specific index to the string, and an additional
     * Zero(0) to signify the single instance item.
     * </P>
     */
    public static final String SYSTEM_OID = ".1.3.6.1.2.1.1";

    private final SnmpStore m_store;
    private final InetAddress m_address;

    /**
     * <P>
     * The class constructor is used to initialize the collector and send out
     * the initial SNMP packet requesting data. The data is then received and
     * store by the object. When all the data has been collected the passed
     * signaler object is <EM>notified</em> using the notifyAll() method.
     * </P>
     *
     * @param address TODO
     */
    public KwpConfigurationGroup(InetAddress address) {
        super(NamedSnmpVar.getTrackersFor(ms_elemList));
        m_address = address;
        m_store = new SnmpStore(ms_elemList);
    }


    public String getRadioMode() {
        return m_store.getDisplayString(RADIO_MODE);
    }

    public String getWirelessBandwidth() {
        return m_store.getDisplayString(WIRELESS_BANDWIDTH);
    }

    public Integer getWirelessChannel() {
        return m_store.getInt32(WIRELESS_ACTIVE_CHANNEL);
    }

    public Integer getWirelessCountry() {
        return m_store.getInt32(WIRELESS_COUNTRY);
    }

    public String getWirelessOpmode() {
        return m_store.getDisplayString(WIRELESS_OPMODE);
    }

    public String getWirelessSsid() {
        return m_store.getDisplayString(WIRELESS_SSID);
    }

    public String getEthernetMac() {
        return m_store.getDisplayString(ETHERNET_MAC);
    }

    public String getEthernetSpeed() {
        return m_store.getDisplayString(ETHERNET_SPEED);
    }

    public Integer getIOBandwidth() { return  m_store.getInt32(WIRELESS_BANDWIDTH_LIMIT_INPUT) + m_store.getInt32(WIRELESS_BANDWIDTH_LIMIT_OUTPUT);}


    public Integer getWirelessFrequency() {
        return m_store.getInt32(WIRELESS_FREQUENCY);
    }

    /** {@inheritDoc} */
    @Override
    protected void storeResult(SnmpResult res) {
        m_store.storeResult(res);
    }
    /** {@inheritDoc} */
    @Override
    protected void reportGenErr(String msg) {
        LOG.warn("Error retrieving systemGroup from {}. {}", m_address, msg);
    }

    /** {@inheritDoc} */
    @Override
    protected void reportNoSuchNameErr(String msg) {
        LOG.info("Error retrieving systemGroup from {}. {}", m_address, msg);
    }

    @Override
    protected void reportFatalErr(final ErrorStatusException ex) {
        LOG.warn("Error retrieving systemGroup from {}. {}", m_address, ex.getMessage(), ex);
    }
    @Override
    protected void reportNonFatalErr(final ErrorStatus status) {
        LOG.info("Error ({}) retrieving systemGroup from {}. {}", status, m_address, status.retry()? "Retrying." : "Giving up.");
    }

    /**
     * <p>updateSnmpDataForResource</p>
     *
     * @param sr a {@link ScanResource} object.
     */
    public void updateSnmpDataForResource(ConfigScanResource sr) {
        if (!failed()) {
            sr.setAttribute(RADIO_MODE_ALIAS, getRadioMode());
            sr.setAttribute(WIRELESS_SSID, getWirelessSsid());
            sr.setAttribute(WIRELESS_BANDWIDTH, getWirelessBandwidth());
            sr.setAttribute(WIRELESS_ACTIVE_CHANNEL, getWirelessChannel());
            sr.setAttribute(WIRELESS_COUNTRY, getWirelessCountry());
            sr.setAttribute(WIRELESS_OPMODE,getWirelessOpmode());
            sr.setAttribute(WIRELESS_BANDWIDTH_LIMIT_INPUT,getIOBandwidth());
            sr.setAttribute(ETHERNET_MAC_ALIAS, getEthernetMac());
            sr.setAttribute(ETHERNET_SPEED_ALIAS, getEthernetMac());
            sr.setAttribute(WIRELESS_FREQUENCY_ALIAS, getWirelessFrequency());

            /*node.setSysObjectId(sysId);
            node.setSysName(sysName);
            node.setSysContact(sysContact);
            node.setSysDescription(sysDescription);
            node.getAssetRecord().getGeolocation().setLatitude(0.0D);
            node.getAssetRecord().getGeolocation().setLongitude(0.0D);*/
        }
    }

    /**
     * <p>updateSnmpDataForResource</p>
     *
     * @param sr a {@link ScanResource} object.
     */
    public void updateSnmpDataForResource(List<SnmpValue> values, ConfigScanResource resource) {

        if (values != null && values.size() == ms_elemList.length) {
            for (int i = 0; i < ms_elemList.length; i++) {
                SnmpValue value = values.get(i);
                NamedSnmpVar var = ms_elemList[i];
                if (var.getType().equals(NamedSnmpVar.SNMPINT32)) {
                    resource.setAttribute(var.getAlias(),value.toInt());
                } else if (var.getType().equals(NamedSnmpVar.SNMPOCTETSTRING)) {
                    resource.setAttribute(var.getAlias(),value.toDisplayString());
                }
            }
            resource.setAttribute(WIRELESS_BANDWIDTH_LIMIT_IO_ALIAS,"");
        }

        /*if (!failed()) {
            sr.setAttribute(RADIO_MODE_ALIAS, getRadioMode());
            sr.setAttribute(WIRELESS_SSID, getWirelessSsid());
            sr.setAttribute(WIRELESS_BANDWIDTH, getWirelessBandwidth());
            sr.setAttribute(WIRELESS_ACTIVE_CHANNEL, getWirelessChannel());
            sr.setAttribute(WIRELESS_COUNTRY, getWirelessCountry());
            sr.setAttribute(WIRELESS_OPMODE,getWirelessOpmode());

            *//*node.setSysObjectId(sysId);
            node.setSysName(sysName);
            node.setSysContact(sysContact);
            node.setSysDescription(sysDescription);
            node.getAssetRecord().getGeolocation().setLatitude(0.0D);
            node.getAssetRecord().getGeolocation().setLongitude(0.0D);*//*
        }*/
    }

    /**
     * <p>updateSnmpDataForNode</p>
     *
     * @param node a {@link OnmsNode} object.
     */
    public void updateSnmpDataForNode(OnmsNode node) {
        ConfigScanResource sr = new ConfigScanResource("SNMP");
        sr.setNode(node);
        updateSnmpDataForResource(sr);
    }

    /**
     * <p>updateSnmpDataForNode</p>
     *
     * @param node a {@link OnmsNode} object.
     */
    public void updateSnmpDataForNode(OnmsNode node,List<SnmpValue> values) {
        ConfigScanResource sr = new ConfigScanResource("SNMP");
        sr.setNode(node);
        updateSnmpDataForResource(values,sr);
    }

    public List<SnmpObjId> getGetList() {
        List<SnmpObjId> idList = new ArrayList<SnmpObjId>();
        for (int i = 0; i < ms_elemList.length; i++) {
            idList.add(ms_elemList[i].getSnmpObjId());
        }
        return idList;
    }

}
