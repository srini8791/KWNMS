package com.hsq.kw.packet.vo;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KeywestPacket;
import com.hsq.kw.packet.util.ConversionUtil;

public class Configuration {

	private KeywestPacket packet;

	public static final byte ADDRESS_TYPE = 1;
	public static final byte SSID_TYPE = 3;
	public static final byte MODE_TYPE = 4;
	public static final byte BANDWIDTH_TYPE = 5;
	public static final byte CHANNEL_TYPE = 6;
	public static final byte IPADDRESS_TYPE = 2;
	public static final byte DEVICE_MODE = 7;
	public static final byte DEVICE_MAC = 8;
	public static final byte COUNTRY_CODE = 9;
	public static final byte GATEWAY_IP = 10;
	public static final byte NETMASK_IP = 11;
	public static final byte CUSTOMER_NAME = 12;
	public static final byte LINK_ID = 13;

	// Added changes before release

	public static final byte DDRS_STATUS = 14;
	public static final byte SPACIAL_STREAM = 15;
	public static final byte MODULATION_INDEX = 16;
	public static final byte MIN_MODULATION_INDEX = 17;
	public static final byte MAX_MODULATION_INDEX = 18;
	public static final byte ATPC_STATUS = 19;
	public static final byte TRANSMIT_POWER = 20;
	
	// new values added for handling DHCP
	public static final byte DYNAMIC_IPADDRESS = 21;
	public static final byte DYNAMIC_GATEWAY = 22;
	public static final byte DYNAMIC_NETMASK = 23;
	

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

	private int ddrsStatus = 0;
	private int spacialStream = 0;
	private int modulationIndex = 0;
	private int minModulationIndex = 0;
	private int maxModulationIndex = 0;
	private int atpcStatus = 0;
	private int tranmitPower = 0;

	private String dhcpAddress;
	private String dhcpGateway;
	private String dhcpMask;
	
	public int getOperationalMode() {
		return operationalMode;
	}

	public void setOperationalMode(int operationalMode) {
		this.operationalMode = operationalMode;
	}

	public String getDhcpAddress() {
		return dhcpAddress;
	}

	public void setDhcpAddress(String dhcpAddress) {
		this.dhcpAddress = dhcpAddress;
	}

	public String getDhcpGateway() {
		return dhcpGateway;
	}

	public void setDhcpGateway(String dhcpGateway) {
		this.dhcpGateway = dhcpGateway;
	}

	public String getDhcpMask() {
		return dhcpMask;
	}

	public void setDhcpMask(String dhcpMask) {
		this.dhcpMask = dhcpMask;
	}

	/**
	 * Generates a new Configuration from UI
	 */
	public Configuration() {
		this.packet = new KeywestPacket((byte) 1, (byte) 1, (byte) 1);
	}

