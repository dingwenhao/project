package com.util.test.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 目的：客户端从一个文件中写入数据，发送给服务端写入文件  此是客户端
 * @author huiyi
 *
 *分析： 
 *      
 */
public class TcpFileClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		// 读取文件中的内容到输入流
				String path="C:/work/nihao.txt";
				InputStreamReader reader=new InputStreamReader(new FileInputStream(path),"UTF-8");
				BufferedReader bufferedReader=new BufferedReader(reader);
				Socket socket=new Socket(InetAddress.getByName("192.168.1.101"), 8011);
				BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String line=null;
				while((line=bufferedReader.readLine())!=null){
					writer.write(line);
					writer.newLine();
				}
				writer.flush();
				bufferedReader.close();
				socket.close();

	}

}
