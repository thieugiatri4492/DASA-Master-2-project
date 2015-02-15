package net.ws.restfile;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/action")
public class RestService extends HttpServlet {
	@GET
	@Path("/hello/{param}")
	public Response getMsg(@PathParam("param") String msg) {
		String output = "Jersey say : " + msg;
		return Response.status(200).entity(output).build();
	}

	@POST
	@Path("/login")
	public void login(@FormParam("username") String username, @FormParam("password") String password, @Context HttpServletResponse response) throws IOException	{
		String output = "Username = "+username +" <br/> Password = " +password;
		
		response.sendRedirect("/file-manager.html");
		//return Response.status(200).entity(output).build();
	}
	
	/*
	 * public void doGet(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException {
	 * 
	 * PrintWriter out = response.getWriter(); out.println(
	 * "Check the servlet\n" ); out.flush(); out.close(); }
	 */

}
