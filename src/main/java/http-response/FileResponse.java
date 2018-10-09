package response;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.net.SocketException;

import httpstatus.HttpStatusCode;
import utils.AppConfig;
import utils.HttpHeaderBuilder;
import logger.Logger;
import request.Request;

public class FileResponse extends Response {
	private String filePath;

	public FileResponse(String filePath, Request request, BufferedOutputStream responseStream) {
		super(HttpStatusCode.OK, request, responseStream);
		this.filePath = filePath;
	}

	public void sendResponse() throws IOException {
		try {
			File dataToReturned = new File(AppConfig.DIRECTORY_ROOT + filePath);
			byte[] dataByteStream = Files.readAllBytes(dataToReturned.toPath());
			String header = HttpHeaderBuilder.generateHttpOkHeader(request.getHttpVersion(),
				dataByteStream.length, Files.probeContentType(dataToReturned.toPath()));

			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(dataByteStream);
			responseStream.write("\r\n".getBytes("UTF-8"));
			responseStream.flush();

			Logger.requestLogger(request.getMethod(), request.getOriginUrl(), request.getHttpVersion(),
				HttpStatusCode.OK.getCode(), dataByteStream.length);
			
		} catch (SocketException e){

		}
	}
}