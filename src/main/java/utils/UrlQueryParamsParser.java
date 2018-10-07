package utils;

import  java.util.HashMap;

public class UrlQueryParamsParser {

	public static HashMap<String, String> constructQueryParams(String decodedBody) {
		String[] temp = decodedBody.split("&");
		HashMap<String, String> returnedData = new HashMap<String, String>();
		for(String body: temp) {
			String[] parsed = body.split("=");
			if(parsed.length <= 1) {
				returnedData.put(parsed[0].trim(), "");
			} else {
				returnedData.put(parsed[0].trim(), parsed[1].trim());
			}
		}
		return returnedData;
	}
}