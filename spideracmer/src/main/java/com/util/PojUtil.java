package com.util;

import com.sqider.PageData;
import com.sqider.PreTag;
import com.sqider.ProblemData;
import org.htmlparser.tags.Div;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class PojUtil {
	public static String qnamediv = "<div ><h1 class=\"mytitle mybigtile\">";
	public static String qnamediv2="</h1></div>";
	
	public static String titledes = "<div><p class=\"mytitle\"> 问题描述 :</p></div>";
	public static String titleInput = "<div><p class=\"mytitle\"> 输入:</p></div>";
	public static String titledOutput = "<div><p class=\"mytitle\"> 输出:</p></div>";
	public static String titledSamInput = "<div><p class=\"mytitle\"> 样例输入:</p></div>";
	public static String titledSamOutput = "<div><p class=\"mytitle\"> 样例输出:</p></div>";
	public static String titledHint = "<div><p class=\"mytitle\"> 温馨提示:</p></div>";
	public static String titledSource = "<div><p class=\"mytitle\"> 题目来源:</p></div>";

	public static String submitString = "<div class=\"mybigtile mysubmit\"> <a href=\"http://poj.org/submit?problem_id=rrrrr\">提交代码</a>&nbsp;  " +
	"<a href=\"http://poj.org/bbs?problem_id=rrrrr\">问题讨论</a>&nbsp;  " +
	"<a href=\"http://poj.org/problemstatus?problem_id=rrrrr\">数据统计</a> </div>";
	
	public static void main(String[] args) {
//		String des = "<img src=\"images/1046/color.gif\">";
//		des = des.replaceAll("images","http://poj.org/"+ "images");
//		System.out.println(des);
		
		String test = "gao tong 	ni";
		System.out.println(test.replaceAll("\\s+", "+"));
		
		
		try {
			System.out.println(URLEncoder.encode("ni-hao'，", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static String baseDownLoad = "D:\\PHP\\wordpress-3.7-zh_CN\\wordpress\\img\\poj\\";
	public static String baseDownLoad = "/server/wordpress/img/poj/";
//	String myhost = "http://localhost/blog/";
	static String myhost = "http://www.acmerblog.com/";
	
	static{
		if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
			baseDownLoad = "D:\\PHP\\wordpress-3.7-zh_CN\\wordpress\\img\\poj\\";
			myhost = "http://localhost/blog/";
		} else {
			
		}
	}
	///server/wordpress/img/

	
	public static ProblemData getPorblemStr(String problem, boolean ishint) throws Exception{
		ProblemData pdata = new ProblemData();
		
		PageData pd = MyUtil.getPage("http://poj.org/problem?id=" + problem, false);
		try {
			//title
			List<Div> title = MyUtil.parseTags(pd.html, Div.class, "class", "ptt");
			String qName = title.get(0).getStringText();
			
			//Des of problem, Des of Input,  Des of Output, Des of Hint
			List<Div> divs = MyUtil.parseTags(pd.html, Div.class, "class", "ptx");
			String des = divs.get(0).getStringText();
			des = des.replaceAll("images","http://poj.org/"+ "images");
			String desInput=null,desOutput=null;
			if(divs.size() > 2){
				 desInput = divs.get(1).getStringText();
				 desOutput = divs.get(2).getStringText();
			}
			String desSource = null;
			String desHint = null;
			if(divs.size() == 4){
				desSource = divs.get(3).getStringText();
			}else if(divs.size() == 5){
				desHint = divs.get(3).getStringText();
				desSource = divs.get(4).getStringText();
			}
			//images/1046/color.gif
			String baseUrl = "http://poj.org/";


			String refer = "http://poj.org/problem?id="+ problem;
			
			des = des.trim();
			while(des.endsWith("<br>")) des = des.substring(0, des.length()-4);
			//handler the image tags
			des = ImageUtil.modifyImgHtml(des,baseUrl,myhost,  refer,  baseDownLoad + problem + "-", "http://poj.org/" );
			//System.out.println(des);
			
			//input, output
			List<PreTag> input = MyUtil.parseTags(pd.html, PreTag.class, "class", "sio");
			String sampleInput = input.get(0).getStringText();
			String sampleOutput = input.get(1).getStringText();
			//System.out.println(titles.size());
			
			String postText = qnamediv + qName + qnamediv2 + "\n";
			
			//description
			postText += titledes;
			postText += "<div class=\"mypanel\">" + "\n";
			postText += des + " </div>" + "\n";
			
			if(desInput!=null){
			postText+= titleInput;
			postText += "<div class=\"mypanel\">" + "\n";
			postText += desInput + " </div>" + "\n";
			}
			
			if(desOutput!=null){
			postText+= titledOutput;
			postText += "<div class=\"mypanel\">" + "\n";
			postText += desOutput + "</div> " + "\n";
			}
			
			postText+= titledSamInput;
			postText += "<pre class=\"mypanel iopanel\">" + "\n";
			postText += sampleInput + "</pre> " + "\n";
			
			postText+= titledSamOutput;
			postText += "<pre class=\"mypanel iopanel\">" + "\n";
			postText += sampleOutput + "</pre> " + "\n";
			
			if(ishint && desHint != null){
				postText+= titledHint;
				postText += "<div class=\"mypanel\">" + "\n";
				postText += desHint.replaceAll("<br><br>", "\n") + "</div> " + "\n";
				//System.out.println(desHint);
			}
			
			//postText += submitString.replaceAll("rrrrr", problem);
			postText += "<br> <!-- problem end -->";
//			postText+= titledSamOutput;
//			postText += "<pre class=\"mypanel\">" + "\n";
//			postText += sampleOutput + "</pre> " + "\n";
			pdata.text = postText;
			pdata.title = qName;
			return pdata;
		} catch (Exception e) {
			throw e;
		}
		

	}
}
