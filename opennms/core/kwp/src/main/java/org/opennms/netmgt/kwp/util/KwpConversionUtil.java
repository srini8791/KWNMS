/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018-2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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
package org.opennms.netmgt.kwp.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class KwpConversionUtil {

    /**
     * Converts integer to 4 bytes
     *
     * @param intValue
     * @return
     */
    public static byte[] toBytes(int intValue) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(intValue).array();
//        byte[] result = new byte[4];
//        result[0] = (byte) (i >> 24);
//        result[1] = (byte) (i >> 16);
//        result[2] = (byte) (i >> 8);
//        result[3] = (byte) (i /*>> 0*/);
//
//        return result;
    }


    /**
     * Converts byte array to int
     *
     * @param packet
     * @return
     */
    public static int byteToInt(byte[] packet) {
        return ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getInt();
//        int i = (packet[0] << 24) & 0xff000000 |
//                (packet[1] << 16) & 0x00ff0000 |
//                (packet[2] << 8) & 0x0000ff00 |
//                (packet[3] << 0) & 0x000000ff;
//        return i;
    }

    /**
     * Converts bytes to short
     *
     * @param bytes
     * @return
     */
    public static short bytesToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    /**
     * Converts from short to bytes
     *
     * @param value
     * @return
     */
    public static byte[] shortToBytes(short value) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
    }

}
