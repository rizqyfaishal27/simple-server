package utils;

import java.util.HashMap;
import httpstatus.HttpStatusCode;

public class HttpHeaderHelper {

	HashMap<String, String> headers = new HashMap<String, String>();
	private String initialHeader;

	public HttpHeaderHelper(HttpStatusCode statusCode) {
		switch(statusCode) {
			case HttpStatusCode.OK:
				initialHeader = "";
			case 
		}
	}

	public static String generateHeader() {

	}
}