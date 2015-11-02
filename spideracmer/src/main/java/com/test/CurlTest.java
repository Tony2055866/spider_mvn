package com.test;

public class CurlTest {
	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CurlTest.class);

	
	public static void main(String[] args) throws Exception {
		Runtime run = Runtime.getRuntime();
		String url = "https://www.google.com.hk/search?num=20&biw=1366&bih=344&q=hdu+3474&oq=hdu+3474";
		if(args.length > 0) {
			url = args[0];
		}
		//Process p = run.exec("curl -L "  + url);
		logger.info("curl -L "  + url);
//		String line;
//		    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));  
//		    while ((line = input.readLine()) != null) {  
//		        // logger.info(line);  
//		       logger.info(line);
//		    }  
	}
}
