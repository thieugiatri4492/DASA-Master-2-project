package net.ws.service;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ws.object.ClientFile;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class AuthServlet extends HandleCre {
	private String downloadFileContent(Drive service, File file)
			throws IOException {
		GenericUrl url = new GenericUrl(file.getDownloadUrl());
		HttpResponse response = service.getRequestFactory()
				.buildGetRequest(url).execute();
		try {
			return new Scanner(response.getContent()).useDelimiter("\\A")
					.next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Drive service = getDriveService(getCredential(req, resp));
		String fileId = req.getParameter("file_id");

		if (fileId == null) {
			sendError(resp, 400,
					"The `file_id` URI parameter must be specified.");
			return;
		}

		File file = null;
		try {
			file = service.files().get(fileId).execute();
		} catch (GoogleJsonResponseException e) {
			if (e.getStatusCode() == 401) { // The user has revoked our token or
											// it is otherwise bad. //Delete the
											// local copy so that their next
											// page load will recover.
				deleteCredential(req, resp);
				sendGoogleJsonResponseError(resp, e);
				return;
			}
		}

		if (file != null) {
			String content = downloadFileContent(service, file);
			if (content == null) {
				content = "";
			}
			sendJson(resp, new ClientFile(file, content));
		} else {
			sendError(resp, 404, "File not found");
		}

		System.out.println("Autho-Servlet: " + req.getRequestURI());

		/*
		 * if (req.getRequestURI().equals("/auth")) { loginIfRequired(req,
		 * resp); } else if (req.getRequestURI().equals("/svc")) {
		 * handleCallbackIfRequired(req, resp); }
		 */

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Drive service = getDriveService(getCredential(req, resp));
		ClientFile clientFile = new ClientFile(req.getReader());
		File file = clientFile.toFile();

		if (!clientFile.content.equals("")) {
			file = service
					.files()
					.insert(file,
							ByteArrayContent.fromString(clientFile.mimeType,
									clientFile.content)).execute();
		} else {
			file = service.files().insert(file).execute();
		}
		sendJson(resp, file.getId());
	}
}