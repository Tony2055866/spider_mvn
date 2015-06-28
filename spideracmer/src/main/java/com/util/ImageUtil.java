package com.util;

import com.main.Main;
import com.sqider.PageData;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

public class ImageUtil {
	
	//static String downLoadPaht ="D:\\PHP\\wordpress-3.7-zh_CN\\wordpress\\imgs\\poj\\";
	/**
	 * html:html
	 * baseUrl: 当前页面url
	 * myhost: 图片地址. /img/
	 * refer: Init.baseDownLoad
	 * downLoadPath: 下载的 硬盘地址
	 * orgHost：page.host
	 */
	public static String modifyImgHtml(String html,String baseUrl,String myhostImgBaseUrl,String refer,String downLoadPath, String orgHost) throws Exception{
		StringBuffer sb = new StringBuffer();
		//try {
			Parser parser = new Parser();
			parser.setInputHTML(html);
			NodeList nodeList = parser.parse(new NodeFilter() {
				@Override
				public boolean accept(Node node) {
					return true;
				}
			});
			//List<ImageTag> imgsList = MyUtil.parseTags(html, ImageTag.class, "src", null);
			for(int i=0; i<nodeList.size(); i++){
				Node node = nodeList.elementAt(i);
				if(node instanceof ImageTag){
					ImageTag img = (ImageTag)node;
					String originUrl = img.getAttribute("src");
					if(originUrl == null || originUrl.trim().equals("")) continue;
//					baseUlr 值当前页面的url
					baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/')+1);
					
					String realUrl = "";
					originUrl = originUrl.replaceAll("\\\\", "/");
					
					if(!orgHost.endsWith("/")) orgHost+="/";
					
					//说明是从host开始
					if(originUrl.startsWith("/")){
						originUrl =  originUrl.substring(1);
						realUrl = orgHost + originUrl;
					}else
						realUrl = baseUrl + originUrl;
					if(originUrl.startsWith("http://"))
						realUrl = originUrl;
					
					String downLoadName = HttpDownload.download(realUrl , downLoadPath, refer);
					//System.out.println("保存图片:" + downLoadPath + i);
					//src="http://s3.51cto.com/wyfs02/M01/22/4C/wKioL1MaD4byGkhoAAI2j9TUy2E154.jpg" title="2222.jpg"
//System.out.println("img.setAttribute:" + myhostImgBaseUrl  +downLoadName);
					img.removeAttribute("onload");
					img.setAttribute("src", myhostImgBaseUrl  +downLoadName);
					if(Main.proData != null)
						img.setAttribute("alt", Main.proData.title);
					sb.append(img.toHtml());
				}else if(node instanceof TextNode){
					sb.append(node.getText());
				}else{
					sb.append("<" + node.getText() + ">");
				}
			}
			
		//} catch (Exception e) {
			// TODO: handle exception
		//	throw e;
		//}
		
		return sb.toString();
	}
	
	public static String modifyImgHtml(String html, PageData page) throws Exception{
		return modifyImgHtml(html,
				page.url,  //当前下载页面的url
				Init.host + "img/",  //host后面加上
				page.url, //refer 
				Init.baseDownLoad, //下载的目录
				page.host // 当前页面的根域名
				);
		
	}
	
	public static String modeyUrlOnly(String des, String baseUrl, String orgHost) throws Exception {
		StringBuffer sb = new StringBuffer();
		
			Parser parser = new Parser();
			parser.setInputHTML(des);
			
			NodeList nodeList = parser.parse(new NodeFilter() {
				@Override
				public boolean accept(Node node) {
					// TODO Auto-generated method stub
					return true;
				}
			});
			for(int i=0; i<nodeList.size(); i++){
				Node node = nodeList.elementAt(i);
				if(node instanceof ImageTag){
					ImageTag img = (ImageTag)node;
					String originUrl = img.getAttribute("src");
					if(originUrl == null) continue;
					
					String realUrl = "";
					originUrl = originUrl.replaceAll("\\\\", "/");
					
					//说明是从host开始
					if(originUrl.startsWith("/")){
						originUrl =  originUrl.substring(1);
						realUrl = orgHost + originUrl;
					}else
						realUrl = baseUrl + originUrl;
					if(originUrl.startsWith("http://"))
						realUrl = originUrl;
					
					img.setAttribute("src", realUrl);
					sb.append(img.toHtml());
				}else if(node instanceof TextNode){
					sb.append(node.getText());
				}else{
					sb.append("<" + node.getText() + ">");
				}
			}
			
		
		
		return sb.toString();
	}
	public static void main(String[] args) {
		
//		System.out.println("aa\\img".replaceAll("\\\\", "/"));
		//System.out.println("../../../data/images/con208-1004-2.JPG".replaceAll("\\.\\./", "/").replaceAll("", replacement));
	}
}
