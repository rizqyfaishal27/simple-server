package server;

import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Date;
import java.lang.StringBuilder;
import java.util.HashMap;

import logger.Logger;
import utils.HttpHeaderHelper;
import httpstatus.HttpStatusCode;
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
			BufferedInputStream request = new BufferedInputStream(clientSocket.getInputStream());
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
						clientResponse = new NotImplementedResponse(returnedData.getBytes(), 
							header, response);
						clientResponse.send();
						break;
					case 400:
						returnedData = HttpStatusCode.BAD_REQUEST.getCode() + " " 
							+ HttpStatusCode.BAD_REQUEST.getDescription() + "\r\n";
						clientResponse = new BadRequestResponse(returnedData.getBytes(), header, response);
						clientResponse.send();
						break;
				}
			} else {
				String urlResource = header.get("URL_RESOURCE");
				String httpMethod = header.get("HTTP_METHOD");
				
				if(httpMethod.equals("GET")) {
					String url = httpMethod + ":" + urlResource;
					router.sendResponse(url, header, response);
				} else if(httpMethod.equals("POST")) {
					
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