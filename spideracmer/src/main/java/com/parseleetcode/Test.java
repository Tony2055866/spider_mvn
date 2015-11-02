package com.parseleetcode;

import com.util.MyUtil;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;

import java.util.List;

public class Test {
	static Logger logger = Logger.getLogger(Test.class);

	public static void main(String[] args) {
		String text = MyUtil.getPage("https://oj.leetcode.com/problems/binary-tree-preorder-traversal/").html;
		Div div = MyUtil.parseTags(text, Div.class, "class", "question-content").get(0);
		
		String tags = "";
		for(int i =0 ;i < div.getChildren().size(); i++){
			Node node = div.getChildren().elementAt(i);
			if(node instanceof Div && ((Div)node).getAttribute("class").startsWith("btn btn-xs btn-warning")){
				div.removeChild(i);
				//logger.info(node.toHtml());
			}
			if(node instanceof Span && ((Span)node).getAttribute("class").equals("hide")){
				List<LinkTag> links = MyUtil.parseTags(node.toHtml(), LinkTag.class, null, null);
				for(LinkTag link:links){
					tags += "&nbsp;" + link.getStringText();
				}

				div.removeChild(i);
			}
		}
		logger.info(div.toHtml() + "<br> 标签:" + tags);
	}
}
