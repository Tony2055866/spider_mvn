
package com.hdusubmit;

import java.util.List;

public class Threads{
   	private String author_key;
   	private String content;
   	private String thread_key;
   	private String title;
   	private String url;

 	public String getAuthor_key(){
		return this.author_key;
	}
	public void setAuthor_key(String author_key){
		this.author_key = author_key;
	}
 	public String getContent(){
		return this.content;
	}
	public void setContent(String content){
		this.content = content;
	}
 	public String getThread_key(){
		return this.thread_key;
	}
	public void setThread_key(String thread_key){
		this.thread_key = thread_key;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
 	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		this.url = url;
	}
}
