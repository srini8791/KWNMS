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
package org.opennms.netmgt.kwp;

import org.opennms.netmgt.kwp.util.KwpConversionUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class KwpLTVPacket {

    private short length;
    private byte type;
    private byte[] value;

    /**
     * Default constructor
     */
    public KwpLTVPacket() {
    }

    /**
     * Constructing from type
     *
     * @param type data type
     */
    public KwpLTVPacket(byte type) {
        this.type = type;
    }

    /**
     * Constructing from type and byte array value
     *
     * @param type data type
     * @param value data as byte array
     */
    public KwpLTVPacket(byte type, byte[] value) {
        this.type = type;
        this.value = value;
        this.length = (short) value.length;
    }

    /**
     * Constructing from type and string value
     *
     * @param type data type
     * @param value data as string
     */
    public KwpLTVPacket(byte type, String value) {
        this(type,value.getBytes());
    }

    // Getters and Setters
    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    // Utility methods

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(KwpConversionUtil.shortToBytes(this.length)); // L
        stream.write(this.type); // T
        stream.write(this.value); // V
        return stream.toByteArray();
    }

    public int getTotalLtvLength() {
        return 2+1+value.length;
    }

    public short getShortValue() {
        return KwpConversionUtil.bytesToShort(value);
    }

    public int getShortIntValue() {
        return KwpConversionUtil.bytesToShort(value);
    }

    public int getUnsignedToInt() {
        if (value != null && value.length == 1) {
            return KwpConversionUtil.unsignedByteToInt(value[0]);
        } else {
            return getShortIntValue();
        }

    }


    public String getStringUTF8Value() throws UnsupportedEncodingException {
        if (value != null) {
            return new String(value,"UTF-8");
        }
        return "";
    }

    public int getTotalLength() {
        return Short.SIZE/Byte.SIZE /* length = short = 2 bytes */
               + Byte.SIZE /* type = byte = 1 byte */
               + value.length; /* value = length of byte array */
    }

    public int getIntValue() {
        return KwpConversionUtil.byteToInt(value);
    }

    public String getStringValue() {
        return new String(this.value);
    }

    public String getUnicodeStringValue() throws UnsupportedEncodingException {
        return new String(this.value, "UTF-8");
    }

    @Override
    public String toString() {
        return "KwpLTVPacket {" +
                "length=" + length +
                ", type=" + type +
                ", value=" + Arrays.toString(value) +
                "}";
    }
}
