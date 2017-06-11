package com.util.test.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class SendThread extends Thread{

	public DatagramSocket ds;

	public SendThread(DatagramSocket ds) {
		this.ds=ds;
	}

	public void run(){
		Scanner scanner=new Scanner(System.in);
		while(scanner.hasNext()){
			byte[]by=scanner.nextLine().getBytes();
			InetAddress address = null;
			try {
				address = InetAddress.getByName("192.168.1.101");
				DatagramPacket packet=new DatagramPacket(by, 0, by.length, address, 8089);
				ds.send(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
