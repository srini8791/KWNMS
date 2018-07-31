package com.hsq.kw.server.test;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KeywestPacket;
import com.hsq.kw.packet.util.ConversionUtil;
import com.hsq.kw.packet.util.KeywestConstants;
import com.hsq.kw.packet.vo.Configuration;

public class ResponseHandler {

	public KeywestPacket sendConfigResponse() {
		KeywestPacket packet = new KeywestPacket((byte)KeywestConstants.CONFIG_GET_RESPONSE,(byte)2,(byte)1);
		byte[] b = {0,0,0,0};
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.IPADDRESS_TYPE, b));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.SSID_TYPE, "SifyWERTYU"));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.MODE_TYPE, "11AC".getBytes()));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.BANDWIDTY_TYPE, "HT20".getBytes()));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.CHANNEL_TYPE, ConversionUtil.shortToSingleByte((short)1)));
		return packet;
	}
	
	public KeywestPacket sendKeywestLinkData() {
		return null;
	}
	
	public KeywestPacket sendSetConfigResponse() {
		KeywestPacket packet = new KeywestPacket((byte)1,(byte)1,(byte)1);
		byte[] b = {1};
		packet.addLTVToPacket(new KeywestLTVPacket((byte)1, b));
		return packet;
	}
	
	public static void main(String[] args) {
		ResponseHandler handler = new ResponseHandler();
		System.out.println(handler.sendConfigResponse());
	}
}
