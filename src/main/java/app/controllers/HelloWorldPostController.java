package webapp.controllers;

import java.io.BufferedOutputStream;
import java.util.HashMap;


import request.Request;
import response.Response;
import response.TemplateTextResponse;

public class HelloWorldPostController extends BaseController {

	public HelloWorldPostController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("__HELLO__", request.getBody().get("name"));
		return new TemplateTextResponse("/hello-world.html", data, request, responseStream);
	}
}