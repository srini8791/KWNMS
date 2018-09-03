package com.hsq.kw.packet.vo;

import com.hsq.kw.packet.KeywestPacket;

public class KeywestEthernetStats {

	public static final int TX_PKTS_TYPE = 1;
	public static final int RX_PKTS_TYPE = 2;
	public static final int TX_BYTES_TYPE = 3;
	public static final int RX_BYTES_TYPE = 4;
	public static final int TX_ERRORS_TYPE = 5;
	public static final int RX_ERRORS_TYPE = 6;
	
	
	private String txPkts;
	private String rxPkts;
	private String txBytes;
	private String rxBytes;
	private String txErrors;
	private String rxErrors;
	
	private KeywestPacket packet;
	
	public KeywestEthernetStats(KeywestPacket packet) {
		this.packet = packet;
		this.txPkts = packet.getStringValueFromLTVByType(TX_PKTS_TYPE);
		this.rxPkts = packet.getStringValueFromLTVByType(RX_PKTS_TYPE);
		this.txBytes = packet.getStringValueFromLTVByType(TX_BYTES_TYPE);
		this.rxBytes = packet.getStringValueFromLTVByType(RX_BYTES_TYPE);
		this.txErrors = packet.getStringValueFromLTVByType(TX_ERRORS_TYPE);
		this.rxErrors = packet.getStringValueFromLTVByType(RX_ERRORS_TYPE);
	}
	
	public KeywestEthernetStats() {
		this.packet = new KeywestPacket((byte)1, (byte)1, (byte)3);
	}
	
	public String getTxPkts() {
		return txPkts;
	}
	public void setTxPkts(String txPkts) {
		this.txPkts = txPkts;
	}
	public String getRxPkts() {
		return rxPkts;
	}
	public void setRxPkts(String rxPkts) {
		this.rxPkts = rxPkts;
	}
	public String getTxBytes() {
		return txBytes;
	}
	public void setTxBytes(String txBytes) {
		this.txBytes = txBytes;
	}
	public String getRxBytes() {
		return rxBytes;
	}
	public void setRxBytes(String rxBytes) {
		this.rxBytes = rxBytes;
	}
	public String getTxErrors() {
		return txErrors;
	}
	public void setTxErrors(String txErrors) {
		this.txErrors = txErrors;
	}
	public String getRxErrors() {
		return rxErrors;
	}
	public void setRxErrors(String rxErrors) {
		this.rxErrors = rxErrors;
	}

	@Override
	public String toString() {
		return "TX Packets:" + this.txPkts + " Bytes:"+this.txBytes + " Errors:" + this.txErrors + "\n" +
				"RX Packets:" + this.rxPkts + " Bytes:"+this.rxBytes + " Errors:" + this.rxErrors;
	}
}
