package com.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

import com.model.WpComments;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.model.WpTermTaxonomy;
import com.model.WpTermTaxonomyDAO;
import com.util.ItblogInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoTest extends TestCase{
	WpPostsDAO pdao = new WpPostsDAO();
	private static Logger logger = LoggerFactory.getLogger(DaoTest.class);

	public void testAddarticle(){
//		WpPosts artile = new WpPosts();
//		artile.set
//		pdao.save(transientInstance);
	}
	
	public void testGetCategorys(){
		/*WpTermTaxonomyDAO wtdao = new WpTermTaxonomyDAO();
		WpTermsDAO termDao = new WpTermsDAO();
		
		List<WpTermTaxonomy> list = wtdao.findByTaxonomy("category");
		logger.info(list.size());
	
		List<WpTerms> cateTerms = new ArrayList<WpTerms>(list.size());
		for(int i=0; i<list.size(); i++){
			WpTermTaxonomy taxonomy = list.get(i);
			WpTerms term = termDao.findById(taxonomy.getTermId());
			cateTerms.add(term);
			logger.info(term.getName() + "  " + term.getSlug());
		}*/
	}
	
	public void testTest(){
		/*try {
			logger.info(URLDecoder.decode("bfs-%e5%ae%bd%e5%ba%a6%e4%bc%98%e5%85%88-%e5%b9%bf%e5%ba%a6%e4%bc%98%e5%85%88","utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void testInsertArticle(){
//		WpPosts post = new WpPosts();
//		WpTerms term1= new WpTerms();
//		term1.setTermId(21l);
		
		WpPostsDAO pdao = new WpPostsDAO();
//		WpPosts post = pdao.findById(177l);
		
		WpPosts post = new WpPosts();
		
		post.setPostAuthor(1L);
		post.setPostDate(new Timestamp(new Date().getTime()));
		post.setPostDateGmt(new Timestamp(new Date().getTime()));
		post.setPostContent("abc" + System.currentTimeMillis());
		post.setPostTitle("new Title");
		post.setPostExcerpt(post.getPostTitle());
		post.setPostStatus("publish");
		post.setCommentStatus("open");
		post.setPingStatus("open");
		post.setPostName(System.currentTimeMillis()+"");
		post.setToPing("");
		post.setPinged("");
		post.setPostModified(new Timestamp(new Date().getTime()));
		post.setPostModifiedGmt(new Timestamp(new Date().getTime() -  60 * 8 * 60 * 1000));
		post.setPostContentFiltered("");
		post.setPostParent(0L);
		post.setPostPassword("");
		post.setGuid("");
		post.setPostMimeType("");
		
		post.setMenuOrder(0);
		post.setPostType("post");
		post.setCommentCount(0L);
		
		
		
		
		WpTermTaxonomyDAO wtdao = new WpTermTaxonomyDAO();
		
		WpTermTaxonomy termtax01 =wtdao.findById(21L);
		
		//logger.info(termtax01.getTerm().getName());
		
		WpTermTaxonomy termtax02 =wtdao.findById(62L);
		
		WpTermTaxonomy termtax03 =wtdao.findById(41L);
		Set<WpComments> comments = getCommets(post);
		post.setWpCommentses(comments);
		pdao.save(post);
		
		post.setGuid(ItblogInit.host + "?p=" + post.getId());
		post.getTerms().add(termtax01);
		post.getTerms().add(termtax02);
		post.getTerms().add(termtax03);
		
		post.setCommentCount((long)comments.size());
		//comments.clear();
		
		pdao.save(post);
//		WpCommentsDAO comDao = new WpCommentsDAO();
//		for(WpComments com:comments){
//			comDao.save(com);
//			logger.info(com.getCommentContent());
//		}
		
	}

	private Set<WpComments> getCommets(WpPosts post) {
		Set<WpComments> coms = new HashSet<WpComments>();
		
		Random random = new Random();
		
		WpComments comments = new WpComments();
		//comments.setCommentPostId(post.getId());
		comments.setCommentContent("你好 评论1");
		comments.setCommentDate(new Timestamp(new Date().getTime() - random.nextInt(10000)));
		comments.setCommentDateGmt( new Timestamp(comments.getCommentDate().getTime() -  60 * 8 * 60 * 1000) );
		
		WpComments comments2 = new WpComments();
		//comments2.setCommentPostId(post.getId());
		comments2.setCommentContent("你好 评论2");
		comments2.setCommentDate(new Timestamp(new Date().getTime() - random.nextInt(10000)));
		comments2.setCommentDateGmt( new Timestamp(comments.getCommentDate().getTime() -  60 * 8 * 60 * 1000) );
		
		comments2.setWpPosts(post);
		comments.setWpPosts(post);
		
		coms.add(comments);
		coms.add(comments2);
		return coms;
	}
	
}
