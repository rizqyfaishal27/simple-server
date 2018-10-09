package webapp.controllers;

import java.io.BufferedOutputStream;


import request.Request;
import response.Response;
import response.TextResponse;
import httpstatus.HttpStatusCode;


public class NotImplementedController extends BaseController {

	public NotImplementedController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new TextResponse(HttpStatusCode.NOT_IMPLEMENTED.getCode() 
			+  " Not Implemented: Reason: " + request.getRejectedVerbose() + "\r\n", 
			HttpStatusCode.NOT_IMPLEMENTED, request, responseStream);
	}
}