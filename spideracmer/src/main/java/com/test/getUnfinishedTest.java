package com.test;

import com.main.UnfJob;
import com.util.Init;

public class getUnfinishedTest {
	public static void main(String[] args) {
		Init.init();
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
