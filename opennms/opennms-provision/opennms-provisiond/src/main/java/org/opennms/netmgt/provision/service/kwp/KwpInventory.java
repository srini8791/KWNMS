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
import org.opennms.netmgt.kwp.KwpPacket;

public class KwpInventory extends AbstractKwpProxiableTracker {

    // Constants
    public static final byte SERIAL_NUMBER_TYPE = 1;
    public static final byte MODEL_NUMBER_TYPE = 2;
    public static final byte FIRMWARE_VERSION_TYPE = 3;
    public static final byte ETH_SPEED_TYPE = 4;
    public static final byte BANDWIDTH_LIMIT_TYPE = 5;


    // variables
    private KwpPacket packet;
    private String serialNumber;
    private String modelNumber;
    private String firmwareVersion;
    private String ethSpeed;
    private String bandwidthLimit;


    @Override
    public void handleResponses(KwpPacket response) {
        this.packet = response;
        this.serialNumber = packet.getStringValueFromLTVByType(SERIAL_NUMBER_TYPE);
        this.modelNumber = packet.getStringValueFromLTVByType(MODEL_NUMBER_TYPE);
        this.firmwareVersion = packet.getStringValueFromLTVByType(FIRMWARE_VERSION_TYPE);
        this.ethSpeed = packet.getStringValueFromLTVByType(ETH_SPEED_TYPE);
        this.bandwidthLimit = packet.getStringValueFromLTVByType(BANDWIDTH_LIMIT_TYPE);

    }
}
