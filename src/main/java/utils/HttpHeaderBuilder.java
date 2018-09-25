package utils;

import java.util.HashMap;
import java.util.Iterator;
import java.lang.StringBuilder;
import java.util.Set;
import java.util.Date;

import httpstatus.HttpStatusCode;
import mimetypes.MimeTypes;

public class HttpHeaderBuilder {
	HashMap<String, String> headerList;

	public HttpHeaderBuilder() {
		headerList = new HashMap<String, String>();
	}

	public void set(String headerKey, String headerValue) {
		headerList.put(headerKey, headerValue);
	}

	public void remove(String headerKey) {
		if(headerList.containsKey(headerKey)) {
			headerList.remove(headerKey);
		}
	}

	public String generateHeaderString() {
		Set<String> keys = headerList.keySet();
		Iterator<String> it = keys.iterator();
		StringBuilder sb = new StringBuilder();
		while(it.hasNext()) {
			String key = it.next();
			sb.append(key);
			sb.append(": ");
			sb.append(headerList.get(key));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	public static String generateCommonHeader() {
		HttpHeaderBuilder hhb = new HttpHeaderBuilder();
		hhb.set("Connection", "close");
		hhb.set("Server", "localhost:12345");
		hhb.set("Date", new Date().toString());
		return hhb.generateHeaderString();
	}

	public static String generateHttpOkHeader(String httpVersion, int contentLength, String mimeType) {
		HttpHeaderBuilder header = new HttpHeaderBuilder();
		String commonHeader = HttpHeaderBuilder.generateCommonHeader();
		StringBuilder sb = new StringBuilder();
		sb.append(httpVersion);
		sb.append(" ");
		sb.append(HttpStatusCode.OK.getCode());
		sb.append(" ");
		sb.append(HttpStatusCode.OK.getDescription());
		sb.append("\r\n");
		sb.append(commonHeader);
		header.set("Content-Type", String.valueOf(mimeType));
		header.set("Content-Length", String.valueOf(contentLength));
		if(mimeType.equals("text/plain")) {
			header.set("Charset", "UTF-8");
		}
		sb.append(header.generateHeaderString());
		sb.append("\r\n");
		return sb.toString();
	}

	public static String generateHttpBadRequestHeader(String httpVersion, int contentLength) {
		String commonHeader = HttpHeaderBuilder.generateCommonHeader();
		HttpHeaderBuilder hb = new HttpHeaderBuilder();
		hb.set("Content-Type", MimeTypes.PLAIN_TEXT.asText());
		hb.set("Content-Length", String.valueOf(contentLength));
		StringBuilder sb = new StringBuilder();
		sb.append(httpVersion);
		sb.append(" ");
		sb.append(HttpStatusCode.BAD_REQUEST.getCode());
		sb.append(" ");
		sb.append(HttpStatusCode.BAD_REQUEST.getDescription());
		sb.append("\r\n");
		sb.append(commonHeader);
		sb.append(hb.generateHeaderString());
		sb.append("\r\n");
		return sb.toString();
	}

	public static String generateHttpNotImplementedHeader(String httpVersion, int contentLength) {
		String commonHeader = HttpHeaderBuilder.generateCommonHeader();
		HttpHeaderBuilder hb = new HttpHeaderBuilder();
		hb.set("Content-Type", MimeTypes.PLAIN_TEXT.asText());
		hb.set("Content-Length", String.valueOf(contentLength));
		StringBuilder sb = new StringBuilder();
		sb.append(httpVersion);
		sb.append(" ");
		sb.append(HttpStatusCode.NOT_IMPLEMENTED.getCode());
		sb.append(" ");
		sb.append(HttpStatusCode.NOT_IMPLEMENTED.getDescription());
		sb.append("\r\n");
		sb.append(commonHeader);
		sb.append(hb.generateHeaderString());
		sb.append("\r\n");
		return sb.toString();
	}

	public static String generateHttpFoundHeader(String httpVersion, int contentLength, String location) {
		String commonHeader = HttpHeaderBuilder.generateCommonHeader();
		HttpHeaderBuilder hb = new HttpHeaderBuilder();
		hb.set("Content-Type", MimeTypes.PLAIN_TEXT.asText());
		hb.set("Content-Length", String.valueOf(contentLength));
		hb.set("Location", location);
		StringBuilder sb = new StringBuilder();
		sb.append(httpVersion);
		sb.append(" ");
		sb.append(HttpStatusCode.FOUND.getCode());
		sb.append(" ");
		sb.append(HttpStatusCode.FOUND.getDescription());
		sb.append("\r\n");
		sb.append(commonHeader);
		sb.append(hb.generateHeaderString());
		sb.append("\r\n");
		return sb.toString();
	}

	public static String generateHttpNotFoundHeader(String httpVersion, int contentLength) {
		String commonHeader = HttpHeaderBuilder.generateCommonHeader();
		HttpHeaderBuilder hb = new HttpHeaderBuilder();
		hb.set("Content-Type", MimeTypes.PLAIN_TEXT.asText());
		hb.set("Content-Length", String.valueOf(contentLength));
		StringBuilder sb = new StringBuilder();
		sb.append(httpVersion);
		sb.append(" ");
		sb.append(HttpStatusCode.NOT_FOUND.getCode());
		sb.append(" ");
		sb.append(HttpStatusCode.NOT_FOUND.getDescription());
		sb.append("\r\n");
		sb.append(commonHeader);
		sb.append(hb.generateHeaderString());
		sb.append("\r\n");
		return sb.toString();
	}
}