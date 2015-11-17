package com.test;

import com.main.Main;
import com.main.Util;
import com.model.WpOptions;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.sqider.Content;
import com.sqider.PageData;
import com.sqider.Spider;
import com.util.ImageUtil;
import com.util.MyUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaotong1 on 2015/11/10.
 */
public class FixBlankBlogs {
    
    public static void main2(String args[]){
        
        WpPostsDAO wpPostsDAO = new WpPostsDAO();
        wpPostsDAO.findById(5481L);
    }
    
    public static void main(String args[]){

        String text = "<!-- problem end -->\n" +
                "参考";

        System.out.println(StringUtils.substringBetween(text, "problem end", "参考"));
        if(false) return;
        WpPostsDAO wpPostsDAO = new WpPostsDAO();
     // List<WpPosts> posts = wpPostsDAO.findAll(); from WpPosts
        Session session = wpPostsDAO.getSession();
       Iterator<WpPosts> iter = session.createQuery("from WpPosts").iterate();
        
        while (iter.hasNext()){
//            WpPosts post = posts.get(i);
            WpPosts wpPosts = iter.next();
            /*try {
                wpPosts.setPostContent(new String(wpPosts.getPostContent().getBytes(),"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(wpPosts.getPostContent());
        if(true) break;*/
            if(wpPosts.getPostType().equals("post") && wpPosts.getPostStatus().equals("publish") && 
                    !wpPosts.getPostTitle().contains("待解决")){
                //System.out.println("start check post:" + post.getId());
                //System.out.println(post.getPostContent());
                String tmpContent = wpPosts.getPostContent();
                try {
                     tmpContent = new String(wpPosts.getPostContent().getBytes(),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                
                String content = StringUtils.substringBetween(tmpContent, "<!-- problem end -->", "参考");
                //System.out.println("content:" + content);
               // System.out.println(post.getPostContent().indexOf("problem end") + " " + post.getPostContent().indexOf("参考") + " " + post.getPostContent().length());
                String url = "";
                if(content != null && content.trim().isEmpty()){

                    int urlIndex = tmpContent.lastIndexOf("参考：");
                    if(urlIndex != -1 ){
                         url = tmpContent.substring(urlIndex);
                        if(url.contains("http")){
                            url = url.substring(url.indexOf("http"));
                        }
                    }
                    
                    if(url.isEmpty()) {
                        System.out.println("not found url");
                        continue;
                    }

                   PageData pageData = MyUtil.getPage(url, false);
                    //System.out.println(post.getPostTitle().split("-")[0].split(" "));
                    System.out.println(pageData);
                    Spider spider = Util.getSpiderByUrl(pageData.host);
                    System.out.println(spider + " : " + url);

                    wpPosts.setPostTitle(wpPosts.getPostTitle().toLowerCase());
                    WpPosts post = spider.parseArticleSUrl(pageData, wpPosts.getPostTitle().split("-")[0].split(" "), false);

                    String finalText = "";
                    try {
                        if(post.listContent.size() == 1 && post.hasPro){
                            Content content1 = post.listContent.get(0);
                            if(post.pageData != null)
                                finalText += ImageUtil.modifyImgHtml(content1.text, post.pageData);
                            else
                                finalText += ImageUtil.modifyImgHtml(content1.text, new PageData(post.host,post.url));

                            wpPosts.setPostContent(finalText);
                        }else{

                            for (int i = 0; i < post.listContent.size(); i++) {
                                Content content1 = post.listContent.get(i);
                                if (content == null || content1.text == null || content1.text == "") continue;
                                if (!content1.isCode) {
                                    content1.text = content1.text.replaceAll("href=\"http://.+?\"", "");
                                    content1.text = content1.text.replaceAll("class=\"brush", "xxxxxbrush");
                                    content1.text = content1.text.replaceAll("class=\".+?\"", "");
                                    content1.text = content1.text.replaceAll("xxxxxbrush", "class=\"brush");
                                    //	logger.info(content.text);
                                    //post.hasPro 说明文章部分含有问题，则不插入该博客的文字部分 (163 新浪等除外)
                                    if (!post.hasPro || !post.hasCode) {
                                        if (post.pageData != null)
                                            finalText += ImageUtil.modifyImgHtml(content1.text, post.pageData);
                                        else
                                            finalText += ImageUtil.modifyImgHtml(content1.text, new PageData(post.host, post.url));
                                    }
                                } else {
                                    finalText += "<pre class=\"brush:" + content1.lang + " \">";
                                    finalText += content1.text.trim();
                                    finalText += "</pre>";
                                }
                            }
                            
                            
                            int m = tmpContent.lastIndexOf("参考");
                            String pro = tmpContent.substring(0, m);
                            pro += finalText;
                            wpPosts.setPostContent(pro);
                            wpPosts.setPostContent(new String(pro.getBytes(),"gbk"));

                            System.out.println("--------------------");
//                            System.out.println(pro);
                        }
                       // System.out.println(wpPosts.getPostContent());
                        session.update(wpPosts);
                        session.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("post id:" + wpPosts.getId() + "  url:" + url);
                }

            }
        }
    }
}
