package com.sevlets;

import com.main.Main;
import com.util.HibernateSessionFactory;

import java.io.File;

public class OJTask extends Thread {
	private String oj;
	private String ojbak;
	private int s;
	private int e;
	private int delayHours;
	public static boolean stop = true;
	public static File ojlogFile;

	public OJTask(){
		
	}
	
	public OJTask(String oj, String ojbak, int s, int e, int delayHours) {
		super();
		this.oj = oj;
		this.ojbak = ojbak;
		this.s = s;
		this.e = e;
		this.delayHours = delayHours;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		HibernateSessionFactory.closeSession();
		//super.run();
		try {
			Main.startOJTask(oj, ojbak, s, e, delayHours);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
