package com.util.test.thread;

public class TreadTest extends Thread{

	
	public void  run(){
		
		for(int i=0;i<500;i++){
			System.out.println(i+"------"+Thread.currentThread().getName());
		}
	}
	public static void main(String[] args) {
		
		TreadTest th0=new TreadTest();
		TreadTest th1=new TreadTest();
		TreadTest th2=new TreadTest();
		TreadTest th3=new TreadTest();
		th0.start();
		th1.start();
		th2.start();
		th3.start();
	}

}
