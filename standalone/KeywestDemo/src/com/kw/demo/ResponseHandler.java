package com.kw.demo;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KwpPacket;

public class ResponseHandler {

	public KwpPacket sendConfigResponse() {
		KwpPacket packet = new KwpPacket((byte)1,(byte)3,(byte)1);
		byte[] b = {0,0,0,0};
		packet.addLTVToPacket(new KeywestLTVPacket((byte)1, b));
		packet.addLTVToPacket(new KeywestLTVPacket((byte)2, "SifyWERTYU"));
		packet.addLTVToPacket(new KeywestLTVPacket((byte)3, "11AC".getBytes()));
		packet.addLTVToPacket(new KeywestLTVPacket((byte)4, "HT20".getBytes()));
		packet.addLTVToPacket(new KeywestLTVPacket((byte)5, "1".getBytes()));
		return packet;
	}
	
	public KwpPacket sendConfigRequest() {
		KwpPacket packet = new KwpPacket((byte)1,(byte)1,(byte)1);
		return packet;
	}
	
	public static void main(String[] args) {
		ResponseHandler handler = new ResponseHandler();
		System.out.println(handler.sendConfigResponse());
	}
}
