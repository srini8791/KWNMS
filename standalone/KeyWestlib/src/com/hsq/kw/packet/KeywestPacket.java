package com.hsq.kw.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hsq.kw.packet.util.ConversionUtil;

/**
 * Keywest packet which is used as a protocol or transerring data from KW device
 * to NMS/APP
 * 
 * @author Spittala
 *
 */
public class KeywestPacket {

	private final int SIZE_OF_LENGTH = 2;

	private final int SIZE_OF_TYPE = 1;

	public static final int SIZE_OF_HEADER = 8;

	private final int PACKET_MAX_SIZE = 1307;

	/**
	 * total payload which contains the LTVs
	 */
	protected byte[] payload = new byte[300];

	/**
	 * 
	 */
	protected DatagramPacket packet;

	private PacketHeader header = null;

	protected List<KeywestLTVPacket> ltvPackets = new ArrayList<KeywestLTVPacket>();

	private int length = 0;

	public KeywestPacket() {
	}

	/**
	 * kewest packet built from the Datagram packet
	 * 
	 * @param packet
	 */
	public KeywestPacket(DatagramPacket packet) {
		this.packet = packet;
		try {
			// retreive the header
			this.header = new PacketHeader(getHeader(this.packet.getData()));
			// extract the paylead from 6 bytes to length specified in header
			payload = Arrays.copyOfRange(packet.getData(), SIZE_OF_HEADER, this.header.getLength() + SIZE_OF_HEADER);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (payload != null) {
			while (payload.length > 0) {
				KeywestLTVPacket ltv = getLTVFromPacket();
				if (ltv == null) {
					break;
				}
				ltvPackets.add(ltv);
			}
		}
	}

	public KeywestPacket(byte[] packet) {
		try {

			System.out.println("packetLength" + packet.length);
			this.header = new PacketHeader(getHeader(packet));
			// retreive the payload from the packet
			payload = Arrays.copyOfRange(packet, SIZE_OF_HEADER, this.header.getLength() + SIZE_OF_HEADER);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (payload != null) {
			// if (this.header.)
			while (payload.length > 0) {
				KeywestLTVPacket ltv = getLTVFromPacket();
				if (ltv == null) {
					break;
				}
				ltvPackets.add(ltv);
			}
		}
	}

	/**
	 * @param id
	 * @param type
	 * @param subType
	 */
	public KeywestPacket(byte id, byte type, byte subType) {
		this.header = new PacketHeader(id, (byte) 1, type, subType);
		this.header.setLength((short) payload.length);
		this.length = payload.length;
	}

	protected int getIntValueFromPacket(byte[] data, int startIdx, int endIdx) {
		if (data != null) {
			byte[] arr = Arrays.copyOfRange(data, 0, 4);
			System.out.println("arr" + arr.length);
			return getPacketLength(arr);
		}
		return 0;
	}

	/**
	 * retreives the short value from the payload
	 * 
	 * @param data     payload data
	 * @param startIdx start index from
	 * @param endIdx   end index
	 * @return
	 */
	protected short getShortValueFromPacket(byte[] data, int startIdx, int endIdx) {
		if (data != null) {
			byte[] arr = Arrays.copyOfRange(data, 0, SIZE_OF_LENGTH);
			System.out.println("arr" + arr.length);
			return ConversionUtil.bytesToShort(arr);
		}
		return 0;
	}

	private byte[] getHeader(byte[] data) {
		byte[] bytes = { 0 };
		if (data != null) {
			bytes = Arrays.copyOfRange(data, 0, SIZE_OF_HEADER);
			System.out.println("packetLength" + bytes.length);
			return bytes;
		}
		return bytes;
	}

	public PacketHeader getHeader() {
		return this.header;
	}

	/**
	 * retreives the LTV from the packets
	 * 
	 * @return
	 */
	protected KeywestLTVPacket getLTVFromPacket() {

		KeywestLTVPacket ltv = new KeywestLTVPacket();

		short length = getShortValueFromPacket(this.payload, 0, 2);
		/*
		 * if (length == 0) { return null; }
		 */

		ltv.setLength(length);
		// retreve the ltv type
		byte type = this.payload[2];
		ltv.setType(type);
		// from 3rd byte to payload length retreive the remaining payload (length (2
		// bytes) + type (1 byte))
		this.payload = Arrays.copyOfRange(this.payload, 3, this.payload.length);
		// retreive the ltv data from the first byte to length
		byte[] value = Arrays.copyOfRange(this.payload, 0, length);
		if (length != 0) {
			// after the LTV is retrevied discard the bytes from 0 to length
			this.payload = Arrays.copyOfRange(this.payload, length, payload.length);
			ltv.setValue(value);
			System.out.println(ltv);
		}

		return ltv;
	}

	/**
	 * retrevies the ltv from packet if the length is of integer
	 * 
	 * @return
	 */
	protected KeywestLTVPacket getLTVFromPackets() {

		KeywestLTVPacket ltv = new KeywestLTVPacket();

		int opLen = getIntValueFromPacket(this.payload, 0, 4);
		System.out.println("opLen" + opLen);
		System.out.println("opLen111" + Character.toString((char) opLen));
		// ltv.setLength(opLen);
		this.payload = Arrays.copyOfRange(this.payload, 4, this.payload.length);
		int opCode = getIntValueFromPacket(this.payload, 0, opLen);
		System.out.println("opCode" + opCode);
		if (opCode == 13) {
			System.out.println("op");
		}
		ltv.setType((byte) opCode);
		this.payload = Arrays.copyOfRange(this.payload, 4, this.payload.length);
		byte[] arr = Arrays.copyOfRange(this.payload, 0, opLen);
		System.out.println("arr" + arr.length);
		ltv.setValue(arr);
		this.payload = Arrays.copyOfRange(this.payload, opLen, payload.length);
		return ltv;
	}

	public DatagramPacket getPacket() {
		return packet;
	}

	private int getPacketLength(byte[] packet) {
		int i = (packet[0] << 24) & 0xff000000 | (packet[1] << 16) & 0x00ff0000 | (packet[2] << 8) & 0x0000ff00
				| (packet[3] << 0) & 0x000000ff;
		System.out.println("i" + i);
		return i;
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

	public String getIPAddressBytesFromLTVByType(short type) {
		KeywestLTVPacket ltv = getLTVPacketByType(type);
		byte[] bytes = ltv.getValue();
		StringBuffer buffer = new StringBuffer();
		if (bytes != null) {
			for (int i = 0; i < bytes.length; i++) {
				if (i != 0) {
					buffer.append(".");
				}
				buffer.append(ConversionUtil.unsignedByteToInt(bytes[i]));
			}
		}
		return buffer.toString();
	}

	public String getMacAddressFromLTV(short type) {
		String macAddress = "";
		byte[] arr = getValueFromLTVByType(type);
		if (arr != null) {
			macAddress = ConversionUtil.byteArray2MAC(arr);
		}
		return macAddress == null ? "" : macAddress.trim();
	}

	public KeywestLTVPacket getOpCodeLTVPacket() {
		return ltvPackets.get(0);
	}

	public List<KeywestLTVPacket> getLTVPacket() {
		return ltvPackets;
	}

	public void addLTVToPacket(KeywestLTVPacket ltv) {
		// length = length + ltv.getTotalLtvLength();
		ltvPackets.add(ltv);
	}

	public byte[] toByteArray() throws IOException {
		int totalLength = 0;
		if (ltvPackets != null && ltvPackets.size() > 0) {
			for (KeywestLTVPacket ltv : ltvPackets) {
				// stream.write(ltv.toByteArray());
				totalLength += ltv.getTotalLtvLength();
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

	protected int getIntValueInPacket(byte[] data, int startIdx, int endIdx) {
		if (data != null) {
			byte[] arr = Arrays.copyOfRange(data, startIdx, endIdx);
			return arr[0];
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Controller Packet : [ Header length :" + this.header.getLength() + ",");
		int i = 0;
		for (KeywestLTVPacket ltv : ltvPackets) {
			builder.append(ltv.toString());

		}
		builder.append("]");
		return builder.toString();
	}

}
