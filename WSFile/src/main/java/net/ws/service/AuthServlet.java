package net.ws.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthServlet extends HandleCre {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		 if(req.getRequestURI().equals("/auth/file")){
             handleCallbackIfRequired(req, resp);
          }else if(req.getRequestURI().equals("/auth/login")){
             loginIfRequired(req, resp);
          }

	}

	
}