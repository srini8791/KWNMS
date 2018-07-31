package com.hsq.kw.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.hsq.kw.packet.KeywestPacket;

/**
 * @author Spittala
 *
 */
public class PacketTesterClient {

	public static void main1(String[] args) throws UnknownHostException, IOException {
		
		byte[] buffer = new byte[1307];
		Socket socket = new Socket("192.168.2.242",9876);
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		KeywestPacket packet = new ResponseHandler().sendConfigResponse();
		dos.write(packet.toByteArray());
		
		dis.read(buffer);
		KeywestPacket recivedPacket = new KeywestPacket(buffer);
		System.out.println(recivedPacket);
		socket.close();
	}
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = new byte[1307];
		
		DatagramSocket socket = new DatagramSocket();
		KeywestPacket packet = new ResponseHandler().getWirelessStats();
		byte[] requestArr = packet.toByteArray();
		System.out.println("Sending packet from " + socket.getLocalPort());
		DatagramPacket dPacket = new DatagramPacket(requestArr, requestArr.length, InetAddress.getByName("192.168.1.1"), 9181);
		socket.send(dPacket);
		System.out.println("packet sent");
		DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length);
		socket.receive(rPacket);
		KeywestPacket rKwPacket = new KeywestPacket(rPacket.getData());
		System.out.println(rKwPacket);
		socket.close();
		
	}
}
