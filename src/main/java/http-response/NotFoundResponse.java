package response;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;

import httpstatus.HttpStatusCode;
import response.Response;
import utils.HttpHeaderBuilder;
import logger.Logger;


public class NotFoundResponse extends Response {

	public NotFoundResponse(
		byte[] returnedData,
		HashMap<String, String> requestHeader,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.NOT_FOUND, returnedData, requestHeader, responseStream);
	}

	public NotFoundResponse() {
		super(HttpStatusCode.NOT_FOUND, null, null, null);
	}

	public void send() throws IOException {
		Logger.errorMessage("\"" + 
			requestHeader.get("HTTP_METHOD")+ " " +
			requestHeader.get("URL_RESOURCE") + " " + 
			requestHeader.get("HTTP_VERSION")  +
		"\" " + HttpStatusCode.NOT_FOUND.getCode() + " " + returnedData.length);

		String header = HttpHeaderBuilder.generateHttpNotFoundHeader(requestHeader.get("HTTP_VERSION"),
			returnedData.length);
		responseStream.write(header.getBytes());
		responseStream.write(returnedData);
		responseStream.flush();
		responseStream.close();
	}
}