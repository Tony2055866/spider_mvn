package com.util;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;


public class HttpDownload {

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GBK2UTF.class);

    
    public static final int cache = 10 * 1024;
   public static final boolean isWindows;
   public static final String splash;
   //public static final String root;
   static {
       if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
           isWindows = true;
           splash = "\\";
           //root="D:\\PHP\\wordpress-3.7-zh_CN\\wordpress\\imgs";
       } else {
           isWindows = false;
           splash = "/";
           //root="/server/wordpress/img";
       }
   }


   public static String download(String url) throws Exception {
       return download(url, null, null);
   }


   public static String download(String urlstr, String filepath, String refer) throws Exception {
       //try {
           urlstr = urlstr.replaceAll("\\.\\./", "");

           HttpClient client;
           HttpRequestBase httpget;
           try {
               client = new DefaultHttpClient();
               //logger.info("url:" + url);
                //url = URLEncoder.encode(url);
               URL url = new URL(urlstr);
               URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
               httpget = new HttpGet(uri);
                logger.info("下载:" + uri.getPath());

           } catch (Exception e) {
               e.printStackTrace();
               return null;
           }
           httpget.setHeader("Referer", refer);

           HttpResponse response = client.execute(httpget);
           int statcode =response.getStatusLine().getStatusCode();
       if (statcode != 200){
               logger.info("statcode == " +  statcode + "  bad  imgae url!");
               return null;
           }
           /*HashMap<String, String> headers = new HashMap<String, String>();
           headers.put("Referer", refer);
           headers.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.6) Gecko/20100625 Firefox/3.6.6 Greatwqs");
           headers.put("Accept-Language","zh-cn,zh;q=0.5");*/


           HttpEntity entity = response.getEntity();


           InputStream is = entity.getContent();
           //logger.info("path1 : " + filepath);
           //if (filepath == null)
           if(filepath.endsWith("\\") ||filepath.endsWith("/"))
               filepath += getFileName(response, urlstr);
           else
               filepath += File.separator + getFileName(response, urlstr);
           //else{
               /*Header[] headers = response.getHeaders("Content-Type");
               String type = headers[0].getValue();
               if(type != null){
                   if(type.endsWith("png"))
                       filepath += ".png";
                   else if(type.endsWith("jpeg"))
                       filepath += ".jpg";
                   else if(type.endsWith("gif"))
                       filepath += ".gif";
               }*/

           //}
           logger.info("下载至：" + filepath);
           File file = new File(filepath);
           if(file.exists()){
               return new File(filepath).getName();
           }

           file.getParentFile().mkdirs();
           FileOutputStream fileout = new FileOutputStream(file);


           byte[] buffer=new byte[cache];
           int ch = 0;
           while ((ch = is.read(buffer)) != -1) {
               fileout.write(buffer,0,ch);
           }
           is.close();
           fileout.flush();
           fileout.close();

//		} catch (Exception e) {
//			e.printStackTrace();
//		}
       return new File(filepath).getName();
   }

//	public static String getImgName(HttpResponse response) {
//		//String filepath = root + splash;
//		String filename = getFileName(response);
//
//	}

   public static String getFileName(HttpResponse response, String url) {

       Header contentHeader = response.getFirstHeader("Content-Disposition");
       String filename = null;
       if (contentHeader != null) {
           HeaderElement[] values = contentHeader.getElements();
           if (values.length == 1) {
               NameValuePair param = values[0].getParameterByName("filename");
               if (param != null) {
                   try {
                       filename = param.getValue();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           }
       }

       String endString = url.substring(url.lastIndexOf('.')).toLowerCase();
       if(!endString.contains("jpg") && !endString.contains("png") && !endString.contains("gif")) endString = "jpg";

       Header[] headers = response.getHeaders("Content-Type");
       String type = "jpeg";
       if(headers != null && headers.length > 0)
            type = headers[0].getValue().toLowerCase();
       //logger.info(headers[0].getValue());
       //logger.info("test file and type: " + filename + " " +  type + "  " + endString);

//		if (filename != null) {
//			//filepath += filename;
//			if(type.endsWith("png") && !filename.toLowerCase().endsWith("png"))
//				filename += ".png";
//			else if(type.endsWith("jpeg") && !filename.toLowerCase().endsWith("jpg"))
//				filename += ".jpg";
//			else if(type.endsWith("gif") && !filename.toLowerCase().endsWith("gif"))
//				filename += ".gif";
//			else
//				filename += endString;
//		} else {
           filename = getRandomFileName();
           if(type.endsWith("png"))
               filename += ".png";
           else if(type.endsWith("jpeg"))
               filename += ".jpg";
           else if(type.endsWith("gif"))
               filename += ".gif";
           else
               filename += endString;
//		}

       return filename;
   }

   public static String getRandomFileName() {
       return String.valueOf(System.currentTimeMillis());
   }
   public static void outHeaders(HttpResponse response) {
       Header[] headers = response.getAllHeaders();
       for (int i = 0; i < headers.length; i++) {
           logger.info(headers[i]);
       }
   }
   public static void main(String[] args) {
//		String url = "http://bbs.btwuji.com/job.php?action=download&pid=tpc&tid=320678&aid=216617";
       String url = "http://acm.hdu.edu.cn/../../data//images/1052-1.gif";
       logger.info(url.replaceAll("\\.\\./", "").replaceAll("//", "/"));

//		String filepath = "D:\\test\\a.torrent";
       //String filepath = "D:\\test\\a.jpg";
//String refer = "http://poj.org/problem?id=1191";
       //HttpDownload.download(url,"D:\\PHP\\wordpress-3.7-zh_CN\\wordpress\\img\\hdu-2921-","");
   }
}
