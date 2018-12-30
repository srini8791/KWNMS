package com.hsq.kw.client;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KeywestPacket;
import com.hsq.kw.packet.util.ConversionUtil;

public class ResponseHandler {

	public KeywestPacket sendConfigResponse() {
		KeywestPacket packet = new KeywestPacket((byte)1,(byte)3,(byte)1);
		byte[] b = {0,0,0,0};
		packet.addLTVToPacket(new KeywestLTVPacket((byte)1, b));
		packet.addLTVToPacket(new KeywestLTVPacket((byte)2, "SifyWERTYU"));
		packet.addLTVToPacket(new KeywestLTVPacket((byte)3, "11AC".getBytes()));
		packet.addLTVToPacket(new KeywestLTVPacket((byte)4, "HT20".getBytes()));
		packet.addLTVToPacket(new KeywestLTVPacket((byte)5, "1".getBytes()));
		return packet;
	}
	
	public KeywestPacket sendConfigRequest() {
		KeywestPacket packet = new KeywestPacket((byte)1,(byte)1,(byte)1);
		return packet;
	}
	
	public KeywestPacket getWirelessStats() {
		KeywestPacket packet = new KeywestPacket((byte)1,(byte)1,(byte)2);
		return packet;
	}
	
	public static void main(String[] args) {
		/*ResponseHandler handler = new ResponseHandler();
		System.out.println(handler.sendConfigResponse());*/
		
		byte[] bytes = {-64,-88,1,52};
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if (i != 0) {
				buffer.append(".");
			}
			buffer.append(ConversionUtil.unsignedByteToInt(bytes[i]));
		}
		System.out.println(buffer.toString());
		bytes = null;
		bytes = ConversionUtil.convertIPAddressToBytes(buffer.toString());
		System.out.println(bytes);
	}
}
