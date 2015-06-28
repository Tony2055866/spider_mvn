
package com.hdusubmit;

import java.util.List;

public class Book{
   	private List posts;
   	private List threads;

 	public List getPosts(){
		return this.posts;
	}
	public void setPosts(List posts){
		this.posts = posts;
	}
 	public List getThreads(){
		return this.threads;
	}
	public void setThreads(List threads){
		this.threads = threads;
	}
}
