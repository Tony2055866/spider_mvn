package com.util;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

import com.itblog.sqider.PageData;
import com.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtil {
	static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
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
					try{
						String downLoadName = HttpDownload.download(realUrl , downLoadPath, refer);
						String fullPath = downLoadPath;
						if(!downLoadPath.endsWith(File.separator)) fullPath += File.separator;
						fullPath += downLoadName;
						File file = new File(fullPath);
						BufferedImage sourceImg =ImageIO.read(new FileInputStream(file));
						//设置大图属性
						if(sourceImg.getWidth() > 650){
							img.setAttribute("width", "650");
							img.setAttribute("height", "" + sourceImg.getHeight()*650/sourceImg.getWidth());
						}
						//System.out.println("保存图片:" + downLoadPath + i);
						//src="http://s3.51cto.com/wyfs02/M01/22/4C/wKioL1MaD4byGkhoAAI2j9TUy2E154.jpg" title="2222.jpg"
//System.out.println("img.setAttribute:" + myhostImgBaseUrl  +downLoadName);
						img.removeAttribute("onload");
						img.setAttribute("src", myhostImgBaseUrl  +downLoadName);
						if(Main.proData != null)
							img.setAttribute("alt", Main.proData.title);
						sb.append(img.toHtml());
					}catch (Exception e){
						logger.error("down img error : " + realUrl, e);
						e.printStackTrace();
					}

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
				ItblogInit.host + "img/",  //host后面加上
				page.url, //refer 
				ItblogInit.imgbaseDownLoad, //下载的目录
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
		File file = new File("C:\\Users\\gaotong1\\Pictures\\gaotong1.png");
		BufferedImage sourceImg = null;
		try {
			sourceImg = ImageIO.read(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(String.format("%.1f",file.length()/1024.0));
		System.out.println(sourceImg.getWidth());
		System.out.println(sourceImg.getHeight());
		//		System.out.println("aa\\img".replaceAll("\\\\", "/"));
		//System.out.println("../../../data/images/con208-1004-2.JPG".replaceAll("\\.\\./", "/").replaceAll("", replacement));
	}
}
