package response;

import httpstatus.HttpStatusCode;

public abstract class Response {

	private HttpStatusCode statusCode;

	public Response(HttpStatusCode statusCode) {
		this.statusCode = statusCode;
	}
	
}