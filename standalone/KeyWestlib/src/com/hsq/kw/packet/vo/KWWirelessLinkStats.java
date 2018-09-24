package com.hsq.kw.packet.vo;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KwpPacket;

public class KWWirelessLinkStats {
	
	private static final byte NO_OF_LINKS_TYPE = 1;
	private static final byte LINK_ID_TYPE = 2;
	private static final byte WLAN_MAC_TYPE = 3;
	private static final byte REMOTE_IP_TYPE = 4;
	private static final byte REMOTE_LAT_TYPE = 5;
	private static final byte REMOTE_LONG_TYPE = 6;
	private static final byte LOCAL_LAT_TYPE = 7;
	private static final byte LOCAL_LONG_TYPE = 8;
	private static final byte TX_INPUT_TYPE = 9;
	private static final byte RX_INPUT_TYPE = 10;
	private static final byte LOCAL_SNR_A1_TYPE = 11;
	private static final byte LOCAL_SNR_A2_TYPE = 12;
	private static final byte REMOTE_SNR_A1_TYPE = 13;
	private static final byte REMOTE_SNR_A2_TYPE = 14;
	private static final byte LOCAL_RATE = 15;
	private static final byte REMOTE_RATE = 16;
	
	private int noOfLinks;
	private int linkId;
	private String macAddress;
	private String remoteIP;
	private String remoteLat;
	private String remoteLong;
	private String localLat;
	private String localLong;
	private int txInput;
	private int rxInput;
	private int localSNRA1;
	private int localSNRA2;
	private int remoteSNRA1;
	private int remoteSNRA2;
	private int localRate;
	private int remoteRate;
	
	
	private KwpPacket packet;
	
	public KWWirelessLinkStats() {
	}
	
	
	public KWWirelessLinkStats(KwpPacket packet) {
		this.packet = packet;
		if (packet.getLTVPacket() != null && packet.getLTVPacket().size() > 1) {
			KeywestLTVPacket ltv = this.packet.getLTVPacketByType((byte)NO_OF_LINKS_TYPE);
			if (ltv != null) {
				this.noOfLinks = this.packet.getLTVPacketByType((byte)NO_OF_LINKS_TYPE).getUnsignedToInt();
			}
			ltv = this.packet.getLTVPacketByType((byte)LINK_ID_TYPE);
			if (ltv != null) {
				this.linkId = this.packet.getLTVPacketByType((byte)LINK_ID_TYPE).getUnsignedToInt();
			}
			
			this.macAddress = this.packet.getMacAddressFromLTV(WLAN_MAC_TYPE);
			this.remoteIP = this.packet.getIPAddressBytesFromLTVByType(REMOTE_IP_TYPE);
			this.remoteLat = this.packet.getStringValueFromLTVByType(REMOTE_LAT_TYPE);
			this.remoteLong = this.packet.getStringValueFromLTVByType(REMOTE_LONG_TYPE);
			this.localLat = this.packet.getStringValueFromLTVByType(LOCAL_LAT_TYPE);
			this.localLong = this.packet.getStringValueFromLTVByType(LOCAL_LONG_TYPE);
			ltv = this.packet.getLTVPacketByType((byte)TX_INPUT_TYPE);
			if (ltv != null) {
				this.txInput = this.packet.getLTVPacketByType(TX_INPUT_TYPE).getShortIntValue();
			}
			ltv = this.packet.getLTVPacketByType((byte)RX_INPUT_TYPE);
			if (ltv != null) {
				this.rxInput = this.packet.getLTVPacketByType(RX_INPUT_TYPE).getShortIntValue();
			}
			
			ltv = this.packet.getLTVPacketByType((byte)LOCAL_SNR_A1_TYPE);
			if (ltv != null) {
				this.localSNRA1 = this.packet.getLTVPacketByType(LOCAL_SNR_A1_TYPE).getUnsignedToInt();
			}
			
			ltv = this.packet.getLTVPacketByType((byte)LOCAL_SNR_A2_TYPE);
			if (ltv != null) {
				this.localSNRA2 = this.packet.getLTVPacketByType(LOCAL_SNR_A2_TYPE).getUnsignedToInt();
			}
			ltv = this.packet.getLTVPacketByType((byte)REMOTE_SNR_A1_TYPE);
			if (ltv != null) {
				this.remoteSNRA1 = this.packet.getLTVPacketByType(REMOTE_SNR_A1_TYPE).getUnsignedToInt();
			}
			
			ltv = this.packet.getLTVPacketByType((byte)REMOTE_SNR_A2_TYPE);
			if (ltv != null) {
				this.remoteSNRA2 = this.packet.getLTVPacketByType(REMOTE_SNR_A2_TYPE).getUnsignedToInt();
			}
			
			ltv = this.packet.getLTVPacketByType((byte)LOCAL_RATE);
			if (ltv != null) {
				this.localRate = this.packet.getLTVPacketByType(LOCAL_RATE).getShortIntValue();
			}
			ltv = this.packet.getLTVPacketByType((byte)REMOTE_RATE);
			if (ltv != null) {
				this.remoteRate = this.packet.getLTVPacketByType(REMOTE_RATE).getShortIntValue();
			}
						
		}
		
	}

