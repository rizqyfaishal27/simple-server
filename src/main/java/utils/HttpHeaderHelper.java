package utils;

import java.util.HashMap;
import java.io.IOException;
import java.io.BufferedReader; 
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import java.lang.ArrayIndexOutOfBoundsException;

import httpstatus.HttpStatusCode;
import utils.ReadLineHelper;
import exceptions.BadRequestException;
import exceptions.NotImplementedException;

public class HttpHeaderHelper {	

	public static HashMap<String, String> constructHeaderBody(String decodedBody) {
		try {
			String[] temp = decodedBody.split("&");
			HashMap<String, String> returnedData = new HashMap<String, String>();
			for(String body: temp) {
				String[] parsed = body.split("=");
				returnedData.put(parsed[0].trim(), parsed[1].trim());
			}
			return returnedData;
		} catch(Exception e) {
			return null;
		}
	}

	
}