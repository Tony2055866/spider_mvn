package com.util;

import com.sqider.PageData;
import com.sqider.PreTag;
import com.sqider.ProblemData;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;

import java.util.ArrayList;
import java.util.List;

public class HojUtil {
	public static String qnamediv = "<div ><h1 class=\"mytitle mybigtile\">";
	public static String qnamediv2="</h1></div>";
	
	public static String titledes = "<div><p class=\"mytitle\"> 问题描述 :</p></div>";
	public static String titleInput = "<div><p class=\"mytitle\"> 输入:</p></div>";
	public static String titledOutput = "<div><p class=\"mytitle\"> 输出:</p></div>";
	public static String titledSamInput = "<div><p class=\"mytitle\"> 样例输入:</p></div>";
	public static String titledSamOutput = "<div><p class=\"mytitle\"> 样例输出:</p></div>";
	public static String titledHint = "<div><p class=\"mytitle\"> 温馨提示:</p></div>";
	public static String titledSource = "<div><p class=\"mytitle\"> 题目来源:</p></div>";
	public static String titledRecommed = "<div><p class=\"mytitle\"> 推荐题目:</p></div>";
	
	public static String submitString = "<div class=\"mybigtile mysubmit\"> <a href=\"http://acm.hdu.edu.cn/submit.php?pid=rrrrr\">提交代码</a>&nbsp;  " +
	"<a href=\"http://acm.hdu.edu.cn/discuss/problem/list.php?problemid=rrrrr\">问题讨论</a>&nbsp;  " +
	"<a href=\"http://acm.hdu.edu.cn/statistic.php?pid=rrrrr\">数据统计</a> </div>";
	
