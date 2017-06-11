package com.util.test.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.jfinal.template.stat.ast.Set;


/**
 * IO流 文件复制的5种方式
 * @author huiyi
 *
 */
public class IoTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String src="C:/work/djx/java/学习/base/day21/avi/21.01_IO流(转换流出现的原因及格式).avi";
		String des="c:/work/test.avi";
		//File file=new File("C:/work/developToos/other/test");
		//file.delete();
		//System.out.println(file.getName());
		//System.out.println(file.getAbsolutePath());
		    // method1(src,des);
		      // method2(src,des);
		       // method3(src,des);
		      // method4(src,des);
		      // method5(src,des);
		      // mothod6("c:/work/test.txt");
		      // mothod7("c:/work/test.txt");
		//copyfiles("C:/work/developToos/tools","C:/work/developToos/other/test");
		//Studentg;rade();
		//randomaccess();
		//objectoutput();
		//ObjectInput();
		propertiseTest();
	}
	//复制文件
	public static void method1(String srcString,String  desString) throws IOException{
		BufferedReader reader=new BufferedReader(new  FileReader(srcString));
		File desFile=new File(desString);
		if(!desFile.exists()){
			desFile.createNewFile();
		}
		BufferedWriter write=new BufferedWriter(new FileWriter(desFile));
		String len="";
		while((len=reader.readLine())!=null){
			write.write(len);
			write.newLine();
			write.flush();
		}
		reader.close();
		write.close();
	}
	
	public static void method2(String srcString,String desString) throws IOException{
		BufferedReader in =new  BufferedReader(new FileReader(srcString));
		BufferedWriter out=new BufferedWriter(new FileWriter(desString));
		char[] line=new   char [1024];
		int len=0;
		while((len=in.read(line))!=-1){
			out.write(line, 0, len);
		}
		out.close();
		in.close();
	}
	
	
	public   static void  method3(String src,String des) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(src));
		int len=0;
		BufferedWriter writer=new BufferedWriter(new FileWriter(des));
		while((len=reader.read())!=-1){
			writer.write(len);
		}
		reader.close();
		writer.close();
		
		
	}
	
	public  static void method4(String src,String des) throws IOException{
		FileReader in= new FileReader(src);
		FileWriter out=new FileWriter(des);
		char [] ch=new char[1024];
		int len;
		while((len=in.read(ch))!=-1){
			out.write(ch,0,len);
		}
		in.close();
		out.close();
		
		
	}


	public  static void method5(String src,String des) throws IOException{
		FileReader in= new FileReader(src);
		FileWriter out=new FileWriter(des);
		int len;
		while((len=in.read())!=-1){
			out.write(len);
		}
		in.close();
		out.close();
		
		
	}
	
	public static  void  mothod6(String  des) throws IOException{
		
		List<String> list=new ArrayList<String>();
		list.add("fdgdsd");
		list.add("fzxv");
		list.add("fdgdvsddsd");
		list.add("fdgddvdsvsd");
		BufferedWriter out =new  BufferedWriter(new FileWriter(des));
		for(String d:list){
			out.write(d);
			out.write("/b/n");
		}
		out.close();
		
		
	}
	
	public static  void  mothod7(String  des) throws IOException{
		    
		    InputStreamReader in=new InputStreamReader(new FileInputStream(des));
		    BufferedReader ii =new  BufferedReader(in);
			List<String> list=new ArrayList<String>();
		    String str="";
			while((str=ii.readLine())!=null){
				list.add(str);
				System.out.println(str);
			}	
			in.close();
		}
	/**
	 * 复制文件夹下的多个文件   ，使用递归
	 * 分析：
	 * 1.检测目标文件夹是否存在  不存在就创建
	 * 2获取一个文件夹下的所有文件遍历文件
	 * 3 如果是文件    复制文件
	 * 4如果是文件夹递归调用copyfiles方法
	 *
	 * @throws IOException 
	 */ 
	public static  void  copyfiles(String src,String des) throws IOException{
		File desFile= new File(des);
		File file = new File(src);
		if(!desFile.exists()){
		  if(file.isFile()){
			  desFile.createNewFile();
		  }else{
			  desFile.mkdir();
		  }
		}
		File[] filearray=file.listFiles();
		for(File f:filearray){
			if(f.isFile()){
				method1(f.getAbsolutePath(),desFile.getAbsolutePath()+"/"+f.getName());
			}else{
				copyfiles(f.getAbsolutePath(),desFile+"/"+f.getName());
			}
		}
		System.out.println(file.getName()+"---------复制完毕");
	}
	/**
	 * 键盘录入学生成绩    英语   数学  语文      按照总分排序，并且输出到文本文件中
	 * 分析：
	 * 键盘录入    保存学生总数
	 * 如果是按得enter键就退出录入放进test.txt 文件中
	 * @throws IOException
	 */
	public static  void  Studentgrade() throws IOException{
		String name;
		float english;
		float math;
		float chinese;
		int len=0;
		List<HashMap<String, Object>>list=new ArrayList<HashMap<String, Object>>();
		InputStream in=System.in;
		Scanner scanner =new Scanner(in); 
		System.out.println("请输入姓名");
		HashMap<String, Object> map=new HashMap<String, Object>();
		while(scanner.hasNext()){
			if(len==0){
				map=new HashMap<String, Object>();
			}
			String grade= scanner.next();	
			if(grade.equals("over")){
				scanner.close();
				break;
			}
			switch (len) {
			case 0:
				map.put("name", grade);
				System.out.println("请输入"+map.get("name")+"的语文成绩");
				break;
			case 1:
				System.out.println("请输入"+map.get("name")+"的数学成绩");
				 chinese=Float.parseFloat(grade);
				map.put("chinese", chinese);
				break;
			case 2:
				System.out.println("请输入"+map.get("name")+"的英语成绩");
				math=Float.parseFloat(grade);
				map.put("math", math);
				break;
			case 3:
				System.out.println("请输入姓名");
				english=Float.parseFloat(grade);
				map.put("english", english);
				break;
			default:
				break;
			}
			len+=1;
			if(len==4){
				map.put("total", Float.parseFloat(map.get("math").toString())+Float.parseFloat(map.get("chinese").toString())+Float.parseFloat(map.get("english").toString()));
				list.add(map);	
				len=0;
			}
			
		}
		Collections.sort(list, new Comparator<HashMap<String, Object>>(){
			@Override
			public int compare(HashMap<String, Object> o1,
					HashMap<String, Object> o2) {
				  if(Float.parseFloat(o1.get("total").toString())- Float.parseFloat(o1.get("total").toString())>0)
				   return 0 ;
				  else
					return 1;
			}
		});
		for(HashMap<String, Object> map1:list){
			BufferedWriter writer=new BufferedWriter(new FileWriter("C:/work/developToos/test.txt",true));
			String maString=map1.get("name")+"  "+map1.get("math")+map1.get("chinese")+"  "+map1.get("english");
			writer.write(maString);
			writer.newLine();
			writer.flush();
			writer.close();
		}
		System.out.println("完毕");
		
	}
	
	//随机访问流
	public static void randomaccess() throws IOException{
		RandomAccessFile file=new RandomAccessFile("c:/work/nihao.txt", "rw");
		file.writeUTF("中国");
		file.close();
		
	}
	public static  void objectoutput() throws IOException{
		Person person=new Person();
		person.setName("刘瑶");
		Person person1=new Person();
		person1.setName("丁建祥");
		person1.setSex("男");
		ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream("c:/work/nihao.txt"));
		outputStream.writeObject(person);
		outputStream.writeObject(person1);
		outputStream.close();
	}
	
	public static void ObjectInput() throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream objectinput=new ObjectInputStream(new FileInputStream("c:/work/nihao.txt")); 
		Person person =(Person)objectinput.readObject();
		Person person1=(Person)objectinput.readObject();
		
		objectinput.close();
		System.out.println(person.getName());
		System.out.println(person1.getName());
	}
	
	
	public static void propertiseTest() throws FileNotFoundException, IOException{
		
		 Properties   properties=new Properties();
		 
		 FileReader reader=new FileReader("c:/work/nihao.txt");
		 properties.load(reader);
		 java.util.Set<String> keySet=properties.stringPropertyNames();
		 for(String key:keySet){
			 if(key.equals("name"))
			 properties.setProperty("name","孙淑华");
		 }
		FileWriter writer= new FileWriter("c:/work/nihao.txt");
		 properties.store(writer, "修改");
		 reader.close();
		 writer.close();
		 System.out.println(properties);
	}
	
	public static void propertiseTest2(){
		
	}
	

}
