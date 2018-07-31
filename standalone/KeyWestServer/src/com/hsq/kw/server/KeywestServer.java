package com.hsq.kw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.hsq.kw.server.handler.KeywestClientHandler;

public class KeywestServer {
	
	public static final int PORT = 9876;

	public KeywestServer() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws IOException {
		/*ServerSocket socket = new ServerSocket(PORT);
		System.out.println("Starting server on port " + PORT);
		while (true) {
			Socket client = socket.accept();
			System.out.println("Received connection from " + client.getInetAddress());
			KeywestClientHandler handler = new KeywestClientHandler(client.getInputStream(), client.getOutputStream());
			handler.start();
		}*/
		
		KeywestUDPServer server = new KeywestUDPServer();
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
