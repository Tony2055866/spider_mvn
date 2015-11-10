package com.sqider;

import com.main.Main;
import com.util.MyUtil;
import org.apache.log4j.Logger;
import org.htmlparser.tags.LinkTag;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Baidu extends SearchEngine {
	static Logger logger = Logger.getLogger(Baidu.class);

	
	public List<String> search(String keys[],int page, int flag){
		String url="";
		//poj%201400
/*		if(page == 0){ 
			url = "http://www.baidu.com/s?tn=sayh_1_dg&ie=utf-8&f=8&rsv_bp=1&rsv_spt=3&wd=";
			url += keys[0];	
			for(int i=1; i<keys.length; i++) url += "+" + keys[i];
				url += "&inputT=0";
		}else{
		}*/
		
		url="http://www.baidu.com/s?rn="+page+"&wd=" + URLEncoder.encode(keys[0]);
		for(int i=1; i<keys.length; i++) url += "%20" + URLEncoder.encode(keys[i]);
		url+="&pn=" + 0 + "&tn=sayh_1_dg&ie=utf-8";
		
logger.info(url);
		PageData pd = MyUtil.getPage(url, false);
		
		List<LinkTag> list = MyUtil.parseTags(pd.html, LinkTag.class, "data-click", null);
		List<String> urls = new ArrayList<String>();
		for(LinkTag link:list){
			//logger.info(link.getAttribute("href"));
			//logger.info(link.getLinkText().toLowerCase());
			String title = link.getLinkText();
			
			if(flag == 0){
				if( MyUtil.rightTitle(title, keys) ){
					urls.add(link.getAttribute("href"));
				}
			}else if(flag == 1){
				if( MyUtil.rightTitle1(title, keys) ){
					urls.add(link.getAttribute("href"));
				}
			}
		}
		return urls;
	}
	
	public static void main(String[] args) {
		/*Baidu baidu = new Baidu();
		String[] keys = new String[]{"poj","1011"};
		List<String> urls = new Baidu().search(keys, 30);
		for(String s:urls){
			logger.info(s);
			//MyUtil.parseSearchUrl(s);
		}*/
		
		int page = 10;
		int i=0;
		int max = 100000;
		while(i<max){
			String key = "\"password\" => string(32) site:mogujie.com";
	String url = "https://www.baidu.com/s?wd=%22password%22%20%3D%3E%20string(32)%20site%3Amogujie.com&pn=" +
			page+
			"&oq=%22password%22%20%3D%3E%20string(32)%20site%3Amogujie.com&tn=baiduhome_pg&ie=utf-8&usm=1&rsv_idx=2&rsv_pq=cee039890000281d&rsv_t=db0eByMwc3vTTevuzocB5I9ViJETD5dVMqJjpGs1BwVoO19%2Fr4k9ay1FQ7Meby6zIwfv";
			/*
			String url="http://www.baidu.com/s?rn="+page+"&wd=" + URLEncoder.encode(key);
			url+="&pn=" + 0 + "&tn=sayh_1_dg&ie=utf-8";*/
			logger.info(url);
			PageData pd = MyUtil.getPage(url, false);
			//System.out.println(pd.html);

			File file = new File("D:\\xiaomi\\mogujie2\\" + i + ".html");
			try {
				OutputStream outputStream = new FileOutputStream(file);
				OutputStreamWriter w = new OutputStreamWriter(outputStream, "UTF-8");
				w.write(pd.html);
				w.flush();
				w.close();
				outputStream.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			page += 10;
			i++;
			try {
				Thread.sleep(new Random().nextInt(300));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public List<String> search(String[] keys, int page) {
		return search(keys, page, 0);
	}
	
	
	
}
