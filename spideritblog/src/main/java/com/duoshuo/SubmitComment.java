package com.duoshuo;

import com.google.gson.Gson;
import com.model.WpPostmetaDAO;
import com.model.WpPosts;
import com.util.MyUtil;

public class SubmitComment {

	static String postapi = "http://api.duoshuo.com/posts/create.json";

	public static void main(String[] args) {
		/*WpPosts post =new  WpPosts();
		post.setId(6537L);
		post.setPostTitle("HDU 3569-Imaginary Date[解题报告]HOJ");
		post.setPostName("http://www.acmerblog.com/hdu-3569-imaginary-date-6537.html");*/

		String postsJson =
				MyUtil.getPage("http://gaotong.duoshuo.com/api/threads/" +
						"list.json?short_name=gaotong&order=asc&limit=30&nonce=54a015668d940")
				.html;
		System.out.println(postsJson);
		Gson gons = new Gson();
		PostResult pr = gons.fromJson(postsJson, PostResult.class);



	}

	public boolean postComment(String pid,String comment){

		return true;
	}

	String postArticle(WpPostmetaDAO post){
		String threadid = "";


		return threadid;
	}
}