	public int getNoOfLinks() {
		return noOfLinks;
	}

	public void setNoOfLinks(int noOfLinks) {
		this.noOfLinks = noOfLinks;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public String getRemoteLat() {
		return remoteLat;
	}

	public void setRemoteLat(String remoteLat) {
		this.remoteLat = remoteLat;
	}

	public String getRemoteLong() {
		return remoteLong;
	}

	public void setRemoteLong(String remoteLong) {
		this.remoteLong = remoteLong;
	}

	public String getLocalLat() {
		return localLat;
	}

	public void setLocalLat(String localLat) {
		this.localLat = localLat;
	}

	public String getLocalLong() {
		return localLong;
	}

	public void setLocalLong(String localLong) {
		this.localLong = localLong;
	}

	public int getLinkId() {
		return linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	public int getTxInput() {
		return txInput;
	}

	public void setTxInput(int txInput) {
		this.txInput = txInput;
	}

	public int getRxInput() {
		return rxInput;
	}

	public void setRxInput(int rxInput) {
		this.rxInput = rxInput;
	}

	public int getLocalSNRA1() {
		return localSNRA1;
	}

	public void setLocalSNRA1(int localSNRA1) {
		this.localSNRA1 = localSNRA1;
	}

	public int getLocalSNRA2() {
		return localSNRA2;
	}

	public void setLocalSNRA2(int localSNRA2) {
		this.localSNRA2 = localSNRA2;
	}

	public int getRemoteSNRA1() {
		return remoteSNRA1;
	}

	public void setRemoteSNRA1(int remoteSNRA1) {
		this.remoteSNRA1 = remoteSNRA1;
	}

	public int getRemoteSNRA2() {
		return remoteSNRA2;
	}

	public void setRemoteSNRA2(int remoteSNRA2) {
		this.remoteSNRA2 = remoteSNRA2;
	}

	public int getLocalRate() {
		return localRate;
	}

	public void setLocalRate(int localRate) {
		this.localRate = localRate;
	}

	public int getRemoteRate() {
		return remoteRate;
	}

	public void setRemoteRate(int remoteRate) {
		this.remoteRate = remoteRate;
	}

	public KwpPacket getPacket() {
		return packet;
	}

	public void setPacket(KwpPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("WirelessStats-->\n[No. of Links:" + noOfLinks + " linkId:" + linkId + "\nMAC:" + macAddress + "\nIP(R):" + remoteIP +  " Lat(R):" + remoteLat + " Long(r):" + remoteLong+ " SNR1(R):" + remoteSNRA1+ " SNR2(R):" + remoteSNRA2);
		buffer.append("\nLat(L):" + localLat + " Long(L):" + localLong + " SNR1(L):" + localSNRA1+ " SNR2(L):" + localSNRA2);
		buffer.append("txInput:" + txInput + " rxInput:" + rxInput + " Rate(L)" + localRate + " Rate(R)" + remoteRate +"]");
		return buffer.toString();
	}

}
