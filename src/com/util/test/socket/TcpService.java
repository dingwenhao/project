package com.util.test.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 测试TCP协议说的接受
 * @author huiyi
 * 1、建立连接,监听客户端
 * 2、接受数据
 * 3.关闭资源
 *
 */
public class TcpService {

	public static void main(String[] args) throws InterruptedException {
	  
		try {
			ServerSocket serversocket=new ServerSocket(8021);
			Socket socket=serversocket.accept();
			InputStream in=socket.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));

            String out=null;
            while((out=reader.readLine())!=null){
            	System.out.println(out);
            }
		socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  
	}

}