	public static void main(String[] args) {
		try {
			ProblemData pd = getPorblemStr("1065", true);
			//System.out.println(pd.text);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	

	static String myhost = "http://www.acmerblog.com/";
	
	public static ProblemData getPorblemStr(String problem, boolean isDownImg) throws Exception{
		ProblemData pdata = new ProblemData();
		//System.out.println("getPorblemStr:"+problem);
		PageData pd = MyUtil.getPage("http://acm.hdu.edu.cn/showproblem.php?pid=" + problem, false, "gb2312", null, null);
		//System.out.println(pd.html);
		try {
			//title
			List<HeadingTag> title = MyUtil.parseTags(pd.html, HeadingTag.class, "style", null);
			
			String qName = title.get(0).getStringText();
			pdata.title = qName;
			if(!isDownImg)
				return pdata;
			if(qName.trim().toLowerCase().equals("system message")){
				throw new Exception("problem not found!");
			}
		
			//Des of problem, Des of Input,  Des of Output, Sample Input ,Sample Output , Des of Hint
			List<Div> divs = MyUtil.parseTags(pd.html, Div.class, "class", "panel_content");
			//List<Div> titles = MyUtil.parseTags(pd.html, Div.class, "class", "panel_title");
			
			String des = null;
			//des = des.replaceAll("images","http://poj.org/"+ "images");
			String desInput=null,desOutput=null,sampleInput = null,sampleOutput = null;
			
			String baseUrl = "http://acm.hdu.edu.cn/showproblem.php/";

			String refer = "http://acm.hdu.edu.cn/showproblem.php?"+ problem;
			
			 List<String> hasTitles = new ArrayList<String>();
			int cnt = 0;
			 if(pd.html.contains("Problem Description")){
				 //hasTitles.add(des);
				 des =  divs.get(cnt).getStringText();
				 des = des.trim();
				//des = ImageUtil.modeyUrlOnly(des, baseUrl, "http://acm.hdu.edu.cn/");
				if(isDownImg)
				 des = ImageUtil.modifyImgHtml(des, pd);
				 cnt ++;
			 }
			 if(pd.html.contains(">Input<")){
				 //hasTitles.add(desInput);
				 desInput = divs.get(cnt).getStringText();
				 desInput = desInput.trim();
				// desInput = ImageUtil.modeyUrlOnly(desInput, baseUrl, "http://acm.hdu.edu.cn/");
				 if(isDownImg)
				 desInput = ImageUtil.modifyImgHtml(desInput, pd);
				 cnt++;
			 }
			 if(pd.html.contains(">Output<")){
				 desOutput = divs.get(cnt).getStringText();
				 desOutput = desOutput.trim();
				//desOutput = ImageUtil.modeyUrlOnly(desOutput, baseUrl, "http://acm.hdu.edu.cn/");
				 if(isDownImg)
				 desOutput = ImageUtil.modifyImgHtml(desInput, pd);
				 cnt++;
			 }
			 if(pd.html.contains("Sample Input")){
				 sampleInput = ( (Div)divs.get(cnt).getChild(0).getFirstChild() ).getStringText();
				 cnt++;
			 }
			 if(pd.html.contains("Sample Output")){
				// System.out.println(cnt);
				// System.out.println(divs.get(cnt).toHtml());
				 sampleOutput = ( (Div)divs.get(cnt).getChild(0).getFirstChild() ).getStringText();
				 if(isDownImg)
				 sampleOutput = ImageUtil.modifyImgHtml(sampleOutput, pd);
				 cnt++;
			 }
				
			
//			if(divs.size() > 4){
//				divs.get(0).getStringText();
//				 desInput = divs.get(1).getStringText();
//				 desOutput = divs.get(2).getStringText();
//				 //PreTag preInput = 
//				 sampleInput = ( (Div)divs.get(3).getChild(0).getFirstChild() ).getStringText();
//				 sampleOutput = ( (Div)divs.get(4).getChild(0).getFirstChild() ).getStringText();
//			}
			
			 
			/*String desSource = null;
			String desHint = null;
			if(divs.size() == 4){
				desSource = divs.get(3).getStringText();
			}else if(divs.size() == 5){
				desHint = divs.get(3).getStringText();
				desSource = divs.get(4).getStringText();
			}*/
			//images/1046/color.gif
			
			 if(des!=null)
				 while(des.endsWith("<br>")) des = des.substring(0, des.length()-4);
			
			//handler the image tags
			//des = des.replaceAll("\\.\\./", "/");
				
			//des = ImageUtil.modifyImgHtml(des,"http://acm.hdu.edu.cn/showproblem.php/",Init.host,  refer,  Init.baseDownLoad + File.separator  + "hdu-" + problem + "-", "http://acm.hdu.edu.cn/" );
			//System.out.println(des);
			//input, output
			List<PreTag> input = MyUtil.parseTags(pd.html, PreTag.class, "class", "sio");
		
			//System.out.println(titles.size());
			
			String postText = qnamediv + qName + qnamediv2 + "\n";
			
			//description
			if(des!=null){
			postText += titledes;
			postText += "<div class=\"mypanel\">" + "\n";
			postText += des + " </div>" + "\n";
			}
			
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
			
			if(sampleInput!=null){
			postText+= titledSamInput;
			postText += "<pre class=\"mypanel iopanel\">" + "\n";
			postText += sampleInput + "</pre> " + "\n";
			
			}
			if(sampleOutput!=null){
			postText+= titledSamOutput;
			postText += "<pre class=\"mypanel iopanel\">" + "\n";
			postText += sampleOutput + "</pre> " + "\n";
			}
			
			postText += "<br> <!-- problem end -->";
			/*if(ishint && desHint != null){
				postText+= titledHint;
				postText += "<div class=\"mypanel\">" + "\n";
				postText += desHint.replaceAll("<br><br>", "\n") + "</div> " + "\n";
				//System.out.println(desHint);
			}*/
			//postText += submitString.replaceAll("rrrrr", problem);
//			postText+= titledSamOutput;
//			postText += "<pre class=\"mypanel\">" + "\n";
//			postText += sampleOutput + "</pre> " + "\n";
			pdata.text = postText;
			pdata.title = qName;
			
			//System.out.println(postText);
			
			return pdata;
		} catch (Exception e) {
			throw e;
		}
		

	}

//	private static boolean has(List<Div> titles, String string) {
//		for(int i=0; i<titles.size(); i++)
//			if(titles.get(i).toHtml().contains(string)) return true;
//		return false;
//	}
}
