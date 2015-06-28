package com.test;

import com.main.UnfJob;
import com.util.ItblogInit;

public class getUnfinishedTest {
	public static void main(String[] args) {
		ItblogInit.init();
		try {
			new Thread(){
				public void run() {
					
					try {
						UnfJob.getUnfinished();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
}
