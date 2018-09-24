package utils;

import java.util.HashMap;
import java.io.BufferedInputStream;
import java.io.IOException; 
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import java.lang.ArrayIndexOutOfBoundsException;

import httpstatus.HttpStatusCode;
import utils.ReadLineHelper;
import exceptions.BadRequestException;
import exceptions.NotImplementedException;

public class HttpHeaderHelper {

	HashMap<String, String> headers = new HashMap<String, String>();
	public static final String HTTP_V1 = "HTTP/1.0";
	public static final String HTTP_V1_1 = "HTTP/1.1";

	public HttpHeaderHelper(BufferedInputStream request) throws IOException{
		try {
			byte[] requestByte = ReadLineHelper.readLine(request);
			String requestTextPerLine = new String(requestByte);
			String[] temp = requestTextPerLine.split(" ");

			if(temp.length < 3) {
				throw new BadRequestException("Bad format");
			}

			headers.put("HTTP_METHOD", temp[0]);
			headers.put("URL_RESOURCE", temp[1]);
			headers.put("HTTP_VERSION", temp[2]);
			if(!headers.get("HTTP_VERSION").equals(HTTP_V1) && !headers.get("HTTP_VERSION").equals(HTTP_V1_1)) {
				throw new NotImplementedException(headers.get("HTTP_VERSION"));
			}

			if(!headers.get("HTTP_METHOD").equals("GET") && !headers.get("HTTP_METHOD").equals("POST")) {
				throw new NotImplementedException(headers.get("HTTP_METHOD"));
			}

			requestByte = ReadLineHelper.readLine(request);
			requestTextPerLine = new String(requestByte);
			while(requestTextPerLine != null && !requestTextPerLine.equals("")) {
				temp = requestTextPerLine.split(":");
				if(temp.length != 2) {
					throw new BadRequestException("Bad Format");
				}
				String headerKey = temp[0].toUpperCase();
				String headerValue = temp[1].trim();
				headers.put(headerKey, headerValue);
				requestByte = ReadLineHelper.readLine(request);
				requestTextPerLine = new String(requestByte);
			}
			headers.put("IS_VALID", "true");
		} catch(NotImplementedException error) {
			headers.put("IS_VALID", "false");
			headers.put("REASON", error.getMessage());
			headers.put("STATUS_CODE", String.valueOf(HttpStatusCode.NOT_IMPLEMENTED.getCode()));
		} catch(BadRequestException error) {
			headers.put("IS_VALID", "false");
			headers.put("REASON", error.getMessage());
			headers.put("STATUS_CODE", String.valueOf(HttpStatusCode.BAD_REQUEST.getCode()));
		} 
	}

	public HashMap<String, String> getHeaderMap() {
		return headers;
	}

	
}