	/**
	 * Generates/ Updates the configuration in UI received from the packet.
	 * 
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

		ltv = packet.getLTVPacketByType(DDRS_STATUS);
		if (ltv != null) {
			this.ddrsStatus = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(SPACIAL_STREAM);
		if (ltv != null) {
			this.spacialStream = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(MODULATION_INDEX);
		if (ltv != null) {
			this.modulationIndex = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(MIN_MODULATION_INDEX);
		if (ltv != null) {
			this.minModulationIndex = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(MAX_MODULATION_INDEX);
		if (ltv != null) {
			this.maxModulationIndex = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(ATPC_STATUS);
		if (ltv != null) {
			this.atpcStatus = ltv.getUnsignedToInt();
		}
		ltv = packet.getLTVPacketByType(TRANSMIT_POWER);
		if (ltv != null) {
			this.tranmitPower = ltv.getUnsignedToInt();
		}
		
		this.dhcpAddress = packet.getIPAddressBytesFromLTVByType(DYNAMIC_IPADDRESS);
		this.dhcpGateway = packet.getIPAddressBytesFromLTVByType(DYNAMIC_GATEWAY);
		this.dhcpMask = packet.getIPAddressBytesFromLTVByType(DYNAMIC_NETMASK);
		
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

	public int getDdrsStatus() {
		return this.ddrsStatus;
	}

	public void setDdrsStatus(int ddrsStatus) {
		this.ddrsStatus = ddrsStatus;
	}

	public int getSpacialStream() {
		return this.spacialStream;
	}

	public void setSpacialStream(int spacialStream) {
		this.spacialStream = spacialStream;
	}

	public int getModulationIndex() {
		return this.modulationIndex;
	}

	public void setModulationIndex(int modulationIndex) {
		this.modulationIndex = modulationIndex;
	}

	public int getMinModulationIndex() {
		return this.minModulationIndex;
	}

	public void setMinModulationIndex(int minModulationIndex) {
		this.minModulationIndex = minModulationIndex;
	}

	public int getMaxModulationIndex() {
		return this.maxModulationIndex;
	}

	public void setMaxModulationIndex(int maxModulationIndex) {
		this.maxModulationIndex = maxModulationIndex;
	}

	public int getAtpcStatus() {
		return this.atpcStatus;
	}

	public void setAtpcStatus(int atpcStatus) {
		this.atpcStatus = atpcStatus;
	}

	public int getTranmitPower() {
		return this.tranmitPower;
	}

	public void setTranmitPower(int tranmitPower) {
		this.tranmitPower = tranmitPower;
	}

	public KeywestPacket buildPacketFromUI() {
		KeywestPacket setPacket = new KeywestPacket((byte) 1, (byte) 3, (byte) 1);
		setPacket.addLTVToPacket(new KeywestLTVPacket(SSID_TYPE, this.ssid));
		setPacket.addLTVToPacket(new KeywestLTVPacket(MODE_TYPE, ConversionUtil.intToSingleByte(this.operationalMode)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(BANDWIDTH_TYPE, ConversionUtil.intToSingleByte(this.channelBW)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(CHANNEL_TYPE, ConversionUtil.intToSingleByte(this.channel)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(ADDRESS_TYPE, ConversionUtil.intToShortBytes(this.ipAddrType)));
		setPacket.addLTVToPacket(
				new KeywestLTVPacket(IPADDRESS_TYPE, ConversionUtil.convertIPAddressToBytes(ipAddress)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(DEVICE_MODE, ConversionUtil.intToSingleByte(this.deviceMode)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(COUNTRY_CODE, ConversionUtil.intToShortBytes(this.countryCode)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(GATEWAY_IP, ConversionUtil.convertIPAddressToBytes(gatewayIp)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(NETMASK_IP, ConversionUtil.convertIPAddressToBytes(netMask)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(CUSTOMER_NAME, this.custName));
		setPacket.addLTVToPacket(new KeywestLTVPacket(LINK_ID, ConversionUtil.intToSingleByte(this.linkId)));
		setPacket.addLTVToPacket(new KeywestLTVPacket( DDRS_STATUS, ConversionUtil.intToSingleByte(this.ddrsStatus)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(SPACIAL_STREAM, ConversionUtil.intToSingleByte(this.spacialStream)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(MODULATION_INDEX, ConversionUtil.intToSingleByte(this.modulationIndex)));
		setPacket.addLTVToPacket(
				new KeywestLTVPacket(MIN_MODULATION_INDEX, ConversionUtil.intToSingleByte(this.minModulationIndex)));
		setPacket.addLTVToPacket(
				new KeywestLTVPacket(MAX_MODULATION_INDEX, ConversionUtil.intToSingleByte(this.maxModulationIndex)));
		setPacket.addLTVToPacket(new KeywestLTVPacket((byte) ATPC_STATUS, ConversionUtil.intToSingleByte(this.atpcStatus)));
		setPacket.addLTVToPacket(new KeywestLTVPacket((byte) TRANSMIT_POWER, ConversionUtil.intToSingleByte(this.tranmitPower)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(DYNAMIC_IPADDRESS, ConversionUtil.convertIPAddressToBytes(this.dhcpAddress)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(DYNAMIC_GATEWAY, ConversionUtil.convertIPAddressToBytes(this.dhcpGateway)));
		setPacket.addLTVToPacket(new KeywestLTVPacket(DYNAMIC_NETMASK, ConversionUtil.convertIPAddressToBytes(this.dhcpMask)));
		return setPacket;
	}

	public KeywestPacket getPacket() {
		return this.packet;
	}

	@Override
	public String toString() {
		return "Configuration-->[ SSID:" + this.ssid + " OPMode:" + this.operationalMode + " Bandwidth:"
				+ this.channelBW + " Channel:" + this.channel + " Address Type:" + this.ipAddrType + " Address:"
				+ this.ipAddress + " Device Mode:" + this.deviceMode + " Country Code:" + this.countryCode
				+ "GateWayIp:" + gatewayIp + "]";
	}

}
