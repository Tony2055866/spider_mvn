package com.sevlets;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.util.ItblogInit;

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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		ItblogInit.init(false);
	}

}
