package mimetypes;

public enum MimeTypes {
	
	HTML("text/html"),
	PLAIN_TEXT("text/plain"),
	PNG("image/png"),
	JPEG("image/jpeg"),
	CSS("text/css");

	private String text;

	MimeTypes(String text) {
		this.text = text;
	}

	public String asText() {
		return text;
	}
}