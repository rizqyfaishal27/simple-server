package utils;

public class HttpResponseClient {
	private int statusCode;
	private String data;

	public HttpResponseClient(int statusCode, String data) {
		this.statusCode = statusCode;
		this.data = data;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getData() {
		return data;
	}
}