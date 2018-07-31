package com.kw.demo;

import com.hsq.kw.packet.KeywestPacket;

public class ConfigurationUI {
	private KeywestPacket packet = null;
	
	private String ssid = null;
	private String mod = null;
	private String bandwidth = null;
	private String channel = null;
	
	private enum bandwith {
		
		
	}
	
	
	public ConfigurationUI(KeywestPacket packet) {
		this.packet = packet;
		ssid = this.packet.getStringValueFromLTVByType(2);
		mod = this.packet.getStringValueFromLTVByType(3);
		bandwidth = this.packet.getStringValueFromLTVByType(4);
		channel = this.packet.getStringValueFromLTVByType(5);
		
		
	}
	
	public ConfigurationUI() {
		this.packet = new KeywestPacket();
	}
	
	public String getSSID() {
		return this.ssid;
	}
	
	public String getMode() {
		return mod;
	}
	
	public String getChannel() {
		return channel;
	}
	
}
