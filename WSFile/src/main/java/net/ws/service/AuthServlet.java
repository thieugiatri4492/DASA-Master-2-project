package net.ws.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ws.googledrive.HandleCre;

public class AuthServlet extends HandleCre {

    /**
     * 
     */
    private static final long serialVersionUID = -8640045766092241690L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/auth/file")) {
            handleCallbackIfRequired(req, resp);
        } else if (req.getRequestURI().equals("/auth/login")) {
            loginIfRequired(req, resp);
        } else {
            System.err.println("404 not found");
        }
    }

}