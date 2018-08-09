package com.hsq.kw.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.hsq.kw.packet.util.ConversionUtil;



public class KeywestLTVPacket {
	
	private short length;
	private byte type;
	private byte[] value;
	
	public KeywestLTVPacket(byte type, byte[] value) {
		this.type = type;
		this.value = value;
		this.length = (short) value.length;
	}
	
	public KeywestLTVPacket(byte type, String value) {
		this(type,value.getBytes());
	}
	
	public KeywestLTVPacket(byte type) {
		this.type = type;
	}
	
	public KeywestLTVPacket() {
	}
	
	public short getLength() {
		return length;
	}
	public void setLength(short length) {
		this.length = length;
	}
	public int getType() {
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
	
	public int getLTVValue() {
		return ConversionUtil.byteToInt(value);
	}
	
	public byte[] toByteArray() throws IOException {
		byte[] len = ConversionUtil.shortToBytes(this.length);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(len);
		stream.write(this.type);
		if (value != null) {
			stream.write(value);
		}
		return stream.toByteArray();
	}
	
	public int getTotalLtvLength() {
		return 2+1+value.length;
	}
	
		
	public int getIntValue() {
		return ConversionUtil.byteToInt(value);
	}
	
	public short getShortValue() {
		return ConversionUtil.bytesToShort(value);
	}
	
	public int getShortIntValue() {
		return ConversionUtil.bytesToShort(value);
	}
	
	public int getUnsignedToInt() {
		if (value != null && value.length == 1) {
			return ConversionUtil.unsignedByteToInt(value[0]);
		} else {
			return getShortIntValue();
		}
		
	}
	
	
	public String getStringValue() {
		if (value != null) {
			return new String(value);
		}
		return "";
	}
	
	public String getStringUTF8Value() throws UnsupportedEncodingException {
		if (value != null) {
			return new String(value,"UTF-8");
		}
		return "";
	}
	
	/*public String toIntString() {
		return "LTV=[" + "Length=" +length + ", Type=" + type + ",Value=" + ProximUtil.byteToInt(value) +"]";
	}*/
	
	@Override
	public String toString() {
		
		/*if (value.length == 4) {
			return "LTV=[" + "Length=" +getLength() + ", Type=" + type + ",Value=" + ConversionUtil.byteToInt(value) +"]";
		}*/
		return "LTV=[" + "Length=" +length + ", Type=" + type + ",Value=" + getStringValue() +"]";
	}

}
