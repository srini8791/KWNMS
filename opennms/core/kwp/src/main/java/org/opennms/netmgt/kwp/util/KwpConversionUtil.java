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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class KwpConversionUtil {

    /**
     * Converts integer to 4 bytes
     *
     * @param i
     * @return
     */
    public static byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /* >> 0 */);

        return result;
    }

    /**
     * Converts byte array to int
     *
     * @param packet
     * @return
     */
    public static int byteToInt(byte[] packet) {
        if (packet != null) {
            int i = (packet[0] << 24) & 0xff000000 | (packet[1] << 16) & 0x00ff0000
                    | (packet[2] << 8) & 0x0000ff00 | (packet[3] << 0) & 0x000000ff;
            return i;
        }
        return 0;
    }

    /**
     * Converts bytes to short
     *
     * @param bytes
     * @return
     */
    public static short bytesToShort(byte[] bytes) {
        if (bytes != null) {
            short s = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
                    .getShort();
            return s;
        }
        return 0;
    }

    /**
     * Converts bytes to short
     *
     * @param bytes
     * @return
     */
    public static int shortBytesToInt(byte[] bytes) {
        return bytesToShort(bytes);
    }

    /**
     * Converts from short to bytes
     *
     * @param value
     * @return
     */
    public static byte[] shortToBytes(short value) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN)
                .putShort(value).array();
    }

    /**
     * Converts from int to 2 bytes
     *
     * @param value
     * @return
     */
    public static byte[] intToShortBytes(int value) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short)value).array();
    }

    public static byte[] shortToSingleByte(short value) {
        byte[] b = { (byte) value };
        return b;
    }

    public static byte[] intToSingleByte(int value) {
        byte[] b = { (byte) value };
        return b;
    }

    public static String byteArray2MAC(byte[] arr) {
        if (arr != null) {
            StringBuilder sb = new StringBuilder();
            for (byte b : arr) {
                if (sb.length() > 0)
                    sb.append(':');
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        return "";
    }

    public static int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

    public static int unsignedIntToInt(int i) {
        return unsignedByteToInt((byte) i);
    }

    public static int unsignedStringToInt(String s)
            throws NumberFormatException {
        int v = Integer.parseInt(s);
        return unsignedIntToInt(v);
    }

    public static byte[] convertIPAddressToBytes(String address) {
        InetAddress add;
        byte[] b = { 0, 0, 0, 0 };
        try {
            add = InetAddress.getByName(address);
            b = add.getAddress();
        } catch (UnknownHostException e) {
        }
        return b;
    }

    public static byte[] coverMACToBytes(String mac) {
        mac = mac.replace(":", "");
        byte[] bytes = new byte[6];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = Byte.parseByte(mac.substring(i * 2, (i + 1) * 2));
        }
        return bytes;
    }

    public static byte[] convertMACToBytes(String macAddr) {
        macAddr = macAddr.replaceAll(":", "");
        byte[] b = new byte[macAddr.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(macAddr.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

}
