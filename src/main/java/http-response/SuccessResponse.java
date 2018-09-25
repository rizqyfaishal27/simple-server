package response;

import java.net.Socket;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.net.SocketException;


import httpstatus.HttpStatusCode;
import response.Response;
import utils.HttpHeaderBuilder;
import logger.Logger;
import mimetypes.MimeTypes;


public class SuccessResponse extends Response {

	private String mimeType;
	private String path;
	private HashMap<String, String> templateStrings;
	private boolean useTemplateStringFromFile;

	public SuccessResponse(
		String mimeType,
		byte[] returnedData,
		HashMap<String, String> requestHeader,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.OK, returnedData, requestHeader, responseStream);
		this.mimeType = mimeType;
	}

	public SuccessResponse(String path, 
		HashMap<String, String> templateStrings, 
		HashMap<String, String> requestHeader, 
		BufferedOutputStream responseStream, boolean useTemplateStringFromFile) {
		super(HttpStatusCode.OK, null, requestHeader, responseStream);
		this.path = path;
		this.templateStrings = templateStrings;
		this.useTemplateStringFromFile = useTemplateStringFromFile;
	}

	public void send() throws IOException {
		if(path !=  null) {
			sendLazy();
		} else {
			Logger.outputMessage("\"" + 
			requestHeader.get("HTTP_METHOD")+ " " +
			requestHeader.get("URL_RESOURCE") + " " + 
			requestHeader.get("HTTP_VERSION")  +
			"\" " + HttpStatusCode.OK.getCode() + " " + returnedData.length);

			String header = HttpHeaderBuilder.generateHttpOkHeader(requestHeader.get("HTTP_VERSION"),
				returnedData.length, mimeType);
			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(returnedData);
			responseStream.flush();
			responseStream.close();
		}
	}

	public void sendLazy() throws IOException {
		
		if(templateStrings == null) {
			File file = new File(path);
			String mimeTypeFile = Files.probeContentType(file.toPath());
			this.mimeType = mimeTypeFile;
			byte[] returnedDataFile = Files.readAllBytes(file.toPath());
			setReturnedData(returnedDataFile);
		} else {
			if(useTemplateStringFromFile) {
				File file = new File(path);
				String mimeTypeFile = Files.probeContentType(file.toPath());
				this.mimeType = mimeTypeFile;
				byte[] returnedDataFile = Files.readAllBytes(file.toPath());
				String text = new String(returnedDataFile, "UTF-8");
				Set<String> keys = templateStrings.keySet();
				Iterator<String> it = keys.iterator();
				while(it.hasNext()) {
					String key = it.next();
					text = text.replaceAll(key, templateStrings.get(key));
				}
				setReturnedData(text.getBytes("UTF-8"));
			} else {
				this.mimeType = MimeTypes.PLAIN_TEXT.asText();
				Set<String> keys = templateStrings.keySet();
				Iterator<String> it = keys.iterator();
				String text = path;
				while(it.hasNext()) {
					String key = it.next();
					text = text.replaceAll(key, templateStrings.get(key));
				}
				setReturnedData(text.getBytes("UTF-8"));
			}
		}
		try {
			String header = HttpHeaderBuilder.generateHttpOkHeader(requestHeader.get("HTTP_VERSION"),
			returnedData.length, mimeType);
			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(returnedData);
			responseStream.flush();
			responseStream.close();

			Logger.outputMessage("\"" + 
				requestHeader.get("HTTP_METHOD")+ " " +
				requestHeader.get("URL_RESOURCE") + " " + 
				requestHeader.get("HTTP_VERSION")  +
				"\" " + HttpStatusCode.OK.getCode() + " " + returnedData.length);
		} catch(SocketException error) {
			// responseStream.close();
		}
	}

	public void setTemplateStrings(HashMap<String, String> templateStrings) {
		this.templateStrings = templateStrings;
	}
}