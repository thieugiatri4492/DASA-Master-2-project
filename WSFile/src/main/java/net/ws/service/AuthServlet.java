package net.ws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ws.object.ClientFile;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class AuthServlet extends HandleCre {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {



		System.out.println("Autho-Servlet: " + req.getRequestURI());
		//retrieveAllFiles(service);
	
		 if(req.getRequestURI().equals("/auth/file")){
             handleCallbackIfRequired(req, resp);
          }else if(req.getRequestURI().equals("/auth/login")){
             loginIfRequired(req, resp);
          }

	}

	
}