package webapp.controllers;

import java.io.BufferedOutputStream;
import java.util.Random;


import request.Request;
import response.Response;
import response.TextResponse;
import httpstatus.HttpStatusCode;
import utils.TimestampUtil;


public class InfoController extends BaseController {

	public InfoController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		String type = request.getQueryParams().get("type");
		String data = "";
		switch(type) {
			case "random":
				Random rand = new Random();
				data = Integer.toString(rand.nextInt()) + "\r\n";
				break;
			case "time":
				data = TimestampUtil.now() + "\r\n";
				break;
			default:
				data = "No Data\r\n";
				break;
		}
		return new TextResponse(data, HttpStatusCode.OK, request, responseStream);
	}
}