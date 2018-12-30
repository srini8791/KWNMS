package com.hsq.kw.server.handler;

import com.hsq.kw.packet.KeywestPacket;
import com.hsq.kw.packet.PacketHeader;
import com.hsq.kw.packet.util.KeywestConstants;
import com.hsq.kw.server.test.ResponseHandler;

public class KWPacketAnalyzer {

	private KeywestPacket packet = null;

	public KWPacketAnalyzer(KeywestPacket packet) {
		this.packet = packet;
	}

	public KeywestPacket analyzeAndReturn() {
		PacketHeader header = packet.getHeader();
		ResponseHandler rHandler = new ResponseHandler();
		KeywestPacket returnPacket = null;
		if (header.getType() == 1) {
			switch (header.getSubType()) {
			case KeywestConstants.CONFIG_GET_REQUEST:
				returnPacket = rHandler.sendConfigResponse();
				break;
			case KeywestConstants.SYSINFO_GET_REQUEST:
				System.out.println("Received Packet");
				System.out.println(packet);
				returnPacket = rHandler.sendSysinfoResponse();
				break;
			case KeywestConstants.INVENTORY_GET_REQUEST:
				System.out.println("Received Packet");
				System.out.println(packet);
				returnPacket = rHandler.sendInventoryResponse();
				break;
			}
		}

		return returnPacket;
	}

}
