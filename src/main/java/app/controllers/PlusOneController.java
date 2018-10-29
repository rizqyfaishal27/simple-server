package webapp.controllers;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.lang.NumberFormatException;

import httpstatus.HttpStatusCode;
import request.Request;
import response.Response;
import response.JsonResponse;

import webapp.models.AppSpec;


public class PlusOneController extends BaseController{

	public PlusOneController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		int number = Integer.parseInt(request.getUrlParams().get("number"));
		String apiVersion = (String) AppSpec.getInstance().getInfo().get("version");
		String plusoneret = Integer.toString(number + 1);
		data.put("apiVersion", apiVersion);
		data.put("plusoneret", plusoneret);

		return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
	}

}