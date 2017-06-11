package com.util.test.socket;

import java.net.DatagramSocket;
import java.net.SocketException;


public class Udprecive {

	
	public static void main(String[] args) throws SocketException {
		
		DatagramSocket reDatagramSocket=new DatagramSocket(8089);
		new ReciveThread(reDatagramSocket){
			
		}.start();
	}

}
