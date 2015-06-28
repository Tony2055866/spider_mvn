package com.sevlets;


import com.util.Init;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Initservlet extends HttpServlet {

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws javax.servlet.ServletException if an error occurs
	 */
	public void init() throws ServletException {
		Init.init(false);
	}

}
