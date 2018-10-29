package response;

import java.util.HashMap;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.SocketException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import request.Request;
import httpstatus.HttpStatusCode;
import utils.HttpHeaderBuilder;
import logger.Logger;

public class JsonResponse extends Response {

	private HashMap<String, Object> data;

	public JsonResponse(HashMap<String, Object> data,
		HttpStatusCode httpStatus,
		Request request, BufferedOutputStream responseStream) {
		super(httpStatus, request, responseStream);
		this.data = data;
	}

	public void sendResponse() throws IOException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.data);

			byte[] returnedData = json.getBytes("UTF-8");
			String header = HttpHeaderBuilder.generateHttpOkHeader(request.getHttpVersion(),
				returnedData.length, "application/json");
			responseStream.write(header.getBytes("UTF-8"));
			responseStream.write(returnedData);
			responseStream.write("\r\n".getBytes("UTF-8"));
			responseStream.flush();

			if(statusCode.getCode() == 200) {
				Logger.requestLogger(request.getMethod(), request.getOriginUrl(), request.getHttpVersion(),
					HttpStatusCode.OK.getCode(), returnedData.length);
			} else {
				Logger.errorRequestLogger(request.getMethod(), request.getOriginUrl(), request.getHttpVersion(),
					statusCode.getCode(), returnedData.length);
			}
			

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}