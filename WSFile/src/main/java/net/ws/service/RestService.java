package net.ws.service;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;



@Path("/action")
public class RestService extends HandleCre {
	
	@GET
	@Path("/oath/{param}")
	public Response google(@Context HttpServletRequest req,@Context HttpServletResponse resp) throws IOException, ServletException {
		RequestDispatcher disp = req.getRequestDispatcher("/auth/login");
	       disp.forward(req, resp);
	       return null;
	}
	@GET
	@Path("/oath2/{param}")
	public Response dropbox(@Context HttpServletRequest req,@Context HttpServletResponse resp) throws IOException, ServletException {
		RequestDispatcher disp = req.getRequestDispatcher("/filedropbox");
	       disp.forward(req, resp);
	       return null;
	}
	
	@POST
	@Path("/login")
	public void login(@FormParam("username") String username, @FormParam("password") String password, @Context HttpServletResponse response) throws IOException	{
		String output = "Username = "+username +" <br/> Password = " +password;
		
		response.sendRedirect("/file-manager.html");
		//return Response.status(200).entity(output).build();
	}
	

}
