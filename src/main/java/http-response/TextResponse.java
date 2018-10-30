package response;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.net.SocketException;

import httpstatus.HttpStatusCode;
import utils.AppConfig;
import utils.HttpHeaderBuilder;
import logger.Logger;
import request.Request;


public class TextResponse extends Response {
	private String data;

	public TextResponse(String data, HttpStatusCode httpStatus, Request request, BufferedOutputStream responseStream) {
		super(httpStatus, request, responseStream);
		this.data = data;
	}

	public void sendResponse() throws IOException {
		try {
			byte[] returnedData = data.getBytes("UTF-8");
			String header = HttpHeaderBuilder.generateHttpOkHeader(request.getHttpVersion(),returnedData.length, "text/plain");
			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(returnedData);
			responseStream.write("\r\n".getBytes("UTF-8"));
			responseStream.flush();

			if(statusCode.getCode() != 200) {
				Logger.errorRequestLogger(
					request.getMethod(), 
					request.getOriginUrl(), request.getHttpVersion(),
					statusCode.getCode(), returnedData.length);
			} else {
				Logger.requestLogger(
					request.getMethod(), 
					request.getOriginUrl(), request.getHttpVersion(),
					statusCode.getCode(), returnedData.length);
			}
			
			
		} catch (SocketException e) {

		}
	}
}

