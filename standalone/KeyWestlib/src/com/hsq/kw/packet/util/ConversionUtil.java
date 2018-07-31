/**
 * 
 */
package com.hsq.kw.packet.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Spittala
 *
 */
public class ConversionUtil {

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
		int i = (packet[0] << 24) & 0xff000000 | (packet[1] << 16) & 0x00ff0000
				| (packet[2] << 8) & 0x0000ff00 | (packet[3] << 0) & 0x000000ff;
		return i;
	}

	/**
	 * Converts bytes to short
	 * 
	 * @param bytes
	 * @return
	 */
	public static short bytesToShort(byte[] bytes) {
		short s = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
				.getShort();
		return s;
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
	
	public static byte[] shortToSingleByte(short value) {
		byte[] b = {(byte) value};
		return b;
	}
	
	public static byte[] intToSingleByte(int value) {
		byte[] b = {(byte) value};
		return b;
	}

	public static String byteArray2MAC(byte[] arr) {
		StringBuilder sb = new StringBuilder();
		for (byte b : arr) {
			if (sb.length() > 0)
				sb.append(':');
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
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
		byte[] b = {0,0,0,0};
		try {
			add = InetAddress.getByName(address);
			b = add.getAddress();
		} catch (UnknownHostException e) {
		}
		return b;
	}
}
