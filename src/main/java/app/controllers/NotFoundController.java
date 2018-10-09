package webapp.controllers;

import java.io.BufferedOutputStream;
import java.util.HashMap;


import request.Request;
import response.Response;
import response.TextResponse;
import httpstatus.HttpStatusCode;
import response.JsonResponse;


public class NotFoundController extends BaseController {

	public NotFoundController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("detail", "The requested URL was not found on the server.  If you entered the URL manually please check your spelling and try again.");
		data.put("status", Integer.toString(HttpStatusCode.NOT_FOUND.getCode()));
		data.put("title", HttpStatusCode.NOT_FOUND.getDescription());
		return new JsonResponse(data, HttpStatusCode.NOT_FOUND, request, responseStream);
	}
}