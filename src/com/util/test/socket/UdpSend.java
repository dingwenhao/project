package com.util.test.socket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UdpSend {

	public static void main(String[] args) throws SocketException {
		
		DatagramSocket ds=new DatagramSocket();
		  new SendThread(ds){
			  
		  }.start();
		
	}

}
