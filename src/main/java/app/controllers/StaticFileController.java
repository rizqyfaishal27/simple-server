package webapp.controllers;

import request.Request;
import response.Response;
import java.io.BufferedOutputStream;

import utils.AppConfig;
import response.FileResponse;

public class StaticFileController extends BaseController {
	private String fileName;

	public StaticFileController(String fileName, Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
		this.fileName = fileName;
	}

	public Response createResponse() {
		return new FileResponse(this.fileName, request, responseStream);
	}
}