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
import org.opennms.netmgt.model.OnmsNode;

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

    private String channelBW = "Unknown";

    private String operationalMode;

    private Integer channel = 0;

    private int countryCode = 0;

    private String gatewayIp = "";

    private String netMask = "";

    private String custName = "";

    private int linkId = -1;

    private String deviceMode;

    private String deviceMac = "";

    private String ipAddrType="";

    private String ipAddress="";

    @Override
    public void handleResponses(KwpPacket response) {
        this.packet = response;

        this.ipAddress = packet.getIPAddressBytesFromLTVByType(IPADDRESS_TYPE);
        KwpLTVPacket ltv = packet.getLTVPacketByType(ADDRESS_TYPE);
        if (ltv != null) {
            this.ipAddrType = ltv.getStringValue();
        }
        this.ssid = packet.getStringValueFromLTVByType(SSID_TYPE);
        ltv = packet.getLTVPacketByType(BANDWIDTH_TYPE);
        if (ltv != null) {
            try {
                this.channelBW = ltv.getStringValue();
            } catch (IllegalArgumentException ex) {
            }

        }
        ltv = packet.getLTVPacketByType(MODE_TYPE);
        if (ltv != null) {
            this.operationalMode = ltv.getStringValue();
        }
        ltv = packet.getLTVPacketByType(CHANNEL_TYPE);
        if (ltv != null) {
            try {
                this.channel = Integer.parseInt(ltv.getStringValue());
            } catch (NumberFormatException ne) {

            }
        }
        ltv = packet.getLTVPacketByType(DEVICE_MODE);
        if (ltv != null) {
            this.deviceMode = ltv.getStringValue();
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
            System.out.println("**** KwpConfiguration.updateKwpDataforNode -- assetRecord = " + node.getAssetRecord());
            //TODO country code might need to be set
            node.setActive(true);
        }
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
}
