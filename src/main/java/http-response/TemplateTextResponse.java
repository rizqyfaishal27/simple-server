package response;

import java.util.HashMap;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.net.SocketException;
import java.util.Set;
import java.util.Iterator;

import httpstatus.HttpStatusCode;
import request.Request;
import utils.AppConfig;
import utils.HttpHeaderBuilder;
import logger.Logger;

public class TemplateTextResponse extends Response {

	private String templateTextPath;
	private HashMap<String, String> templateStrings;

	public TemplateTextResponse(
		String templateTextPath,
		HashMap<String, String> templateStrings,
		Request request,
		BufferedOutputStream responseStream) {
		super(HttpStatusCode.OK, request, responseStream);
		this.templateTextPath = templateTextPath;
		this.templateStrings = templateStrings;
	}

	public void sendResponse() throws IOException {
		try {
			File templateTextFile = new File(AppConfig.TEMPLATE_VIEW_ROOT + templateTextPath);
			String templateText = new String(Files.readAllBytes(templateTextFile.toPath()));

			Set<String> keys = templateStrings.keySet();
			Iterator<String> keysIterator = keys.iterator();
			while(keysIterator.hasNext()) {
				String key = keysIterator.next();
				String replacedText = templateStrings.get(key);
				templateText = templateText.replaceAll(key, replacedText);
			}

			byte[] returnedData = templateText.getBytes("UTF-8");

			String header = HttpHeaderBuilder.generateHttpOkHeader(request.getHttpVersion(),returnedData.length, "text/html");
			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(returnedData);
			responseStream.write("\r\n".getBytes("UTF-8"));
			responseStream.flush();

			Logger.requestLogger(request.getMethod(), request.getOriginUrl(), request.getHttpVersion(),
				HttpStatusCode.OK.getCode(), returnedData.length);
			
		} catch (SocketException e) {

		}
	}
}