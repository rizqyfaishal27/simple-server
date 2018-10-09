package response;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;
import java.net.SocketException;

import httpstatus.HttpStatusCode;
import response.Response;
import request.Request;
import utils.HttpHeaderBuilder;
import logger.Logger;


public class RedirectResponse extends Response {

	private String location;

	public RedirectResponse(
		String location,
		Request request,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.FOUND, request, responseStream);
		this.location = location;
	}

	public void sendResponse() throws IOException {
		try {
			byte[] returnedData = "".getBytes("UTF-8");
			String header = HttpHeaderBuilder.generateHttpFoundHeader(
				request.getHttpVersion(),
				returnedData.length, location);
			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(returnedData);
			responseStream.write("\r\n".getBytes("UTF-8"));
			responseStream.flush();

			Logger.requestLogger(request.getMethod(), request.getOriginUrl(), request.getHttpVersion(),
				HttpStatusCode.FOUND.getCode(), returnedData.length);
		} catch(SocketException error) {

		}
	}
}