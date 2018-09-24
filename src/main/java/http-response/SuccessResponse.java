package response;

import java.net.Socket;
import java.io.BufferedOutputStream;
import java.util.HashMap;

import httpstatus.HttpStatusCode;
import response.Response;
import utils.HttpHeaderBuilder;


public class SuccessResponse extends Response {

	public SuccessResponse(
		HashMap<String, String> params,
		byte[] returnedData,
		HashMap<String, String> requestHeader,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.OK, returnedData, requestHeader, responseStream);
	}

	public void send() {
		
	}
}