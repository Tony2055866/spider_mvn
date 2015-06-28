package com.itblog.sqider;

import java.util.List;
import java.util.Set;

public abstract class SearchEngine {
	/**
	 * 
	 * @param keys 搜索的关键词数组
	 * @param page 搜索出的结果条数
	 * @return 搜索的链接的url（可以是百度的间接url或Google的直接url）
	 */
	public abstract List<String> search(String keys[],int page);
	
	/**
	 * 
	 * @param keys
	 * @param page
	 * @param method 是否在检测 网页标题的合法性，检测的话会提前判断是否访问该网页
	 * @return
	 */
	public abstract List<String> search(String keys[],int page, int method);
}
