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

public class KwpSysInfo extends AbstractKwpProxiableTracker {

    // Constants
    public static final byte SYSID_TYPE = 1;
    public static final byte SYSNAME_TYPE = 2;
    public static final byte SYSLOCATION_TYPE = 3;
    public static final byte SYSCONTACT_TYPE = 4;
    public static final byte SYSDESCRIPTION_TYPE = 5;
    public static final byte GEOLATITUDE_TYPE = 6;
    public static final byte GEOLONGITUDE_MODE = 7;

    // request/response packet
    KwpPacket packet = null;

    // Variables
    private String sysId;
    private String sysName;
    private String sysContact;
    private String sysDescription;
    private String geoLatitude;
    private String geLongitude;





    @Override
    public void handleResponses(KwpPacket response) {
        this.packet = response;
        this.sysId = packet.getStringValueFromLTVByType(SYSID_TYPE);
        this.sysName = packet.getStringValueFromLTVByType(SYSNAME_TYPE);
        this.sysContact = packet.getStringValueFromLTVByType(SYSCONTACT_TYPE);
        this.sysDescription = packet.getStringValueFromLTVByType(SYSDESCRIPTION_TYPE);
        this.geoLatitude = packet.getStringValueFromLTVByType(GEOLATITUDE_TYPE);
        this.geLongitude = packet.getStringValueFromLTVByType(GEOLONGITUDE_MODE);
    }
}
