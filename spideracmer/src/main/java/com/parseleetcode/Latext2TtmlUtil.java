package com.parseleetcode;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Latext2TtmlUtil {

	static String exe = "tth.exe";
	static String path = "C:/tth_exe/";
	static{
		if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
			
		} else {
			exe = "tth";
			path = "";
		}
	}
	
	public static String tex2Html(String txt) {
		
		try {
			FileOutputStream fos = new FileOutputStream(new File(path + "tmp.tex"));
			fos.write(txt.getBytes());
			fos.close();
			
			Runtime.getRuntime().exec(new String[]{path + exe,path + "tmp.tex"});
			
			Thread.sleep(2000);
			String html = IOUtils.toString(new FileInputStream(new File(path + "tmp.html")));
			html = StringUtils.substringBetween(html, "</title>", "File translated");
			int index = html.lastIndexOf("<hr");
			if(index != -1){
				html = html.substring(0,index);
			}
			while(html.endsWith("<br />")){
				html = html.substring(0, html.length()-6);
			}
			return html;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String input = "允许重复元素，则上一题中如果\\fn{A[m]>=A[l]},那么\\fn{[l,m]}为递增序列的假设就不能成立了，比如\\code{[1,3,1,1,1]}。 \n"+

"如果\\fn{A[m]>=A[l]}不能确定递增，那就把它拆分成两个条件：\n"+
"\\begindot\n"+
"\\item 若\\fn{A[m]>A[l]}，则区间\\fn{[l,m]}一定递增\n"+
"\\item 若\\fn{A[m]==A[l]} 确定不了，那就\\fn{l++}，往下看一步即可。\n"+
"\\myenddot\n";
		System.out.println(input);
		
		System.out.println(tex2Html(input));
	}
	
}
