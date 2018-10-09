package webapp.controllers;

import java.io.BufferedOutputStream;

import request.Request;
import response.Response;
import response.FileResponse;

public class ApiSpesificationController extends BaseController {

	public ApiSpesificationController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new FileResponse("/spesifikasi.yaml", request, responseStream);
	}
}