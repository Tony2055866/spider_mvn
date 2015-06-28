package com.util;

import com.sqider.PageData;

public class VisitUtil {
	public static String[] urls = {
		//"http://acmerblog.iteye.com/blog/2021816",
		//"http://acmerblog.iteye.com/blog/2021514",
		"http://gaotong1991.iteye.com/blog/2024335",
		"http://acmerblog.iteye.com/blog/2024330"
	};
	
	public static void main(String[] args) {
		cnt = 1000;
		url = "http://acmerblog.iteye.com/blog/2021514";
		start();
	}
	
	public static boolean stop = false;
	static int cnt;
	public static String url;
	
	public static void start(){
		new VisitThread().start();
	}
	
	static class VisitThread extends Thread{
		@Override
		public void run() {
			for(int i=0; i<cnt && !stop; i++){
				for(int j=0; j<urls.length; j++){
					PageData pd = MyUtil.getPage(urls[j]);
					//System.out.println(pd.html);
					if(pd.html.contains("您所在的IP地址对")){
						System.out.println("检测到 爬虫！！");
						stop = true;
						break;
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			super.run();
		}
	}
}
