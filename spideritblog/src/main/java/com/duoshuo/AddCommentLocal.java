package com.duoshuo;

import com.model.WpComments;
import com.model.WpCommentsDAO;
import com.model.WpPosts;
import com.model.WpPostsDAO;
import com.util.HibernateSessionFactory;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;


/**
 * Created by Administrator on 2015/6/28.
 */
public class AddCommentLocal {
    static Random r = new Random();
    public static void main(String[] args){

        WpPostsDAO postsDAO = new WpPostsDAO();
        WpPosts post = postsDAO.findById(1113L);
        if(post != null){

            Transaction transaction = HibernateSessionFactory.openCurrentSession().beginTransaction();

            WpCommentsDAO commentsDAO = new WpCommentsDAO();
            WpComments comments = new WpComments();
            comments.setCommentAgent("none");
            comments.setCommentApproved("1");
            comments.setCommentAuthor("hello");
            comments.setCommentAuthorEmail("hello@acmerblog.com");
            comments.setCommentAuthorIp("127.0.0.1");
            comments.setCommentContent("hello, my comment!");

            long postDate = System.currentTimeMillis() - 60 * 60 * 1000 * 24 * 30;
            postDate = post.getPostDate().getTime();

            long commentDate = postDate + r.nextLong() % (System.currentTimeMillis() - postDate);

            Timestamp timestamp = new Timestamp(commentDate);
            comments.setCommentDate(timestamp);
            comments.setCommentDateGmt(new Timestamp(commentDate - 60 * 60 * 1000 * 8));
            comments.setCommentType("");
            comments.setWpPosts(post);

            comments.setCommentParent(0L);

            commentsDAO.save(comments);

            transaction.commit();
        }



    }

}
