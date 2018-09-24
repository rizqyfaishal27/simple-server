package response;

import java.io.BufferedOutputStream;
import java.util.HashMap;

import httpstatus.HttpStatusCode;
import response.Response;
import utils.HttpHeaderBuilder;


public class NotFoundResponse extends Response {

	public NotFoundResponse(
		byte[] returnedData,
		HashMap<String, String> requestHeader,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.NOT_FOUND, returnedData, requestHeader, responseStream);
	}

	public void send() {
		
	}
}