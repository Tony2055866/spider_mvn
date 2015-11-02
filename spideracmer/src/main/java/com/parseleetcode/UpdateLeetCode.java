package com.parseleetcode;

import com.model.WpPosts;
import com.model.WpPostsDAO;
import org.apache.log4j.Logger;

public class UpdateLeetCode {

	static Logger logger = Logger.getLogger(UpdateLeetCode.class);


	public static void main(String[] args) {
		logger.info("\\fn".replaceAll("\\\\fn", ""));
	}

	public static void update(WpPosts post, String[] codes, String[] items,
			String txt, WpPostsDAO wpdao) {
		 wpdao = new WpPostsDAO();
		
	}
	
}
