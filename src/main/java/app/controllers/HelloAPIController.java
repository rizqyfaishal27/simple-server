package webapp.controllers;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import request.Request;
import response.Response;
import response.JsonResponse;

import utils.HttpClient;
import utils.TimestampUtil;
import httpstatus.HttpStatusCode;
import webapp.models.AppSpec;
import webapp.models.AppState;

public class HelloAPIController extends BaseController{

	public HelloAPIController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		String requestText = request.getBody().get("request");
		HashMap<String, String> data = new HashMap<String, String>();
		if(requestText == null) {
			data.put("detail", "\'request\' is required property");
			data.put("status", Integer.toString(HttpStatusCode.BAD_REQUEST.getCode()));
			data.put("title", HttpStatusCode.BAD_REQUEST.getDescription());
			return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = HttpClient.sendGET("http://172.22.0.222:5000");
			HashMap<String, String> decodedJson = new HashMap<String, String>();
			decodedJson = mapper.readValue(jsonString, new TypeReference<HashMap<String, String>>(){});
			String state = decodedJson.get("state");
			String currentVisit = TimestampUtil.now();
			String count = Integer.toString(AppState.getInstance().getCount());
			String response = "Good " + state + ", " + requestText;
			data.put("apiVersion", (String) AppSpec.getInstance().getInfo().get("version"));
			data.put("count", count);
			data.put("currentVisit", currentVisit);
			data.put("response", response);
			return new JsonResponse(data, HttpStatusCode.OK, request, responseStream);
		} catch (Exception e) {
			return new JsonResponse(data, HttpStatusCode.INTERNAL_SERVER_ERROR, request, responseStream);
		}
	}

}