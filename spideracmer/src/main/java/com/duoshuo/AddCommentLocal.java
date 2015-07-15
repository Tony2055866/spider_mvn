package com.duoshuo;

import com.model.*;
import com.util.HibernateSessionFactory;
import org.hibernate.Transaction;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;


/**
 * Created by Administrator on 2015/6/28.
 */
public class AddCommentLocal {
    static Random r = new Random();
    static WpCommentsDAO commentsDAO = new WpCommentsDAO();

    static List<String> names;
    public static boolean  running;
    public static int commentLimit = 10;
    public static int cnt = 0;
    public static int all = 0;
    public static void main(String[] args) throws InterruptedException {

        WpPostsDAO postsDAO = new WpPostsDAO();

        //1
        List<String> comments = getAllComments();


        //2
        names = getAllNames();

        //3
        Transaction transaction = HibernateSessionFactory.openCurrentSession().beginTransaction();

        org.hibernate.Query query = HibernateSessionFactory.openCurrentSession().createQuery("From WpPosts  post where " +
                "post.postStatus=? and post.postType=?");
        query.setString(0,"publish");
        query.setString(1, "post");
        List<WpPosts> list = query.list();
        transaction.commit();
        all = list.size();
        running = true;
        for(WpPosts post:list){
            if(running == false) break;

            if(post.getCommentCount() < 15){
                int cnt = r.nextInt( (int) (15 - post.getCommentCount()) );
                for(int i=0; i<cnt; i++){
                    transaction = HibernateSessionFactory.openCurrentSession().beginTransaction();

                    String commentText = comments.get( r.nextInt(comments.size()));
                    WpComments comment = getComment(commentText, post.getPostDate().getTime());
                    comment.setWpPosts(post);
                    commentsDAO.save(comment);
                    transaction.commit();
                }

                if(cnt > 0){
                    //transaction = HibernateSessionFactory.openCurrentSession().beginTransaction();
                    post.setCommentCount(post.getCommentCount() + cnt);
                    postsDAO.save2(post);
                    //transaction.commit();
                }
            }
            cnt++;
            Thread.sleep(300);
        }
        running = false;

    }

    public static WpComments getComment(String text, long postDate){
        WpComments comments = new WpComments();
        comments.setCommentAgent("none");
        comments.setCommentApproved("1");
        comments.setCommentAuthor(names.get(r.nextInt(names.size())));
        comments.setCommentAuthorEmail("hello@acmerblog.com");
        comments.setCommentAuthorIp("127.0.0.1");
        comments.setCommentContent(text);

        //long postDate = System.currentTimeMillis() - 60 * 60 * 1000 * 24 * 30;
        //postDate = post.getPostDate().getTime();
        long x = r.nextLong();
        if(x < 0) x= -x;
        long commentDate = postDate + x% (System.currentTimeMillis() - postDate);

        Timestamp timestamp = new Timestamp(commentDate);
        comments.setCommentDate(timestamp);
        comments.setCommentDateGmt(new Timestamp(commentDate - 60 * 60 * 1000 * 8));
        comments.setCommentType("");

        comments.setCommentParent(0L);
        return comments;
    }


    public static List<String> getAllComments(){
        List<String> comments = new ArrayList<String>();
        InputStream in = AddCommentLocal.class.getClassLoader().getResourceAsStream("comments.txt");
        Scanner s = new Scanner(in);
        while(s.hasNextLine()){
            String line = s.nextLine();
            if(line.trim().length() > 0)
                comments.add(line.trim());
        }
        System.out.println(comments.size());
        List<WpComments> list = commentsDAO.findByCommentApproved("1");
        for(WpComments comm:list){
            comments.add(comm.getCommentContent());
        }
        System.out.println(comments.size());
        return comments;
    }


    public static List<String> getAllNames() {
        List<String> names = new ArrayList<String>();
        WpUsersDAO dao = new WpUsersDAO();
        List<WpUsers> users = dao.findAll();
        for(WpUsers user:users){
            names.add(user.getUserLogin());
        }
        return names;
    }
}
