package com.util.test.thread;

public class RunnableTest  implements Runnable{

	public static void main(String[] args) {
		Thread th0=new Thread(new RunnableTest());
		Thread th1=new Thread(new RunnableTest());
		Thread th2=new Thread(new RunnableTest());
		Thread th3=new Thread(new RunnableTest());
		th0.start();
		th1.start();
		th2.start();
		th3.start();

	}

	@Override
	public void run() {
		for(int i=0;i<500;i++){
			System.out.println(i+"------"+Thread.currentThread().getName());
		}
	}

}
