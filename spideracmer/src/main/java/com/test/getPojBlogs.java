package com.test;

import java.sql.*;


public class getPojBlogs {

	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getPojBlogs.class);


	private static String dbUrl = "jdbc:mysql://127.0.0.1/wordpress";
    public static String port;
    public static String host;
    public static String username="root";
    public static String password="root";
    public static String databaseName = "zJtjKTokkLUoGqQZMBkC";
  
   
  
    static {//注册驱动  
        try {  
            Class.forName("com.mysql.jdbc.Driver");  
        } catch (ClassNotFoundException e) {  
            throw new ExceptionInInitializerError(e);
        }  
    }  

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl,username,password);
    }  

	public static void main(String[] args) {
		try {
			Connection conn = getConnection();
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement("select post_title,post_name,id from wp_posts2test where post_status='publish' and post_type='post'");
			ResultSet set = ps.executeQuery();
			int cnt = 0;
			while(set.next()){
				
				String title = set.getString(1);
				String name = set.getString(2);
				Long id = set.getLong(3);
				if (cnt >= 500 && title.toUpperCase().startsWith("POJ")) {
					logger.info("<a href=\"http://www.acmerblog.com/" + name + "-" + id  + "\">" + title + "</a><br>");
				}
				cnt++;
				//if(cnt >= 500) break;
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
