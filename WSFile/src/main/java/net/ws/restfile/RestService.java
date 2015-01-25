package net.ws.restfile;

import javax.servlet.http.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.*;
import javax.servlet.*;

@Path("/hello")
public class RestService extends HttpServlet {
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey say : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}
	/*public void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
			
			PrintWriter out = response.getWriter();
			out.println( "Check the servlet\n" );
			out.flush();
			out.close();
			 }*/
	 
}
