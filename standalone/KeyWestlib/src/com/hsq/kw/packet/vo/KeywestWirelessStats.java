package com.hsq.kw.packet.vo;

import com.hsq.kw.packet.KeywestPacket;

public class KeywestWirelessStats {
	
	public static final int TX_DATA_TYPE = 1;
	public static final int RX_DATA_TYPE = 2;
	public static final int TX_MGMT_DATA_TYPE = 3;
	public static final int RX_MGMT_DATA_TYPE = 4;
	public static final int MPDU_ERRORS_TYPE = 5;
	public static final int PHY_ERRORS_TYPE = 6;
	
	
	private String txDataPkts;
	private String rxDataPkts;
	private String txMgmtDataPkts;
	private String rxMgmtDataPkts;
	private String mpduErrors;
	private String phyErrors;
	private KeywestPacket packet;
	
	public KeywestWirelessStats(KeywestPacket packet) {
		this.packet = packet;
		this.txDataPkts = packet.getStringValueFromLTVByType(TX_DATA_TYPE);
		this.rxDataPkts = packet.getStringValueFromLTVByType(RX_DATA_TYPE);
		this.txMgmtDataPkts = packet.getStringValueFromLTVByType(TX_MGMT_DATA_TYPE);
		this.rxMgmtDataPkts = packet.getStringValueFromLTVByType(RX_MGMT_DATA_TYPE);
		this.mpduErrors = packet.getStringValueFromLTVByType(MPDU_ERRORS_TYPE);
		this.phyErrors = packet.getStringValueFromLTVByType(PHY_ERRORS_TYPE);
	}
	
	public KeywestWirelessStats() {
		this.packet = new KeywestPacket((byte) 1,(byte) 1, (byte) 2);
	}
	
	public String getTxDataPkts() {
		return txDataPkts;
	}
	public void setTxDataPkts(String txDataPkts) {
		this.txDataPkts = txDataPkts;
	}
	public String getRxDataPkts() {
		return rxDataPkts;
	}
	public void setRxDataPkts(String rxDataPkts) {
		this.rxDataPkts = rxDataPkts;
	}
	public String getTxMgmtDataPkts() {
		return txMgmtDataPkts;
	}
	public void setTxMgmtDataPkts(String txMgmtDataPkts) {
		this.txMgmtDataPkts = txMgmtDataPkts;
	}
	public String getRxMgmtDataPkts() {
		return rxMgmtDataPkts;
	}
	public void setRxMgmtDataPkts(String rxMgmtDataPkts) {
		this.rxMgmtDataPkts = rxMgmtDataPkts;
	}
	public String getMpduErrors() {
		return mpduErrors;
	}
	public void setMpduErrors(String mpduErrors) {
		this.mpduErrors = mpduErrors;
	}
	public String getPhyErrors() {
		return phyErrors;
	}
	public void setPhyErrors(String phyErrors) {
		this.phyErrors = phyErrors;
	}
	
	@Override
	public String toString() {
		return "TX Data:" + this.txDataPkts + " MGMT: " + this.txMgmtDataPkts + "\n"
				+ "RX Data:" + this.rxDataPkts + " MGMT: " + this.rxMgmtDataPkts + "\n"
				+ " Phy Errors:" + this.phyErrors + " MPDU Errors:" + this.mpduErrors;
	}

}
