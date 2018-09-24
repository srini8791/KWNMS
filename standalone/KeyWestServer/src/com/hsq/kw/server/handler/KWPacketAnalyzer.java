package com.hsq.kw.server.handler;

import com.hsq.kw.packet.KwpPacket;
import com.hsq.kw.packet.PacketHeader;
import com.hsq.kw.packet.util.KeywestConstants;
import com.hsq.kw.server.test.ResponseHandler;

public class KWPacketAnalyzer {

	private KwpPacket packet = null;

	public KWPacketAnalyzer(KwpPacket packet) {
		this.packet = packet;
	}

	public KwpPacket analyzeAndReturn() {
		PacketHeader header = packet.getHeader();
		ResponseHandler rHandler = new ResponseHandler();
		KwpPacket returnPacket = null;
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
