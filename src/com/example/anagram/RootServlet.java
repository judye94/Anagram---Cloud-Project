package com.example.anagram;

import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class RootServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException, ServletException {
		
		resp.setContentType("text/html");
		
		//access to the google user service
		UserService us = UserServiceFactory.getUserService();
		User u = us.getCurrentUser();
		String login_url = us.createLoginURL("/");
		String logout_url = us.createLogoutURL("/");
		
		//we need to access them in jsp therefore a few things will be attached to the request
		req.setAttribute("user", u);
		req.setAttribute("login_url",  login_url);
		req.setAttribute("logout_url", logout_url);
		
		// get a request dispatcher and launch a jsp that will render our page
	      RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp");
	      rd.forward(req, resp);
	}
}

