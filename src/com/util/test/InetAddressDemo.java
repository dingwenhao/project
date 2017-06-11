package com.util.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressDemo {

	public static void main(String[] args) throws IOException {
		try {
			InetAddress address=InetAddress.getByName("DESKTOP-9ROF1MP");
			System.out.println(address.getHostAddress()+"...."+address.getHostName()+address.isReachable(200));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
