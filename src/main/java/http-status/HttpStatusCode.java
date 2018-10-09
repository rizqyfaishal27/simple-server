package httpstatus;

public enum HttpStatusCode {

	OK(200, "OK"),
	FOUND(302, "Found"),
	NOT_FOUND(404, "Not Found"),
	NOT_IMPLEMENTED(501, "Not Implemented"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	METHOD_NOT_ALLOWED (405, "Method Not Allowed"),
	BAD_REQUEST(400, "Bad Request");

	private int code;
	private String desc;
	private String text;

	HttpStatusCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
		this.text = Integer.toString(code);
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return desc;
	}

	public String getText() {
		return text;
	}
}