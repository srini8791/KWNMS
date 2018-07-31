package com.hsq.kw.packet.vo;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KeywestPacket;
import com.hsq.kw.packet.util.ConversionUtil;

public class Configuration {
	
	private KeywestPacket packet;
	
	
	public static final byte ADDRESS_TYPE = 2;
	public static final byte SSID_TYPE = 3;
	public static final byte MODE_TYPE = 4;
	public static final byte BANDWIDTY_TYPE = 5;
	public static final byte CHANNEL_TYPE = 6;
	public static final byte IPADDRESS_TYPE = 1;
	
	
	
	private String ssid = null;
	
	private String channelBW = null;
	
	private String mode = null;
	
	private int channel = 0;
	
	private int ipAddrType = 0;
	
	private String ipAddress = null;
	
	
	
	/**
	 * Generates a new Configuration from UI
	 */
	public Configuration() {
		this.packet = new KeywestPacket((byte)1,(byte)1,(byte)1);
	}
	
	
	/**
	 * Generates/ Updates the configuration in UI received from the packet.
	 * @param packet
	 */
	public Configuration(KeywestPacket packet) {
		this.packet = packet;
		this.ipAddress = packet.getIPAddressBytesFromLTVByType(IPADDRESS_TYPE);
		this.ssid = packet.getStringValueFromLTVByType(SSID_TYPE);
		this.channelBW = packet.getStringValueFromLTVByType(BANDWIDTY_TYPE);
		this.mode = packet.getStringValueFromLTVByType(MODE_TYPE);
		//this.channel = packet.get
		
	}


	public String getSsid() {
		return ssid;
	}


	public void setSsid(String ssid) {
		this.ssid = ssid;
	}


	public String getChannelBW() {
		return channelBW;
	}


	public void setChannelBW(String channelBW) {
		this.channelBW = channelBW;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public int getChannel() {
		return channel;
	}


	public void setChannel(int channel) {
		this.channel = channel;
	}


	public int getIpAddrType() {
		return ipAddrType;
	}


	public void setIpAddrType(int ipAddrType) {
		this.ipAddrType = ipAddrType;
	}


	public String getIpAddress() {
		return ipAddress;
	}


	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public KeywestPacket buildPacketFromUI() {
		this.packet.addLTVToPacket(new KeywestLTVPacket(SSID_TYPE, this.ssid));
		this.packet.addLTVToPacket(new KeywestLTVPacket(MODE_TYPE, this.mode.getBytes()));
		this.packet.addLTVToPacket(new KeywestLTVPacket(BANDWIDTY_TYPE, this.channelBW.getBytes()));
		this.packet.addLTVToPacket(new KeywestLTVPacket(CHANNEL_TYPE, (this.channel + "").getBytes()));
		this.packet.addLTVToPacket(new KeywestLTVPacket(ADDRESS_TYPE, ConversionUtil.convertIPAddressToBytes(ipAddress)));
		return null;
	}
	
	public KeywestPacket getPacket() {
		return this.packet;
	}

}
