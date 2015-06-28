package com.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;



public class JdbcUtil {
	 private static String url = "jdbc:mysql://localhost:3306/wordpress";
	 private static String user = "root";
	 private static String password = "root";
	private static String driverName = "com.mysql.jdbc.Driver";

	public JdbcUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	static {
		try {
			Class.forName(driverName);
			Properties pro = new Properties();
			pro.load(JdbcUtil.class.getResourceAsStream("/db.properties"));
			user = pro.getProperty("username");
			password = pro.getProperty("password");
			url = pro.getProperty("url");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public static void close(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null) {
				rs.close();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(st!=null){
					st.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(conn!=null){
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

}
