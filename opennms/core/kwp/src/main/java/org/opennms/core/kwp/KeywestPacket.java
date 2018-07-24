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
package org.opennms.core.kwp;

import org.opennms.core.kwp.util.KeywestConversionUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Keywest packet which is used as a protocol
 * for transferring data from KW device to NMS/APP
 */
public class KeywestPacket {

    private static final int PACKET_MAX_SIZE = 1307;

    protected DatagramPacket packet;

    /**
     * total payload which contains the LTVs
     */
    protected byte[] payload = new byte[1300];

    private int length = 0;

    private KeywestPacketHeader header = null;

    private List<KeywestLTVPacket> ltvPackets = new ArrayList<>();

    /**
     * Default constructor
     */
    public KeywestPacket() {
    }

    /**
     * Constructing from identifier, requestType and contentType
     *
     * @param id
     * @param requestType
     * @param contentType
     */
    public KeywestPacket(byte id, byte requestType, byte contentType) {
        this.header = new KeywestPacketHeader(id, (byte) 1, requestType, contentType);
        this.header.setLength((short) this.payload.length);
        this.length = this.payload.length;
    }

    /**
     * Constructing from DatagramPacket
     *
     * @param packet {@link DatagramPacket}
     */
    public KeywestPacket(DatagramPacket packet) {
        this.packet = packet;
        initialize(packet.getData());
    }

    public KeywestPacket(byte[] packetData) {
        initialize(packetData);
    }

    private void initialize(byte[] packetData) {
        populateHeader(packetData);
        populatePayload(packetData);
        if (this.payload != null) {
            populateLTVPackets();
        }
    }

    private void populateHeader(byte[] packetData) {
        this.header = new KeywestPacketHeader(getHeader(packetData));
    }

    private void populatePayload(byte[] packetData) {
        this.payload = Arrays.copyOfRange(
                packetData,
                KeywestPacketHeader.HEADER_SIZE,
                packetData.length);
    }

    private void populateLTVPackets() {
        while (this.payload.length > 0) {
            KeywestLTVPacket ltv = getLTVFromPacket();
            if (ltv == null) {
                break;
            }
            ltvPackets.add(ltv);
        }
    }

    /**
     * Retrieves the int value from the payload
     *
     * @param payloadData payload data
     * @param startIndex  start index to read from
     * @param endIndex    end index to read upto
     * @return
     */
    protected int getIntValueFromPacket(byte[] payloadData, int startIndex, int endIndex) {
        byte[] arr = Arrays.copyOfRange(payloadData, startIndex, endIndex);
        return getPacketLength(arr);
    }

/*
    private int getPacketLength(byte[] arr) {
        return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
*/

    private int getPacketLength(byte[] packet) {
        return (packet[0] << 24) & 0xff000000 |
                (packet[1] << 16) & 0x00ff0000 |
                (packet[2] << 8) & 0x0000ff00 |
                (packet[3] << 0) & 0x000000ff;
    }

    /**
     * Retreives the short value from the payload
     *
     * @param payloadData payload data
     * @param startIndex  start index to read from
     * @param endIndex    end index to read upto
     * @return
     */
    protected short getShortValueFromPacket(byte[] payloadData, int startIndex, int endIndex) {
        byte[] arr = Arrays.copyOfRange(payloadData, startIndex, endIndex);
        return KeywestConversionUtil.bytesToShort(arr);
    }

    private byte[] getHeader(byte[] data) {
        return Arrays.copyOfRange(data, 0, KeywestPacketHeader.HEADER_SIZE);
    }

    public KeywestPacketHeader getHeader() {
        return this.header;
    }

    /**
     * Retrieves the LTV from the packets
     *
     * @return
     */
    protected KeywestLTVPacket getLTVFromPacket() {
        KeywestLTVPacket ltv = new KeywestLTVPacket();

        short length = getShortValueFromPacket(this.payload, 0, 2);
        if (length == 0) {
            return null;
        }
        ltv.setLength(length);
        // retreve the ltv type
        byte type = this.payload[2];
        ltv.setType(type);
        // from 3rd byte to payload length retrieve the remaining payload (length (2 bytes) + type (1 byte))
        this.payload = Arrays.copyOfRange(this.payload, 3, this.payload.length);
        // retrieve the ltv data from the first byte to length
        byte[] value = Arrays.copyOfRange(this.payload, 0, length);
        // after the LTV is retrieved discard the bytes from 0 to length
        this.payload = Arrays.copyOfRange(this.payload, length, payload.length);
        ltv.setValue(value);
        // return ltv
        return ltv;
    }

    public DatagramPacket getPacket() {
        return packet;
    }

    /**
     * @param type
     * @return
     */
    public KeywestLTVPacket getLTVPacketByType(int type) {
        for (KeywestLTVPacket ltv : ltvPackets) {
            if (ltv.getType() == type) {
                return ltv;
            }
        }
        return null;
    }

    public byte[] getValueFromLTVByType(int type) {
        KeywestLTVPacket ltv = getLTVPacketByType(type);
        if (ltv != null && ltv.getValue() != null && ltv.getValue().length > 0) {
            return ltv.getValue();
        }
        return null;
    }

    public String getStringValueFromLTVByType(int type) {
        KeywestLTVPacket ltv = getLTVPacketByType(type);
        if (ltv != null && ltv.getValue() != null && ltv.getValue().length > 0) {
            return new String(ltv.getValue());
        }
        return null;
    }

/*
	public String getMacAddressFromLTV() {
		String macAddress = "";
		byte[] arr = getValueFromLTVByType(ProximControllerUtil.OP_CODE_MAC_ADDR_TYPE);
		macAddress = ControllerUtil.byteArray2MAC(arr);
		return macAddress == null ? "" : macAddress.trim().toUpperCase();
	}
*/

    public KeywestLTVPacket getOpCodeLTVPacket() {
        return ltvPackets.get(0);
    }

    public List<KeywestLTVPacket> getLTVPacket() {
        return ltvPackets;
    }

    public void addLTVToPacket(KeywestLTVPacket ltv) {
        ltvPackets.add(ltv);
    }

    public byte[] toByteArray() throws IOException {
        int totalLength = 0;
        if (ltvPackets != null && ltvPackets.size() > 0) {
            for (KeywestLTVPacket ltv : ltvPackets) {
                totalLength += ltv.getTotalLength();
            }
        } else {
            totalLength = length;
        }
        this.header.setLength((short) totalLength);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(this.header.getHeader());

        if (ltvPackets != null && ltvPackets.size() > 0) {
            for (KeywestLTVPacket ltv : ltvPackets) {
                stream.write(ltv.toByteArray());
            }
        } else {
            stream.write(payload);
        }

        return stream.toByteArray();
    }

    @Override
    public String toString() {
        return "KeywestPacket {" +
                "packet=" + packet +
                ", payload=" + Arrays.toString(payload) +
                ", length=" + length +
                ", header=" + header +
                ", ltvPackets=" + ltvPackets +
                "}";
    }

}
