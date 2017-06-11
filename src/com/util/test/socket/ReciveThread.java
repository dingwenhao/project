package com.util.test.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReciveThread extends Thread{
	DatagramSocket DatagramSocket;
	public ReciveThread(DatagramSocket reDatagramSocket) {
		this.DatagramSocket=reDatagramSocket;
	}
	public void run() {
		while(true){
			byte []by=new byte[1024];
			DatagramPacket packet=new DatagramPacket(by,by.length);
			try {
				DatagramSocket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] b=packet.getData();
			String string=new String(b, 0,packet.getLength());
			System.out.println(string);
			System.out.println(packet.getAddress());
			System.out.println(packet.getLength());
			if(string.equals("886")){
				break;
			}
		}
	}
         
}
