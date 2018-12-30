package com.hsq.kw.packet.vo;

import com.hsq.kw.packet.KeywestLTVPacket;
import com.hsq.kw.packet.KeywestPacket;
import com.hsq.kw.packet.util.ConversionUtil;

public class LinkTest {
	
	public static final byte START_STOP = 1;
	public static final byte REMOTE_MAC = 2;
	public static final byte DIRECTION = 3;
	public static final byte DURATION = 4;
	
	// Start Stop Link Test
	private int startStop;
	private String macAddr;
	private int direction;
	private int duration;
	
	private KeywestPacket packet = null;
	
	public LinkTest() {
	}
	
	public LinkTest(KeywestPacket packet) {
		this.startStop = packet.getLTVPacketByType(START_STOP).getUnsignedToInt();
		this.macAddr = packet.getMacAddressFromLTV(REMOTE_MAC);
		this.direction = packet.getLTVPacketByType(DIRECTION).getUnsignedToInt();
		this.duration = packet.getLTVPacketByType(DURATION).getUnsignedToInt();
	}
	
	public LinkTest(int status, String macAddress,int direction, int duration) {
		packet = new KeywestPacket((byte)1, (byte)3, (byte)4);
		this.startStop = status;
		this.macAddr = macAddress;
		this.direction = direction;
		this.duration = duration;
	}

	public int getStartStop() {
		return startStop;
	}

	public void setStartStop(int startStop) {
		this.startStop = startStop;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public KeywestPacket buildPacketFromUI() {
		this.packet.getLTVPacket().clear();
		this.packet.addLTVToPacket(new KeywestLTVPacket(START_STOP, ConversionUtil.intToSingleByte(startStop)));
		this.packet.addLTVToPacket(new KeywestLTVPacket(REMOTE_MAC, ConversionUtil.convertMACToBytes(macAddr)));
		this.packet.addLTVToPacket(new KeywestLTVPacket(DIRECTION, ConversionUtil.intToSingleByte(direction)));
		this.packet.addLTVToPacket(new KeywestLTVPacket(DURATION,ConversionUtil.intToSingleByte(duration)));
		return this.packet;
	}

}
