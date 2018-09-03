package com.hsq.kw.packet.vo;

import java.util.List;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KeywestPacket;
import com.hsq.kw.packet.util.ConversionUtil;

public class Configuration {
	
	private KeywestPacket packet;
	
	
	public static final byte ADDRESS_TYPE = 2;
	public static final byte SSID_TYPE = 3;
	public static final byte MODE_TYPE = 4;
	public static final byte BANDWIDTH_TYPE = 5;
	public static final byte CHANNEL_TYPE = 6;
	public static final byte IPADDRESS_TYPE = 1;
	public static final byte DEVICE_MODE = 7;
	public static final byte DEVICE_MAC = 8;
	public static final byte COUNTRY_CODE = 9;
	public static final byte GATEWAY_IP = 10;
	public static final byte NETMASK_IP = 11;
	public static final byte CUSTOMER_NAME = 12;
	public static final byte LINK_ID = 13;
	
	
	
	private String ssid = null;
	
	private int channelBW = 0;
	
	private int operationalMode = 0;
	
	private int channel = 0;
	
	private int ipAddrType = 0;
	
	private String ipAddress = null;
	
	private int deviceMode = 0;
	
	private String deviceMac = "";
	
	private int countryCode = 0;
	
	private String gatewayIp = "";
	
	private String netMask = "";
	
	private String custName = "";
	
	private int linkId = -1;
	
	
	
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
		KeywestLTVPacket ltv = packet.getLTVPacketByType(ADDRESS_TYPE);
		if (ltv != null) {
			this.ipAddrType = ltv.getUnsignedToInt();
		}
		this.ssid = packet.getStringValueFromLTVByType(SSID_TYPE);
		ltv = packet.getLTVPacketByType(BANDWIDTH_TYPE);
		if (ltv != null) {
			this.channelBW = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(MODE_TYPE);
		if (ltv != null) {
			this.operationalMode = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(CHANNEL_TYPE);
		if (ltv != null) {
			this.channel = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(DEVICE_MODE);
		if (ltv != null) {
			this.deviceMode = ltv.getUnsignedToInt();
		}
		this.setDeviceMac(packet.getMacAddressFromLTV(DEVICE_MAC));
		ltv = packet.getLTVPacketByType(COUNTRY_CODE);
		if (ltv != null) {
			this.countryCode = ltv.getShortIntValue();
		}
		this.gatewayIp = packet.getIPAddressBytesFromLTVByType(GATEWAY_IP);
		this.netMask = packet.getIPAddressBytesFromLTVByType(NETMASK_IP);
		this.custName = packet.getStringValueFromLTVByType(CUSTOMER_NAME);
		ltv = packet.getLTVPacketByType(LINK_ID);
		if (ltv != null) {
			this.linkId = ltv.getUnsignedToInt();
		}
		
		
	}


	public String getSsid() {
		return ssid;
	}


	public void setSsid(String ssid) {
		this.ssid = ssid;
	}


	public int getChannelBW() {
		return channelBW;
	}


	public void setChannelBW(int channelBW) {
		this.channelBW = channelBW;
	}


	public int getMode() {
		return operationalMode;
	}


	public void setMode(int mode) {
		this.operationalMode = mode;
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
	
	public int getDeviceMode() {
		return deviceMode;
	}


	public void setDeviceMode(int deviceMode) {
		this.deviceMode = deviceMode;
	}
	
	public String getDeviceMac() {
		return deviceMac;
	}


	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}


	public int getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}


	public String getGatewayIp() {
		return gatewayIp;
	}


	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}


	public String getNetMask() {
		return netMask;
	}


	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}


	public String getCustName() {
		return custName;
	}


	public void setCustName(String custName) {
		this.custName = custName;
	}


	public int getLinkId() {
		return linkId;
	}


	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}


	public KeywestPacket buildPacketFromUI() {
		KeywestPacket setPacket = new KeywestPacket((byte)1,(byte)3,(byte)1);
		setPacket.addLTVToPacket(new KeywestLTVPacket(SSID_TYPE, this.ssid));
		setPacket.addLTVToPacket(new KeywestLTVPacket(MODE_TYPE, ConversionUtil.intToSingleByte(this.operationalMode)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(BANDWIDTH_TYPE, ConversionUtil.intToSingleByte(this.channelBW)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(CHANNEL_TYPE, ConversionUtil.intToSingleByte(this.channel)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(ADDRESS_TYPE, ConversionUtil.intToShortBytes(this.ipAddrType)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(IPADDRESS_TYPE, ConversionUtil.convertIPAddressToBytes(ipAddress)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(DEVICE_MODE, ConversionUtil.intToSingleByte(this.deviceMode)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(COUNTRY_CODE, ConversionUtil.intToShortBytes(this.countryCode)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(GATEWAY_IP, ConversionUtil.convertIPAddressToBytes(gatewayIp)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(NETMASK_IP, ConversionUtil.convertIPAddressToBytes(netMask)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(CUSTOMER_NAME, this.custName));
		setPacket.addLTVToPacket(new KeywestLTVPacket(LINK_ID, ConversionUtil.intToSingleByte(this.linkId)));
		return setPacket;
	}
	
	public KeywestPacket getPacket() {
		return this.packet;
	}
	
	@Override
	public String toString() {
		return "Configuration-->[ SSID:" + this.ssid + " OPMode:" + this.operationalMode + " Bandwidth:" + this.channelBW + " Channel:" + this.channel + " Address Type:" + this.ipAddrType + " Address:" + this.ipAddress + " Device Mode:" +this.deviceMode + " Country Code:" + this.countryCode + "GateWayIp:"+ gatewayIp +"]";
	}

}
