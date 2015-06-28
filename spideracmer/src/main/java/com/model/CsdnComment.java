package com.model;

public class CsdnComment {
	int ArticleId;
	int BlogId;
	int CommentId;
	String Content;
	int ParentId;
	
	String PostTime;
	String Replies;
	String UserName;
	String Userface;
	public int getArticleId() {
		return ArticleId;
	}
	public void setArticleId(int articleId) {
		ArticleId = articleId;
	}
	public int getBlogId() {
		return BlogId;
	}
	public void setBlogId(int blogId) {
		BlogId = blogId;
	}
	public int getCommentId() {
		return CommentId;
	}
	public void setCommentId(int commentId) {
		CommentId = commentId;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public int getParentId() {
		return ParentId;
	}
	public void setParentId(int parentId) {
		ParentId = parentId;
	}
	public String getPostTime() {
		return PostTime;
	}
	public void setPostTime(String postTime) {
		PostTime = postTime;
	}
	public String getReplies() {
		return Replies;
	}
	public void setReplies(String replies) {
		Replies = replies;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserface() {
		return Userface;
	}
	public void setUserface(String userface) {
		Userface = userface;
	}
	
}
