package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;

public class TestFile {
	public static void main(String[] args) {
		try {
			String file="C:/Users/Administrator/AppData/Local/genuitec/common/plugins/com.genuitec.eclipse.easie.tomcat.myeclipse_8.6.0.me201007231647/tomcat/bin/ojlog_1387786084095.txt";
			InputStream in = new FileInputStream("C:/Users/Administrator/AppData/Local/genuitec/common/plugins/com.genuitec.eclipse.easie.tomcat.myeclipse_8.6.0.me201007231647/tomcat/bin/ojlog_1387786084095.txt");
		//	System.out.println(in.available());
			
			File f = new File("D:/PHP/wordpress-3.7-zh_CN/wordpress/spiderlog/new.txt");
//			PrintStream ps = new PrintStream(f);
//			System.setOut(ps);
//			System.out.println("你好，haha\n");
//			
//			ps.flush();
//			ps.close();
//			RandomAccessFile rmf= new RandomAccessFile(f, "r");
//			if(rmf.length() > 10000){
//				byte[] buffer = new byte[10000];
//				rmf.seek(in.available()-10000);
//				rmf.read(buffer, 0, 10000);
//		    	String str = new String(buffer,"utf-8");
//		    	System.out.println(str);
//			}
			Thread.sleep(2000);
			InputStream is = new FileInputStream(f) ;
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String str = new String(buffer,"utf-8");
			System.out.println(str);
//			if(in.available() > 10000){
//			    	byte[] buffer = new byte[10000];
//			    	in.read(buffer, in.available()-10000, 10000);
//			    	String str = new String(buffer);
//			    	System.out.println(str);
//			 }
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
