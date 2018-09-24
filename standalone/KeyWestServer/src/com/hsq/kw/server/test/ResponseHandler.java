package com.hsq.kw.server.test;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KwpPacket;
import com.hsq.kw.packet.util.ConversionUtil;
import com.hsq.kw.packet.util.KeywestConstants;
import com.hsq.kw.packet.vo.Configuration;
import com.hsq.kw.packet.vo.KwpInventory;
import com.hsq.kw.packet.vo.KwpSysInfo;

public class ResponseHandler {

	public KwpPacket sendConfigResponse() {
		KwpPacket packet = new KwpPacket((byte)1,(byte)KeywestConstants.CONFIG_GET_RESPONSE,(byte)1);
		byte[] b = {0,0,0,0};
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.IPADDRESS_TYPE, b));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.SSID_TYPE, "SifyWERTYU"));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.MODE_TYPE, "11AC".getBytes()));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.BANDWIDTH_TYPE, "HT20".getBytes()));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.CHANNEL_TYPE, ConversionUtil.shortToSingleByte((short)1)));
		return packet;
	}
	
	public KwpPacket sendSysinfoResponse() {
		KwpPacket packet = new KwpPacket((byte)1,(byte)KeywestConstants.CONFIG_GET_RESPONSE,(byte)2);
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSID_TYPE, "1.2.1.4.22.1"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSNAME_TYPE, "Test device"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSCONTACT_TYPE, "contact"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSDESCRIPTION_TYPE, "description device 1"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.GEOLATITUDE_TYPE, "geo latitude"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.GEOLONGITUDE_MODE, "geo logitude"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSNAME_TYPE, "Hyderabad"));
		return packet;
	}
	
	public KwpPacket sendInventoryResponse() {
		KwpPacket packet = new KwpPacket((byte)1,(byte)KeywestConstants.CONFIG_GET_RESPONSE,(byte)3);
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.BANDWIDTH_LIMIT_TYPE, "40"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.ETH_SPEED_TYPE, "100"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.FIRMWARE_VERSION_TYPE, "1.1 version"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.MODEL_NUMBER_TYPE, "Model number"));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.SERIAL_NUMBER_TYPE, "Serial Number"));
		return packet;
		
	}
	
	public KwpPacket sendKeywestLinkData() {
		return null;
	}
	
	public KwpPacket sendSetConfigResponse() {
		KwpPacket packet = new KwpPacket((byte)1,(byte)1,(byte)1);
		byte[] b = {1};
		packet.addLTVToPacket(new KeywestLTVPacket((byte)1, b));
		return packet;
	}
	
	public static void main(String[] args) {
		ResponseHandler handler = new ResponseHandler();
		System.out.println(handler.sendConfigResponse());
	}
}
