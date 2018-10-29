package request;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader; 
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.net.URLDecoder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import httpstatus.HttpStatusCode;
import utils.ReadLineHelper;
import utils.HttpHeaderHelper;
import utils.UrlQueryParamsParser;
import exceptions.BadRequestException;
import exceptions.NotImplementedException;

import webapp.RouterMatcher;

public class Request {

	private String url;
	private String originUrl;
	private String method;
	private String httpVersion;
	private String rejectedReason;
	private String rejectedVerbose;
	private HashMap<String, String> urlParams;
	private boolean isValid = false;
	private HashMap<String, String> headers;
	private HashMap<String, String> body;
	private HashMap<String, String> queryParams;

	public static final String HTTP_V1 = "HTTP/1.0";
	public static final String HTTP_V1_1 = "HTTP/1.1";
	public static final String NOT_IMPLEMENTED = "NOT_IMPLEMENTED";
	public static final String BAD_REQUEST = "BAD_REQUEST";

	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";


	public Request(String url, String originUrl, String method, 
					HashMap<String, String> headers, 
					HashMap<String, String> body, 
					HashMap<String, String> queryParams, 
					HashMap<String, String> urlParams,
					String httpVersion) {
		this.url =  url;
		this.originUrl = originUrl;
		this.method = method;
		this.headers = headers;
		this.body = body;
		this.queryParams = queryParams;
		this.urlParams = urlParams;
		this.httpVersion = httpVersion;
		this.isValid = true;
		this.rejectedReason = null;
		this.rejectedVerbose = null;
	}

	public String getUrl() {
		return this.url;
	}

	public String getOriginUrl() {
		return this.originUrl;
	}

	public  HashMap<String, String> getUrlParams() {
		return this.urlParams;
	}

	public String getMethod() {
		return this.method;
	}

	public HashMap<String, String> getQueryParams() {
		return this.queryParams;
	}

