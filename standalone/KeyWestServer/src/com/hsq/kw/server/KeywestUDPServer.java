package com.hsq.kw.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class KeywestUDPServer {
	
	private int port = 9876;
	
	private String host = "localhost";
	
	private DatagramSocket udpSocket = null;
	
	private boolean serverInitialized = false;
	
	public KeywestUDPServer() {
		this("localhost",9876);
	}
	
	public KeywestUDPServer(String host, int port) {
		
		this.host = host;
		this.port = port;
		initialize();
	}
	
	protected void initialize() {
		try {
			System.out.println("UDP server is about to start on port " + port);
			this.udpSocket = new DatagramSocket(port, InetAddress.getByName(host));
			serverInitialized = true;
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean start() throws Exception {
		if (serverInitialized && this.udpSocket != null) {
			KeywestServerHandler handler = new KeywestServerHandler(udpSocket);
			Thread th = new Thread(handler);
			th.start();
		} else {
			throw new Exception("Server failed to start");
		}
		return false;
	}

}
