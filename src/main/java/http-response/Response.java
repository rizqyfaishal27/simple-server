package response;

import java.net.Socket;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;


import httpstatus.HttpStatusCode;

public abstract class Response {

	protected HttpStatusCode statusCode;
	protected BufferedOutputStream responseStream;
	protected byte[] returnedData;
	protected HashMap<String, String> requestHeader;


	public Response(HttpStatusCode statusCode,
		byte[] returnedData,
		HashMap<String, String> requestHeader,
		BufferedOutputStream responseStream) {
		this.statusCode = statusCode;
		this.returnedData = returnedData;
		this.responseStream = responseStream;
		this.requestHeader = requestHeader;
	}

	public HttpStatusCode getStatusCode() {
		return statusCode;
	}

	public BufferedOutputStream getResponseStream() {
		return responseStream;
	}

	public byte[] getReturnedData() {
		return returnedData;
	}

	public HashMap<String, String> getRequestHeader() {
		return requestHeader;
	}

	public void setResponseStream(BufferedOutputStream responseStream) {
		this.responseStream = responseStream;
	}

	public void setRequestHeader(HashMap<String, String> requestHeader) {
		this.requestHeader = requestHeader;
	}

	public void setReturnedData(byte[] returnedData) {
		this.returnedData = returnedData;
	}


	public abstract void send() throws IOException;


	
}