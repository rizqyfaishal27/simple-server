package webapp.controllers.test;

import java.io.BufferedOutputStream;

import request.Request;
import response.Response;
import response.FileResponse;

import webapp.controllers.BaseController;

public class ListHostController extends BaseController {

	public ListHostController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new FileResponse("/list.json", request, responseStream);
	}
}