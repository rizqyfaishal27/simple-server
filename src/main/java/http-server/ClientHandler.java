package server;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.net.URLDecoder;
import java.io.IOException;
import java.util.Date;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.util.Random;

import logger.Logger;
import utils.HttpHeaderHelper;
import utils.UrlQueryParamsParser;
import httpstatus.HttpStatusCode;
import utils.TimestampUtil;
import router.Router;
import response.Response;
import response.NotImplementedResponse;
import response.BadRequestResponse;
import response.FoundResponse;
import response.SuccessResponse;



public class ClientHandler implements Runnable {

	private Socket clientSocket;
	private Router router;
	
	public ClientHandler(Socket clientSocket, Router router) {
		this.clientSocket = clientSocket;
		this.router = router;
	}

	public void run() {
		try {
			BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedOutputStream response  = new BufferedOutputStream(clientSocket.getOutputStream());
			HashMap<String, String> header = new HttpHeaderHelper(request).getHeaderMap();
			
			boolean isValid = Boolean.parseBoolean(header.get("IS_VALID"));
			if(!isValid) {
				String reason = "Reason: " + header.get("REASON");
				int statusCode = Integer.parseInt(header.get("STATUS_CODE"));
				String returnedData = null;
				Response clientResponse = null;
				switch(statusCode) {
					case 501:
						returnedData = HttpStatusCode.NOT_IMPLEMENTED.getCode() + " " 
							+ HttpStatusCode.NOT_IMPLEMENTED.getDescription() + ": " + reason + "\r\n";
						clientResponse = new NotImplementedResponse(returnedData.getBytes("UTF-8"), 
							header, response);
						clientResponse.send();
						break;
					case 400:
						returnedData = HttpStatusCode.BAD_REQUEST.getCode() + " " 
							+ HttpStatusCode.BAD_REQUEST.getDescription() + "\r\n";
						clientResponse = new BadRequestResponse(returnedData.getBytes("UTF-8"), header, response);
						clientResponse.send();
						break;
				}
			} else {
				String urlResource = header.get("URL_RESOURCE");
				String httpMethod = header.get("HTTP_METHOD");
				HashMap<String, Response> urlLists = router.getUrlRoute();
				if(httpMethod.equals("GET")) {
					String url = httpMethod + ":" + urlResource;
					String[] urlQuerySplit = urlResource.split("\\?");
					switch("GET:" + urlQuerySplit[0]) {
						case "GET:/info":
							HashMap<String, String> infoTemplateString = new HashMap<String, String>();
							SuccessResponse infoResponse = (SuccessResponse) urlLists.get("GET:" + urlQuerySplit[0]);

							if(urlQuerySplit.length <= 1) {
								infoTemplateString.put("__DATA__", "No Data");
							} else {
								String decodedUrl = URLDecoder.decode(urlQuerySplit[1], "UTF-8");
								HashMap<String, String> queryParams = UrlQueryParamsParser.constructQueryParams(decodedUrl);
								String type = queryParams.get("type");
								switch(type) {
									case "time":
										String now = TimestampUtil.now();
										infoTemplateString.put("__DATA__", now + "\r\n");
										break;
									case "random":
										Random rand = new Random();
										infoTemplateString.put("__DATA__", Integer.toString(rand.nextInt(10000) + 1) + "\r\n");
										break;
									default:
										infoTemplateString.put("__DATA__", "No Data\r\n");
										break;

								}
							}
							infoResponse.setTemplateStrings(infoTemplateString);
							infoResponse.setResponseStream(response);
							infoResponse.setRequestHeader(header);
							infoResponse.sendLazy();
							break;
						default:
							router.sendResponse(url, header, response);
							break;

					}
				} else if(httpMethod.equals("POST")) {
					String httpBody = header.get("HTTP_BODY");
					String decodedBody = URLDecoder.decode(httpBody, "UTF-8");
					switch(header.get("URL_RESOURCE")) {
						case "/hello-world":
							SuccessResponse helloWorldPostResponse = 
								(SuccessResponse) urlLists.get("POST:" + header.get("URL_RESOURCE"));
							HashMap<String, String> headerBody = HttpHeaderHelper.constructHeaderBody(decodedBody);
							HashMap<String, String> templateStrings = new HashMap<String, String>();
							templateStrings.put("__HELLO__", headerBody.get("name"));
							helloWorldPostResponse.setRequestHeader(header);
							helloWorldPostResponse.setResponseStream(response);
							helloWorldPostResponse.setTemplateStrings(templateStrings);
							helloWorldPostResponse.sendLazy();
							break;
						default:
							router.sendResponse("POST:" + header.get("URL_RESOURCE"), header, response);
							break;
					}
				}
			}
			request.close();
			clientSocket.close();
			return;
		} catch(IOException error) {
			error.printStackTrace();
		}
	}
}