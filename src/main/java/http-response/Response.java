package response;

import java.net.Socket;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;


import httpstatus.HttpStatusCode;
import request.Request;
import webapp.models.AppState;

public abstract class Response {

	protected HttpStatusCode statusCode;
	protected BufferedOutputStream responseStream;
	protected Request request;


	public Response(
		HttpStatusCode statusCode,
		Request request,
		BufferedOutputStream responseStream) {
		this.statusCode = statusCode;
		this.responseStream = responseStream;
		this.request = request;
	}

	public void closeResponseStream() throws IOException {
		this.responseStream.close();
	}

	public void send() throws IOException {
		sendResponse();
		closeResponseStream();
		AppState.getInstance().incrementCount();
		AppState.getInstance().writeStateToFile();
	}

	public abstract void sendResponse() throws IOException;

	
}