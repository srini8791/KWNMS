/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.service.operations;

import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.provision.service.snmp.KwpConfigurationGroup;
import org.opennms.netmgt.provision.service.snmp.KwpInventoryGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>ScanResource class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class ConfigScanResource {
    private String m_type;
    private OnmsNode m_node = null;
    private final Map<String,Object> m_attributes = new HashMap<String,Object>();

    public ConfigScanResource(String type) {
        m_type = type;
    }

    /**
     * <p>getType</p>
     *
     * @return a {@link String} object.
     */
    public String getType() {
        return m_type;
    }

    /**
     * <p>setNode</p>
     *
     * @param node a {@link OnmsNode} object.
     */
    public void setNode(OnmsNode node) {
        m_node = node;
    }

    /**
     * <p>getNode</p>
     *
     * @return a {@link OnmsNode} object.
     */
    public OnmsNode getNode() {
        return m_node;
    }

    // TODO: change node comparison to use spring
    /**
     * <p>setAttribute</p>
     *
     * @param key a {@link String} object.
     * @param value a {@link String} object.
     */
    public void setAttribute(String key, Object value ) {
        m_attributes.put(key, value);
        if (m_node != null && value != null) {
            if (KwpConfigurationGroup.RADIO_MODE_ALIAS.equals(key)) {
                m_node.setRadioMode((String)value);
                System.out.println("**** ConfigScanResource.setAttribute -- assetRecord = " + m_node.getAssetRecord());
            } else if (KwpConfigurationGroup.WIRELESS_BANDWIDTH_ALIAS.equals(key)) {
                m_node.setBandwidth((String)value);
            } else if (KwpConfigurationGroup.WIRELESS_ACTIVE_CHANNEL_ALIAS.equals(key)) {
                if (value instanceof Integer) {
                    m_node.setChannel((Integer) value);
                }
            }else if (key.equals(KwpConfigurationGroup.ETHERNET_MAC_ALIAS)) {
                m_node.setMacAddress((String)value);
            } else if (key.equals(KwpConfigurationGroup.ETHERNET_SPEED_ALIAS)) {
                m_node.getAssetRecord().setEthernetSpeed((String)value);
            } else if (KwpConfigurationGroup.WIRELESS_COUNTRY_ALIAS.equals(key)) {
                //TODO : need to add country variable. currently it is getting stored in OnmsGeolocation.country
                //m_node.getAssetRecord().getGeolocation().set
            } else if (KwpConfigurationGroup.WIRELESS_SSID_ALIAS.equals(key)) {
                m_node.setSsid(value.toString());
            } else if (KwpConfigurationGroup.WIRELESS_OPMODE_ALIAS.equals(key)) {
                m_node.setOpMode((String)value);
            } else if (KwpInventoryGroup.INV_SERIAL_ALIAS.equals(key)) {
                m_node.getAssetRecord().setSerialNumber((String)value);
            } else if (KwpInventoryGroup.INV_MODEL_ALIAS.equals(key)) {
                m_node.getAssetRecord().setModelNumber((String)value);
            }else if (key.equals(KwpInventoryGroup.INV_PRODUCT_CODE_ALIAS)) {
                if (value instanceof Integer) {
                    int v = ((Integer) value).intValue();
                    if (v == 1) {
                        m_node.setProductCode("ptp");
                    } else if (v == 2) {
                        m_node.setProductCode("ptmp");
                    } else if (v == 3) {
                        m_node.setProductCode("indoorap");
                    } else if (v == 4) {
                        m_node.setProductCode("outdoorap");
                    }
                }
                /*if(value.endsWith(".2.1")) {
                    m_node.setProductCode("ptp");
                } else if(value.endsWith(".2.2")) {
                    m_node.setProductCode("ptmp");
                } else if(value.endsWith(".2.3")) {
                    m_node.setProductCode("indoorap");
                } else if(value.endsWith(".2.4")) {
                    m_node.setProductCode("outdoorap");
                }*/
            } else if (KwpConfigurationGroup.WIRELESS_BANDWIDTH_LIMIT_IO_ALIAS.equals(key)) {
                Integer iobw = ((Integer)m_attributes.get(KwpConfigurationGroup.WIRELESS_BANDWIDTH_LIMIT_INPUT_ALIAS) +
                        (Integer)m_attributes.get(KwpConfigurationGroup.WIRELESS_BANDWIDTH_LIMIT_OUTPUT_ALIAS));
                m_node.getAssetRecord().setIoBandwidthLimit(String.valueOf(iobw));
            } else if (KwpInventoryGroup.DEVICE_RELEASE_VERSION.equals(key)) {
                String sValue = (String) value;
                if (sValue.toString().length() == 0) {
                    sValue = m_attributes.get(KwpInventoryGroup.INV_RELEASE_NUM_ALIAS) + "." +
                            m_attributes.get(KwpInventoryGroup.INV_RELEASE_MAJ_NUM_ALIAS) + "." +
                            m_attributes.get(KwpInventoryGroup.INV_RELEASE_MIN_ALIAS) + "(" +
                            m_attributes.get(KwpInventoryGroup.INV_BUILD_NUM_ALIAS) + ")";

                }
                m_node.getAssetRecord().setFirmware(sValue);
            }
        }
    }

    /**
     * <p>getAttribute</p>
     *
     * @param key a {@link String} object.
     * @return a {@link String} object.
     */
    public Object getAttribute(String key) {
        return m_attributes.get(key);
    }
}
