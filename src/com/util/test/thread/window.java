package com.util.test.thread;

public class window extends Thread{
    public static Object obj=new Object();
	public static int Count=0;
	public void run(){
		synchronized (obj) {
			while(Count<100){
			    try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			    
			    	Count++;
			    	System.out.println("我是"+Thread.currentThread().getName()+"窗口,我卖出第"+Count+"张票");
					
				}
			
		}
	}
	public static void main(String[] args) throws InterruptedException {
        window window1=new window();
        window window2=new window();
        window window3=new window();
        window1.start();
        window2.start();
        window3.start();
	}

}
