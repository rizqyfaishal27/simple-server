package webapp.controllers;

import java.io.BufferedOutputStream;

import request.Request;
import response.Response;
import response.FileResponse;

public class BackgroundImageController extends BaseController {

	public BackgroundImageController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new FileResponse("/background.jpg", request, responseStream);
	}
}