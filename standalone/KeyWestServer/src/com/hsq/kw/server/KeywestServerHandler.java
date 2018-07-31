package com.hsq.kw.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.hsq.kw.server.handler.KeywestUDPClientHandler;

public class KeywestServerHandler implements Runnable {
	
	private DatagramSocket udpSocket = null;
	
	private boolean serverRunning = true;
	
	
	public KeywestServerHandler(DatagramSocket udpSocket) {
		this.udpSocket = udpSocket;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[1307];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while (serverRunning) {
			try {
				this.udpSocket.receive(packet);
				System.out.println("Received packet from " + packet.getAddress() + " " + packet.getPort());
				KeywestUDPClientHandler handler = new KeywestUDPClientHandler(packet.getAddress(),packet.getPort(),packet.getData(),this);
				Thread th = new Thread(handler);
				th.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public boolean sendPacket(DatagramPacket packet) {
		try {
			this.udpSocket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
