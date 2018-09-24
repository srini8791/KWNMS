package com.hsq.kw.packet.vo;

import com.hsq.kw.packet.KwpPacket;

public class KwpInventory {

    // Constants
    public static final byte SERIAL_NUMBER_TYPE = 1;
    public static final byte MODEL_NUMBER_TYPE = 2;
    public static final byte FIRMWARE_VERSION_TYPE = 3;
    public static final byte ETH_SPEED_TYPE = 4;
    public static final byte BANDWIDTH_LIMIT_TYPE = 5;


    // variables
    private KwpPacket packet;
    private String serialNumber;
    private String modelNumber;
    private String firmwareVersion;
    private String ethSpeed;
    private String bandwidthLimit;


    
    public KwpPacket getPacket() {
		return packet;
	}



	public void setPacket(KwpPacket packet) {
		this.packet = packet;
	}



	public String getSerialNumber() {
		return serialNumber;
	}



	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}



	public String getModelNumber() {
		return modelNumber;
	}



	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}



	public String getFirmwareVersion() {
		return firmwareVersion;
	}



	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}



	public String getEthSpeed() {
		return ethSpeed;
	}



	public void setEthSpeed(String ethSpeed) {
		this.ethSpeed = ethSpeed;
	}



	public String getBandwidthLimit() {
		return bandwidthLimit;
	}



	public void setBandwidthLimit(String bandwidthLimit) {
		this.bandwidthLimit = bandwidthLimit;
	}



	public void handleResponses(KwpPacket response) {
        this.packet = response;
        this.serialNumber = packet.getStringValueFromLTVByType(SERIAL_NUMBER_TYPE);
        this.modelNumber = packet.getStringValueFromLTVByType(MODEL_NUMBER_TYPE);
        this.firmwareVersion = packet.getStringValueFromLTVByType(FIRMWARE_VERSION_TYPE);
        this.ethSpeed = packet.getStringValueFromLTVByType(ETH_SPEED_TYPE);
        this.bandwidthLimit = packet.getStringValueFromLTVByType(BANDWIDTH_LIMIT_TYPE);

    }
}
