package com.test;

import com.util.MyUtil;
import org.apache.commons.io.IOUtils;
import org.htmlparser.Node;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

import java.io.FileInputStream;
import java.lang.Character.UnicodeBlock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
//	static String file = "D://test.txt";
//	static String file2 = "D://test2.txt";
//	static{
//		if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
//			
//		} else {
//			file = "/server/svndown/test.txt";
//			file2 = "/server/svndown/test2.txt";
//		}
//	}
	public static void main(String[] args) throws Exception {
		/*PageData pd =  MyUtil.getPage("http://www.java3z.com/cwbwebhome/article/article17/acm160.html", false,"gb2312");
		System.out.println(pd.html);
		IOUtils.write(pd.html, FileUtils.openOutputStream(new File(file)));
		pd =  MyUtil.getPage("http://poj.org/problem?id=1061", false);
		IOUtils.write(pd.html, FileUtils.openOutputStream(new File(file2)));
		System.out.println(pd.html);*/

		
		//List<PreTag> codes = MyUtil.parseTags(testHtml, PreTag.class, "class", null, true);
		//System.out.println(codes.get(0).getStringText());
		
		
		//System.out.println(codes.size());
		/*for(int i=0; i<codes.size(); i++){
			Node n = list.elementAt(i);
			System.out.println(n);
		}*/
      
		//System.out.println(org.replaceAll("href=\"http://s3\\.51cto\\.com.+\">", ">"));
				//replaceAll("href=\"http://s3\\.51cto\\.com.+\">", ">"));
		
		//测试下载图片
		/*String result = ImageUtil.modifyImgHtml(testHtml,
				"" , null,  "",  Init.baseDownLoad + 1223 + "-", "http://blog.csdn.net/" );
		System.out.println(result);*/
		//测试H1标签
		/*List<HeadingTag> codes = MyUtil.parseTags(testHtml, HeadingTag.class, "style", null, true);
		System.out.println(codes.size());*/
		
		//测试标题
	/*	List<Span> spans = MyUtil.parseTags(testHtml, Span.class, "class", "link_title");
		
		String str = spans.get(0).getStringText();*/
		
//		testRegx();
		String utf8str = "class Solution:\\u000D\\u000A    # @return an integer\\u000D\\u000A";
		byte[] utf8 = utf8str.getBytes("UTF-8");
		System.out.println(unescape(utf8str));
		
	}
	static String unescape(String s) {
	    int i=0, len=s.length();
	    char c;
	    StringBuffer sb = new StringBuffer(len);
	    while (i < len) {
	        c = s.charAt(i++);
	        if (c == '\\') {
	            if (i < len) {
	                c = s.charAt(i++);
	                if (c == 'u') {
	                    // TODO: check that 4 more chars exist and are all hex digits
	                    c = (char) Integer.parseInt(s.substring(i, i+4), 16);
	                    i += 4;
	                } // add other cases here as desired...
	            }
	        } // fall through: \ escapes itself, quotes any character but u
	        sb.append(c);
	    }
	    return sb.toString();
	}
	 public static String utf8ToUnicode(String inStr) {
	        char[] myBuffer = inStr.toCharArray();
	        
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < inStr.length(); i++) {
	         UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
	            if(ub == UnicodeBlock.BASIC_LATIN){
	             //英文及数字等
	             sb.append(myBuffer[i]);
	            }else if(ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS){
	             //全角半角字符
	             int j = (int) myBuffer[i] - 65248;
	             sb.append((char)j);
	            }else{
	             //汉字
	             short s = (short) myBuffer[i];
	                String hexS = Integer.toHexString(s);
	                String unicode = "\\u"+hexS;
	             sb.append(unicode.toLowerCase());
	            }
	        }
	        return sb.toString();
	    }
	 
	public static void testNode() throws Exception{
		String testHtml = IOUtils.toString(new FileInputStream("D:\\workspace\\ACMER\\spiderweb\\src\\tttt.html"));

		//测试代码
		//String testHtml = IOUtils.toString(new FileInputStream("E:\\Workspaces\\ACMER\\spider-hib\\src\\test.txt"));
		//System.out.println(testHtml);
		NodeList list = MyUtil.parseAllTags(testHtml);
		for(int i=0; i<list.size(); i++){
			Node n = list.elementAt(i);
			if(n instanceof ImageTag){
				ImageTag img = (ImageTag)n;
				img.setAttribute("src","nihao");
				System.out.println(img.toHtml());
			}
			
		}
	}
	
	public static void testRegx(){
		String allString = "<a href=\"http://baike.baidu.com/view/461750.htm\" class=\"nhao\" fdsf>";
		allString = allString.replaceAll("href=\"http://.+?\"", "");
		allString = allString.replaceAll("class=\".+?\"", "");
		System.out.println(allString);
		String org = "<a href=\"http://s3.51cto.com/wyfs02/M02/1D/3D/wKioL1MYKSXhpQ2gAAEpRR0S2BI482.jpg\">fdsf </a> fdsf" +
		"<a href=\"http://s3.51cto.com/wyfs02/M02/1D/3D/aadfessss.jpg\">fsdf </a>";
		System.out.println(org.replaceAll("href=\"http://s3\\.51cto\\.com.+?\"", ""));
		  Pattern pattern=Pattern.compile("href=\"http://s3\\.51cto\\.com.+?\""); 
		    Matcher m=pattern.matcher(org); //除中文不用外，其他的都要 
		       if(m.matches() ){ 
		    	   System.out.println(m.find()); 
		    	   System.out.println(m.start()); 
		         System.out.println(m.end()); 
		         System.out.println(m.group()); 

		       } 
	}
}
