package webapp.controllers;

import java.io.BufferedOutputStream;

import request.Request;
import response.Response;
import response.RedirectResponse;

public class IndexPageController extends BaseController {

	public IndexPageController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new RedirectResponse("/hello-world", request, responseStream);
	}
}