package com.hsq.kw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.hsq.kw.server.handler.KeywestClientHandler;

public class KeywestServer {
	
	public static final int PORT = 7861;

	public KeywestServer() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			throw new Exception("Invalid number of arguments. Usage KeywestServer <IPAddress to bind>");
		}
		KeywestUDPServer server = new KeywestUDPServer(args[0]);
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
