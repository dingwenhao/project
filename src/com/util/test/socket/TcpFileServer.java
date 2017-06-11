package com.util.test.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 目的：客户端从一个文件中写入数据，发送给服务端写入文件  此是服务端
 * 
 * @author huiyi
 *1、创建serverSocket对象
 *      2获取客户端的socket
 *      3、获取输入流
 *      4、输出到文件
 *      5、关闭资源
 */
public class TcpFileServer {

	public static void main(String[] args) throws IOException {
          ServerSocket serverSocket=new ServerSocket(8011);
          
          Socket socket=serverSocket.accept();
          
          InputStream inputStream=socket.getInputStream();
          BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
          String path="C:/work/nihao11.txt";
          BufferedWriter writer=new BufferedWriter(new FileWriter(path));
          
          String line=null;
          while((line=reader.readLine())!=null){
        	  writer.write(line);
        	  writer.newLine();
          }
		writer.flush();
		socket.close();
	}

}
