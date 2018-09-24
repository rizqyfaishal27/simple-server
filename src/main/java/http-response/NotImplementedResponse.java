package response;

import java.net.Socket;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;

import httpstatus.HttpStatusCode;
import response.Response;
import utils.HttpHeaderBuilder;
import logger.Logger;

public class NotImplementedResponse extends Response {

	public NotImplementedResponse(
		byte[] returnedData,
		HashMap<String, String> requestHeader,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.NOT_IMPLEMENTED, returnedData, requestHeader, responseStream);
	}

	public void send() throws IOException {
		Logger.errorMessage("\"" + 
			requestHeader.get("HTTP_METHOD")+ " " +
			requestHeader.get("URL_RESOURCE") + " " + 
			requestHeader.get("HTTP_VERSION")  +
		"\" " + HttpStatusCode.NOT_IMPLEMENTED.getCode() + " " + returnedData.length);

		String header = HttpHeaderBuilder.generateHttpNotImplementedHeader(requestHeader.get("HTTP_VERSION"),
			returnedData.length);
		responseStream.write(header.getBytes());
		responseStream.write(returnedData);
		responseStream.flush();
		responseStream.close();
	}
}