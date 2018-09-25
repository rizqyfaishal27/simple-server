package response;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;
import java.net.SocketException;

import httpstatus.HttpStatusCode;
import response.Response;
import utils.HttpHeaderBuilder;
import logger.Logger;


public class BadRequestResponse extends Response {

	public BadRequestResponse(
		byte[] returnedData,
		HashMap<String, String> requestHeader,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.BAD_REQUEST, returnedData, requestHeader, responseStream);
	}

	public void send() throws IOException {
		try {
			String header = HttpHeaderBuilder.generateHttpBadRequestHeader(requestHeader.get("HTTP_VERSION"),
			returnedData.length);
			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(returnedData);
			responseStream.flush();
			responseStream.close();

			Logger.errorMessage("\"" + 
				requestHeader.get("HTTP_METHOD")+ " " +
				requestHeader.get("URL_RESOURCE") + " " + 
				requestHeader.get("HTTP_VERSION")  +
			"\" " + HttpStatusCode.BAD_REQUEST.getCode() + " " + returnedData.length);
		} catch(SocketException error) {
			// responseStream.close();
		}
	}
}