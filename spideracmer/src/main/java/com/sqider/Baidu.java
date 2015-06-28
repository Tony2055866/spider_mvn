package com.sqider;

import com.main.Main;
import com.util.MyUtil;
import org.htmlparser.tags.LinkTag;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Baidu extends SearchEngine {

	
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
		
System.out.println(url);
if(Main.out!=null)
Main.out.println(url);
		PageData pd = MyUtil.getPage(url, false);
		
		List<LinkTag> list = MyUtil.parseTags(pd.html, LinkTag.class, "data-click", null);
		List<String> urls = new ArrayList<String>();
		for(LinkTag link:list){
			//System.out.println(link.getAttribute("href"));
			//System.out.println(link.getLinkText().toLowerCase());
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
		Baidu baidu = new Baidu();
		String[] keys = new String[]{"poj","1011"};
		List<String> urls = new Baidu().search(keys, 30);
		for(String s:urls){
			System.out.println(s);
			//MyUtil.parseSearchUrl(s);
		}
	}

	@Override
	public List<String> search(String[] keys, int page) {
		return search(keys, page, 0);
	}
	
	
	
}
