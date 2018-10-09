package webapp.controllers;

import java.io.BufferedOutputStream;
import java.io.IOException;

import response.Response;
import request.Request;

public abstract class BaseController {
	protected Request request;
	protected BufferedOutputStream responseStream;

	public BaseController(Request request, BufferedOutputStream responseStream) {
		this.request = request;
		this.responseStream = responseStream;
	}

	public abstract Response createResponse();
	public void sendResponse() throws IOException {
		Response response = createResponse();
		response.send();
	}
}