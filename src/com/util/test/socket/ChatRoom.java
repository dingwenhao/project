package com.util.test.socket;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ChatRoom {

	public static void main(String[] args) throws SocketException {
		DatagramSocket recive=new DatagramSocket(8089);
		
		DatagramSocket send=new DatagramSocket();
		ReciveThread reTread=new ReciveThread(recive);
		SendThread senTread=new SendThread(send);
		reTread.start();
		senTread.start();
		

	}

}
