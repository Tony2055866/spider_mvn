package com.test;

import com.model.WpOptions;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by gaotong1 on 2015/11/10.
 */
public class FixBlankBlogs {
    
    public static void main2(String args[]){
        String content = StringUtils.substringBetween("abc", "problem end -->", "参考");
        System.out.println(content);
    }
    
    public static void main(String args[]){

        WpPostsDAO wpPostsDAO = new WpPostsDAO();
     // List<WpPosts> posts = wpPostsDAO.findAll();
       Iterator<WpPosts> iter = wpPostsDAO.getSession().createQuery("from WpPosts").iterate();
        
        while (iter.hasNext()){
//            WpPosts post = posts.get(i);
            WpPosts post = iter.next();
            if(post.getPostType().equals("post") && post.getPostStatus().equals("publish") && 
                    !post.getPostTitle().contains("待解决")){
                String content = StringUtils.substringBetween(post.getPostContent(), "problem end -->", "参考");

                String url = "";
                if(content != null && content.trim().isEmpty()){
                    System.out.println("start check post:" + post.getId());

                    int urlIndex = post.getPostContent().lastIndexOf("参考：");
                    if(urlIndex != -1 ){
                         url = post.getPostContent().substring(urlIndex);
                        if(url.contains("http")){
                            url = url.substring(url.indexOf("http"));
                        }
                    }

                    
                    if(url.isEmpty()) {
                        System.out.println("not found url");
                        continue;
                    }

                    System.out.println("post id:" + post.getId() + "  url:" + url);
                }

            }
        }
    }
}
