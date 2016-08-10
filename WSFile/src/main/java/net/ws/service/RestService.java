package net.ws.service;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import net.ws.googledrive.HandleCre;

@Path("/action")
public class RestService extends HandleCre {
    /**
     * 
     */
    private static final long serialVersionUID = 3484752372851377257L;

    @GET
    @Path("/oath/{param}")
    public Response connectGoogleDrive(@Context HttpServletRequest req, @Context HttpServletResponse resp)
            throws IOException, ServletException {
        RequestDispatcher disp = req.getRequestDispatcher("/auth/login");
        disp.forward(req, resp);
        return null;
    }

    @GET
    @Path("/oath2/{param}")
    public Response connectDropbox(@Context HttpServletRequest req, @Context HttpServletResponse resp)
            throws IOException, ServletException {
        RequestDispatcher disp = req.getRequestDispatcher("/authdropbox");
        disp.forward(req, resp);
        return null;
    }

    @POST
    @Path("/login")
    public void doLogin(@FormParam("username") String username, @FormParam("password") String password,
            @Context HttpServletResponse response) throws IOException {
        response.sendRedirect("/file-manager.html");
    }

}
