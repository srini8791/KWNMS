package com.hsq.kw.packet.vo;

import com.hsq.kw.packet.KwpPacket;

public class KwpSysInfo {

    // Constants
    public static final byte SYSID_TYPE = 1;
    public static final byte SYSNAME_TYPE = 2;
    public static final byte SYSLOCATION_TYPE = 3;
    public static final byte SYSCONTACT_TYPE = 4;
    public static final byte SYSDESCRIPTION_TYPE = 5;
    public static final byte GEOLATITUDE_TYPE = 6;
    public static final byte GEOLONGITUDE_MODE = 7;

    // request/response packet
    KwpPacket packet = null;

    // Variables
    private String sysId;
    private String sysName;
    private String sysContact;
    private String sysDescription;
    private String geoLatitude;
    private String geLongitude;



    public KwpPacket getPacket() {
		return packet;
	}



	public void setPacket(KwpPacket packet) {
		this.packet = packet;
	}



	public String getSysId() {
		return sysId;
	}



	public void setSysId(String sysId) {
		this.sysId = sysId;
	}



	public String getSysName() {
		return sysName;
	}



	public void setSysName(String sysName) {
		this.sysName = sysName;
	}



	public String getSysContact() {
		return sysContact;
	}



	public void setSysContact(String sysContact) {
		this.sysContact = sysContact;
	}



	public String getSysDescription() {
		return sysDescription;
	}



	public void setSysDescription(String sysDescription) {
		this.sysDescription = sysDescription;
	}



	public String getGeoLatitude() {
		return geoLatitude;
	}



	public void setGeoLatitude(String geoLatitude) {
		this.geoLatitude = geoLatitude;
	}



	public String getGeLongitude() {
		return geLongitude;
	}



	public void setGeLongitude(String geLongitude) {
		this.geLongitude = geLongitude;
	}



	public void handleResponses(KwpPacket response) {
        this.packet = response;
        this.sysId = packet.getStringValueFromLTVByType(SYSID_TYPE);
        this.sysName = packet.getStringValueFromLTVByType(SYSNAME_TYPE);
        this.sysContact = packet.getStringValueFromLTVByType(SYSCONTACT_TYPE);
        this.sysDescription = packet.getStringValueFromLTVByType(SYSDESCRIPTION_TYPE);
        this.geoLatitude = packet.getStringValueFromLTVByType(GEOLATITUDE_TYPE);
        this.geLongitude = packet.getStringValueFromLTVByType(GEOLONGITUDE_MODE);
    }
}
