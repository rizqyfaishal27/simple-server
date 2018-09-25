package response;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;
import java.net.SocketException;


import httpstatus.HttpStatusCode;
import response.Response;
import utils.HttpHeaderBuilder;
import logger.Logger;


public class FoundResponse extends Response {

	private String location;

	public FoundResponse(
		String location,
		byte[] returnedData,
		HashMap<String, String> requestHeader,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.FOUND, returnedData, requestHeader, responseStream);
		this.location = location;
	}

	public void send() throws IOException {
		try {
			String header = HttpHeaderBuilder.generateHttpFoundHeader(requestHeader.get("HTTP_VERSION"),
			returnedData.length, location);
			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(returnedData);
			responseStream.flush();
			responseStream.close();

			Logger.outputMessage("\"" + 
				requestHeader.get("HTTP_METHOD")+ " " +
				requestHeader.get("URL_RESOURCE") + " " + 
				requestHeader.get("HTTP_VERSION")  +
			"\" " + HttpStatusCode.FOUND.getCode() + " " + returnedData.length);
		} catch(SocketException error) {
			// responseStream.close();
		}
	}
}