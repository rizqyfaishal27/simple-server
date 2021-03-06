package webapp.controllers;

import java.io.BufferedOutputStream;

import request.Request;
import response.Response;
import response.RedirectResponse;
import response.FileResponse;

public class IndexPageController extends BaseController {

	public IndexPageController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new FileResponse("/index.html", request, responseStream);
	}
}