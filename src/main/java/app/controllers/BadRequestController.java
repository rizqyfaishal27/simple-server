package webapp.controllers;

import java.io.BufferedOutputStream;


import request.Request;
import response.Response;
import response.TextResponse;
import httpstatus.HttpStatusCode;


public class BadRequestController extends BaseController {

	public BadRequestController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new TextResponse(HttpStatusCode.BAD_REQUEST.getCode() + 
			" Bad Request\r\n", HttpStatusCode.BAD_REQUEST, request, responseStream);
	}
}