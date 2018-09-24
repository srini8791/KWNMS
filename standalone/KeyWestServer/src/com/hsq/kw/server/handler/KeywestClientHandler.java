package com.hsq.kw.server.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.hsq.kw.packet.KwpPacket;

public class KeywestClientHandler implements Runnable {
	
	private boolean started = false;

	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	
	private static final int BUFFER_SIZE = 1307;
	
	public KeywestClientHandler(InputStream input, OutputStream out) {
		dis = new DataInputStream(input);
		dos = new DataOutputStream(out);
	}
	
	public void start() {
		new Thread(this).start();
		started = true;
	}
	
	public void stop() {
		started = false;
		try {
			dis.close();
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {
		byte[] buffer = new byte[BUFFER_SIZE];
		boolean cont = false;
		try {
			dis.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			stop();
		}
		if (buffer != null && buffer.length > BUFFER_SIZE) {
			System.out.println("Invalid buffer size");
			stop();
		}
		
		KwpPacket packet = new KwpPacket(buffer);
		KWPacketAnalyzer analyzer = new KWPacketAnalyzer(packet);
		KwpPacket returnPacket = analyzer.analyzeAndReturn();
		try {
			dos.write(returnPacket.toByteArray());
		} catch (IOException e) {
		}
	}
}
