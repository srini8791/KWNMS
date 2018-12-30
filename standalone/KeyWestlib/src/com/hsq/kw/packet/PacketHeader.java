package com.hsq.kw.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.hsq.kw.packet.util.ConversionUtil;

/**
 * Header data in the Keywest packet
 * Size of the total header is 6 bytes
 *
 * unique to identify - 1 byte 
 * type of interface - 1 byte 
 * size of buffer - 2 bytes
 * Get / Set  & Request/Response - 1 byte 
 * structure or text_file - 1 byte 
 * @author Spittala
 *
 */

public class PacketHeader {
	/**
	 * Unique id with possible values 1 & 2. 1 for Sify
	 */
	private byte id;
	/**
	 * Interface type 1- App, 2- NMS
	 */
	private byte interfaceType;
	
	/**
	 * total length in bytes of LTVs available in the packet
	 */
	private byte[] length;
	/**
	 * type of the request
	 */
	private byte type;
	/**
	 * sub type which defines the type of data which is parsed
	 */
	private byte subType;
	
	
	/**
	 * indicates if single object will be available in packet or multiple objects
	 * 1 - multiple objects
	 * 0 - single objects
	 */
	private byte ptmp;
	
	/**
	 * indicates whether this is the last packet
	 */
	private byte more;
	
	/**
	 * Constructs the packet header from the header byte array
	 * @throws Exception 
	 * 
	 */
	public PacketHeader(byte[] headerData) throws Exception {
		if (headerData == null || headerData.length != KeywestPacket.SIZE_OF_HEADER) {
			throw new Exception("Invalid header");
		}
		id = headerData[0];
		interfaceType = headerData[1];
		length = Arrays.copyOfRange(headerData, 2, 4);//UtilityTest.bytesToShort(Arrays.copyOfRange(headerData, 2, 4));
		type = headerData[4];
		subType = headerData[5];
		ptmp=headerData[6];
		more = headerData[7];
		
	}
	
	/**
	 * builds a new header 
	 * @param id unique id 
	 * @param inType  intervace tkype
	 * @param rTpe request type set/get
	 * @param subType sub type of the request
	 */
	public PacketHeader(byte id,byte inType, byte rTpe, byte subType) {
		this.id = id;
		interfaceType = inType;
		type = rTpe;
		this.subType = subType;
		this.ptmp = 0;
		this.more = 0;
		
	}
	
	/**
	 * set the total length of the LTVs in the packet
	 * @param length
	 */
	public void setLength(short length) {
		this.length = ConversionUtil.shortToBytes(length);
	}
	
	public short getLength() {
		return ConversionUtil.bytesToShort(this.length);
	}
	
	/**
	 * returns the header in bytes
	 * @return
	 * @throws IOException
	 */
	public byte[] getHeader() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(id);
		stream.write(interfaceType);
		stream.write(this.length);
		stream.write(type);
		stream.write(subType);
		stream.write(ptmp);
		stream.write(more);
		return stream.toByteArray();
	}
	
	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public byte getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(byte interfaceType) {
		this.interfaceType = interfaceType;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getSubType() {
		return subType;
	}

	public void setSubType(byte subType) {
		this.subType = subType;
	}

	/**
	 * set the length of the LTVs in bytes. the length of the LTVs should be converted from
	 * short to byte array
	 * @param length
	 */
	public void setLength(byte[] length) {
		this.length = length;
	}

	public String toString() {
		return "Header Data : ID : " + id + " Length: " + getLength();
	}
	
}