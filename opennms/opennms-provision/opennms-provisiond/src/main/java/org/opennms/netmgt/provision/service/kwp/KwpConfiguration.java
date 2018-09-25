/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.service.kwp;

import org.opennms.netmgt.kwp.AbstractKwpProxiableTracker;
import org.opennms.netmgt.kwp.KwpLTVPacket;
import org.opennms.netmgt.kwp.KwpPacket;
import org.opennms.netmgt.model.Bandwidth;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OpMode;
import org.opennms.netmgt.model.RadioMode;

public class KwpConfiguration extends AbstractKwpProxiableTracker {

    private KwpPacket packet;


    public static final byte ADDRESS_TYPE = 2;
    public static final byte SSID_TYPE = 3;
    public static final byte MODE_TYPE = 4;
    public static final byte BANDWIDTH_TYPE = 5;
    public static final byte CHANNEL_TYPE = 6;
    public static final byte IPADDRESS_TYPE = 1;
    public static final byte DEVICE_MODE = 7;
    public static final byte DEVICE_MAC = 8;
    public static final byte COUNTRY_CODE = 9;
    public static final byte GATEWAY_IP = 10;
    public static final byte NETMASK_IP = 11;
    public static final byte CUSTOMER_NAME = 12;
    public static final byte LINK_ID = 13;

    private String ssid = null;

    private Bandwidth channelBW = Bandwidth.BANDWIDTH_UNKNOWN;

    private OpMode operationalMode;

    private int channel = 0;

    private int ipAddrType = 0;

    private String ipAddress = null;

    private RadioMode deviceMode;

    private String deviceMac = "";

    private int countryCode = 0;

    private String gatewayIp = "";

    private String netMask = "";

    private String custName = "";

    private int linkId = -1;

    @Override
    public void handleResponses(KwpPacket response) {
        this.packet = response;

        this.ipAddress = packet.getIPAddressBytesFromLTVByType(IPADDRESS_TYPE);
        KwpLTVPacket ltv = packet.getLTVPacketByType(ADDRESS_TYPE);
        if (ltv != null) {
            this.ipAddrType = ltv.getUnsignedToInt();
        }
        this.ssid = packet.getStringValueFromLTVByType(SSID_TYPE);
        ltv = packet.getLTVPacketByType(BANDWIDTH_TYPE);
        if (ltv != null) {
            try {
                this.channelBW = Bandwidth.get(ltv.getUnsignedToInt());
            } catch (IllegalArgumentException ex) {
            }

        }
        ltv = packet.getLTVPacketByType(MODE_TYPE);
        if (ltv != null) {
            this.operationalMode = OpMode.get(ltv.getUnsignedToInt());
        }
        ltv = packet.getLTVPacketByType(CHANNEL_TYPE);
        if (ltv != null) {
            this.channel = ltv.getUnsignedToInt();
        }
        ltv = packet.getLTVPacketByType(DEVICE_MODE);
        if (ltv != null) {
            this.deviceMode = RadioMode.get(ltv.getUnsignedToInt());
        }
        this.setDeviceMac(packet.getMacAddressFromLTV(DEVICE_MAC));
        ltv = packet.getLTVPacketByType(COUNTRY_CODE);
        if (ltv != null) {
            this.countryCode = ltv.getShortIntValue();
        }
        this.gatewayIp = packet.getIPAddressBytesFromLTVByType(GATEWAY_IP);
        this.netMask = packet.getIPAddressBytesFromLTVByType(NETMASK_IP);
        this.custName = packet.getStringValueFromLTVByType(CUSTOMER_NAME);
        ltv = packet.getLTVPacketByType(LINK_ID);
        if (ltv != null) {
            this.linkId = ltv.getUnsignedToInt();
        }

    }


    public void updateKwpDataforNode(OnmsNode node) {
        if (this.packet != null) {
            node.setSsid(ssid);
            node.setChannel(this.channel);
            node.setBandwidth(this.channelBW);
            node.setOpMode(this.operationalMode);
            node.setBandwidth(this.channelBW);
            node.setRadioMode(this.deviceMode);
            node.setMacAddress(this.deviceMac);
            //TODO
            // Add bsu/su node detected event
            /*EventBuilder builder = new EventBuilder(EventConstants.NODE_ADDED_EVENT_UEI, Provisioner.NAME);
            builder.setNodeid(node.getId());
            builder.getEvent().setDescr(this.deviceMode.toString()  + " discovered");
            builder.s*/
            node.setActive(true);
        }
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
}
