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
import java.util.Arrays;

/**
 * Header data in the {@link KwpPacket}
 * <p>
 * Size of the total header is      6 bytes
 * =========================================
 * Unique Identifier                1 byte
 * Type of interface                1 byte
 * Size of buffer                   2 bytes
 * Request Type (Get/Set & Req/Res) 1 byte
 * Structure or text file           1 byte
 * =========================================
 */
public class KwpPacketHeader {


    /**
     * Standard size of each Keywest Packet Header
     */
    public static final int HEADER_SIZE = 6;

    /**
     * Unique identifier with possible values 1 & 2.
     * 1 is for Sify
     */
    private byte id;

    /**
     * Interface Type
     * 1 => App
     * 2 => NMS
     */
    private byte ifType;

    /**
     * Length of LTVs available in the packet
     */
    private byte[] length;

    /**
     * Type of the Request (Get/Set, Req/Res)
     */
    private byte requestType;

    /**
     * Content Type (struct or text file)
     */
    private byte contentType;

    /**
     * Default constructor
     */
    public KwpPacketHeader() {
    }

    /**
     * Constructing from byte array
     *
     * @param headerData
     */
    public KwpPacketHeader(byte[] headerData) {
        if (headerData == null || headerData.length != 6) {
            throw new IllegalArgumentException("Invalid header data.");
        }
        id = headerData[0];
        ifType = headerData[1];
        length = Arrays.copyOfRange(headerData, 2, 4);
        requestType = headerData[4];
        contentType = headerData[5];
    }

    /**
     * Constructs a new header
     *
     * @param id
     * @param ifType
     * @param requestType
     * @param contentType
     */
    public KwpPacketHeader(byte id, byte ifType, byte requestType, byte contentType) {
        this.id = id;
        this.ifType = ifType;
        this.requestType = requestType;
        this.contentType = contentType;
    }


    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public byte getIfType() {
        return ifType;
    }

    public void setIfType(byte ifType) {
        this.ifType = ifType;
    }

    public byte getRequestType() {
        return requestType;
    }

    public void setRequestType(byte requestType) {
        this.requestType = requestType;
    }

    public byte getContentType() {
        return contentType;
    }

    public void setContentType(byte contentType) {
        this.contentType = contentType;
    }

    public void setLength(short length) {
        this.length = KwpConversionUtil.shortToBytes(length);
    }

    public short getLength() {
        return KwpConversionUtil.bytesToShort(this.length);
    }

    /**
     * Builds and returns the header in bytes
     *
     * @return byte[] byte array
     * @throws IOException
     */
    public byte[] getHeader() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(this.id);
        stream.write(this.ifType);
        stream.write(this.length);
        stream.write(this.requestType);
        stream.write(this.contentType);
        return stream.toByteArray();
    }

    @Override
    public String toString() {
        return "KwpPacketHeader {id : " + id + ", length: " + getLength() + "}";
    }

}
