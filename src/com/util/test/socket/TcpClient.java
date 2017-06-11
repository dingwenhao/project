package com.util.test.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TcpClient {

	/*测试发送端
	 * 1.创建连接
	 * 2.获取输出流写数据
	 * 3.关闭资源
	 * @param args
	 */
	public static void main(String[] args) {
           try {
			Socket socket=new Socket("192.168.1.101", 8021);
			OutputStream out=socket.getOutputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(out));
			String str=null;
			while( (str=reader.readLine())!=null){
				writer.write(str);
				writer.newLine();
				writer.flush();	
			}
			
		    socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
