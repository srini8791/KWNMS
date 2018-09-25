package com.hsq.kw.server.test;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KwpPacket;
import com.hsq.kw.packet.util.ConversionUtil;
import com.hsq.kw.packet.util.KeywestConstants;
import com.hsq.kw.packet.vo.Configuration;
import com.hsq.kw.packet.vo.KwpInventory;
import com.hsq.kw.packet.vo.KwpSysInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ResponseHandler {

	private static Properties properties = new Properties();

	static {
		try {
			System.out.println("props file path: " + new File("node.props").getAbsolutePath());
			properties.load(new InputStreamReader(new FileInputStream("node.props")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public KwpPacket sendConfigResponse() {
		String ip = properties.getProperty("config.ip");
		String ssid = properties.getProperty("config.ssid");
		int mode = Integer.parseInt(properties.getProperty("config.mode"));
		int bandwidth = Integer.parseInt(properties.getProperty("config.bandwidth"));
		int devicemode = Integer.parseInt(properties.getProperty("config.devicemode"));
		short channel = Short.parseShort(properties.getProperty("config.channel"));
		String macaddress = properties.getProperty("config.macaddress");

		KwpPacket packet = new KwpPacket((byte)1,(byte)KeywestConstants.CONFIG_GET_RESPONSE,(byte)1);
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.IPADDRESS_TYPE, ConversionUtil.convertIPAddressToBytes(ip)));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.SSID_TYPE, ssid));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.MODE_TYPE, ConversionUtil.intToShortBytes(mode)));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.BANDWIDTH_TYPE, ConversionUtil.intToShortBytes(bandwidth)));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.DEVICE_MODE,ConversionUtil.intToShortBytes(devicemode)));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.CHANNEL_TYPE, ConversionUtil.shortToSingleByte((short)channel)));
		packet.addLTVToPacket(new KeywestLTVPacket(Configuration.DEVICE_MAC, ConversionUtil.convertMACToBytes(macaddress)));
		return packet;
	}
	
	public KwpPacket sendSysinfoResponse() {
		String sysid = properties.getProperty("sysinfo.sysid");
		String sysname = properties.getProperty("sysinfo.sysname");
		String syscontact = properties.getProperty("sysinfo.syscontact");
		String sysdesc = properties.getProperty("sysinfo.sysdesc");
		String geolat = properties.getProperty("sysinfo.geolat");
		String geolong = properties.getProperty("sysinfo.geolong");

		KwpPacket packet = new KwpPacket((byte)1,(byte)KeywestConstants.CONFIG_GET_RESPONSE,(byte)2);
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSID_TYPE, sysid));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSNAME_TYPE, sysname));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSCONTACT_TYPE, syscontact));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.SYSDESCRIPTION_TYPE, sysdesc));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.GEOLATITUDE_TYPE, geolat));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpSysInfo.GEOLONGITUDE_MODE, geolong));
		return packet;
	}
	
	public KwpPacket sendInventoryResponse() {
		String bandwidthlimit = properties.getProperty("inventory.bandwidthlimit");
		String ethspeed = properties.getProperty("inventory.ethspeed");
		String firmware = properties.getProperty("inventory.firmware");
		String modelnumber = properties.getProperty("inventory.modelnumber");
		String serialnumber = properties.getProperty("inventory.serialnumber");

		KwpPacket packet = new KwpPacket((byte)1,(byte)KeywestConstants.CONFIG_GET_RESPONSE,(byte)3);
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.BANDWIDTH_LIMIT_TYPE, bandwidthlimit));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.ETH_SPEED_TYPE, ethspeed));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.FIRMWARE_VERSION_TYPE, firmware));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.MODEL_NUMBER_TYPE, modelnumber));
		packet.addLTVToPacket(new KeywestLTVPacket(KwpInventory.SERIAL_NUMBER_TYPE, serialnumber));
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
