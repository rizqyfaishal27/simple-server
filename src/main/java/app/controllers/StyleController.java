package webapp.controllers;

import java.io.BufferedOutputStream;

import request.Request;
import response.Response;
import response.FileResponse;

public class StyleController extends BaseController {

	public StyleController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new FileResponse("/style.css", request, responseStream);
	}
}