	public String getHttpVersion() {
		return this.httpVersion;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public Request(String rejectedReason, String rejectedVerbose,
		 String originUrl, String httpVersion, String method) {
		this.rejectedReason = rejectedReason;
		this.rejectedVerbose = rejectedVerbose;
		this.originUrl = originUrl;
		this.httpVersion = httpVersion;
		this.method = method;
	}

	public String getRejectedReason() {
		return this.rejectedReason;
	}

	public String getRejectedVerbose() {
		return this.rejectedVerbose;
	}

	public HashMap<String, String> getHeader() {
		return this.headers;
	}

	public HashMap<String, String> getBody() {
		return this.body;
	}

	public static Request parse(BufferedReader requestInputStream) throws IOException {
		String url = null;
		String originUrl = null;
		String method = null;
		String httpVersion = null;
		try {
			byte[] requestByte = ReadLineHelper.readLine(requestInputStream);
			String requestTextPerLine = new String(requestByte);
			String[] temp = requestTextPerLine.split(" ");
			if(temp.length < 3) {
				throw new BadRequestException(BAD_REQUEST);
			}

			url = "";
			originUrl = temp[1];
			method = temp[0];
			httpVersion = temp[2];

			String[] urlSplitted = temp[1].split("\\?");
			HashMap<String, String> queryParams = new HashMap<String, String>();
			if(urlSplitted.length > 1) {
				String decodedUrl = URLDecoder.decode(urlSplitted[1], "UTF-8");
				queryParams = UrlQueryParamsParser.constructQueryParams(decodedUrl);
				url = urlSplitted[0];
			} else {
				url = temp[1];
			}


			if(!httpVersion.equals(HTTP_V1) && !httpVersion.equals(HTTP_V1_1)) {
				throw new NotImplementedException(httpVersion);
			}

			if(!method.equals(GET) && !method.equals(POST)) {
				throw new NotImplementedException(method);
			}

			HashMap<String, String> matcherList = RouterMatcher.getMatcherList();
			Iterator<String> matcherKeysIt = matcherList.keySet().iterator();
			HashMap<String, String> urlParams = new HashMap<String, String>();

			HashMap<String, ArrayList<String>> matcherKeysMap= RouterMatcher.getMatcherKeys();

			while(matcherKeysIt.hasNext()) {
				String key = matcherKeysIt.next();
				Pattern pattern = Pattern.compile(key);
				Matcher matcher = pattern.matcher(url);
				while(matcher.find()) {
					ArrayList<String> matcherKeys = matcherKeysMap.get(key);
					Iterator<String> it = matcherKeys.iterator();
					while(it.hasNext()) {
						String itKey = it.next();
						urlParams.put(itKey, matcher.group(itKey));
					}
					url = matcherList.get(key);
					break;
				}
			}

			// Parsing request headers
			HashMap<String, String> headers = new HashMap<String, String>();
			requestByte = ReadLineHelper.readLine(requestInputStream);
			while(requestByte.length > 0) {
				requestTextPerLine = new String(requestByte);
				temp = requestTextPerLine.split(":");
				if(temp.length < 2) {
					throw new BadRequestException(BAD_REQUEST);
				} else if(temp.length > 2) {
					for (int i = 2;i<temp.length;i++) {
						temp[1] += ( ":" + temp[i].trim());
					}
				}
				String headerKey = temp[0].toUpperCase();
				String headerValue = temp[1].trim();
				headers.put(headerKey, headerValue);
				requestByte = ReadLineHelper.readLine(requestInputStream);
			}
			HashMap<String, String> body = null;
			if(method.equals("POST")) {
				body = new HashMap<String, String>();
				if(headers.containsKey("CONTENT-TYPE") &&
					headers.get("CONTENT-TYPE").equals("application/x-www-form-urlencoded")) {
					int length = Integer.parseInt(headers.get("CONTENT-LENGTH"));
					requestByte = ReadLineHelper.readLine(requestInputStream, length);
					if(requestByte.length > 0) {
						requestTextPerLine = new String(requestByte);
						String headerBody = requestTextPerLine;
						String decodedBody = URLDecoder.decode(headerBody, "UTF-8");
						HashMap<String, String> constructedBody = HttpHeaderHelper.constructHeaderBody(decodedBody);
						if(constructedBody == null) {
							throw new BadRequestException(BAD_REQUEST);
						}
						body = constructedBody;
					}
				} else if(headers.containsKey("CONTENT-TYPE") &&
					headers.get("CONTENT-TYPE").equals("application/json")) {
					int length = Integer.parseInt(headers.get("CONTENT-LENGTH"));
					requestByte = ReadLineHelper.readLine(requestInputStream, length);
					String jsonString = "";
					if(requestByte.length > 0) {
						requestTextPerLine = new String(requestByte);
						String headerBody = requestTextPerLine;
						jsonString = headerBody;
					}

					try {
						ObjectMapper mapper = new ObjectMapper();
						HashMap<String, Object> map = new HashMap<String, Object>();
						body = mapper.readValue(jsonString, new TypeReference<HashMap<String, String>>(){});
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					int length = Integer.parseInt(headers.get("CONTENT-LENGTH"));
					requestByte = ReadLineHelper.readLine(requestInputStream, length);
					if(requestByte.length > 0) {
						throw new BadRequestException(BAD_REQUEST);
					}
					body = new HashMap<String, String>();
				}
			}
			return new Request(url, originUrl, method, headers, body, queryParams, urlParams, httpVersion);
		} catch(BadRequestException e) {
			System.out.println(e.getMessage());
			return new Request(BAD_REQUEST, e.getMessage(), 
				originUrl, httpVersion, method);
		} catch(NotImplementedException e) {
			return new Request(NOT_IMPLEMENTED, e.getMessage(), 
				originUrl, httpVersion, method);
		}
	}

	
}