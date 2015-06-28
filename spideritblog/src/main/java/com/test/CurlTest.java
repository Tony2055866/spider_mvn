package com.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CurlTest {
	public static void main(String[] args) throws Exception {
		Runtime run = Runtime.getRuntime();
		String url = "https://www.google.com.hk/search?num=20&biw=1366&bih=344&q=hdu+3474&oq=hdu+3474";
		if(args.length > 0){
			 url = args[0];
		}
		//Process p = run.exec("curl -L "  + url);
		System.out.println("curl -L "  + url);
//		String line;
//		    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));  
//		    while ((line = input.readLine()) != null) {  
//		        // System.out.println(line);  
//		       System.out.println(line);
//		    }  
	}
}
