package com.itong.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTask extends Thread {
	boolean isrun = false;
	private Object object = new Object();

	private static Logger logger = LoggerFactory.getLogger(TestTask.class);


	@Override
	public void run() {
		// TODO Auto-generated method stub
//		super.run();
		f1();
	}
	
	public   void f2() {
		try {
			synchronized (this) {
				isrun = true;
				this.notifyAll();
				logger.info("notify all.....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  void f1() {
		try {
			synchronized (this) {
				while(!isrun){
					this.wait();
				}
			}
			logger.info("working... ");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TestTask t = new TestTask();
		t.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.f2();
		
	}
}
