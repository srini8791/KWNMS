package com.hsq.kw.server.handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import com.hsq.kw.packet.KwpPacket;
import com.hsq.kw.server.KeywestServerHandler;

public class KeywestUDPClientHandler implements Runnable {
	
	//private DatagramPacket dPacket = null;
	private InetAddress clientAddress = null;
	private int port = 0;
	private byte[] data = null;
	private KeywestServerHandler  handler = null;
	
	
	public KeywestUDPClientHandler(InetAddress address, int port, byte[] data, KeywestServerHandler handler) {
		System.out.println("Handler added to the packet");
		this.clientAddress = address;
		this.port = port;
		this.data = data;
		this.handler = handler;
	}

	@Override
	public void run() {
		System.out.println("Separate thread has been started to handle the client" + this);
		KwpPacket packet = new KwpPacket(this.data);
		System.out.println("extracted packet from the client" + packet);
		KWPacketAnalyzer analizer = new KWPacketAnalyzer(packet);
		KwpPacket returnPacket = analizer.analyzeAndReturn();
		byte[] returnData;
		try {
			returnData = returnPacket.toByteArray();
			DatagramPacket retdPacket = new DatagramPacket(returnData, returnData.length);
			retdPacket.setAddress(this.clientAddress);
			retdPacket.setPort(this.port);
			System.out.println("Sending reply to " + retdPacket);
			this.handler.sendPacket(retdPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}