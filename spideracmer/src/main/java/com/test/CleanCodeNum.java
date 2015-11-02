package com.test;

import java.util.Scanner;

public class CleanCodeNum {

	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CleanCodeNum.class);


	public static void main(String[] args) {
		String res = "";
		Scanner scanner = new Scanner(CleanCodeNum.class.getClassLoader().getResourceAsStream("com/test/code.txt"));
		
		while(scanner.hasNextLine()){
			String line = scanner.nextLine().trim();
			
			while( line.length() > 0 && ( ( line.charAt(0) >= '0' && line.charAt(0) <= '9') ||  line.charAt(0) >= ':') ){
				line = line.substring(1);
			}
			res += line + "\n";
		}
		logger.info(res);
		
	}
}
