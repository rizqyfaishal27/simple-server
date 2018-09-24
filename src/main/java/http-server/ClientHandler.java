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
import response.Response;
import response.NotImplementedResponse;
import response.BadRequestResponse;
import response.FoundResponse;


public class ClientHandler implements Runnable {

	private Socket clientSocket;
	
	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
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

				switch(statusCode) {
					case HttpStatusCode.NOT_IMPLEMENTED.getCode():
						String returnedData = HttpStatusCode.NOT_IMPLEMENTED.getCode() + " " 
							+ HttpStatusCode.NOT_IMPLEMENTED.getDescription() + ": " + reason + "\r\n";
						NotImplementedResponse clientResponse = new NotImplementedResponse(returnedData.getBytes(), 
							header, response);
						clientResponse.send();
						break;
					case HttpStatusCode.BAD_REQUEST.getCode():
						String returnedData = HttpStatusCode.BAD_REQUEST.getCode() + " " 
							+ HttpStatusCode.BAD_REQUEST.getDescription() + "\r\n";
						BadRequestResponse clientResponse = new BadRequestResponse(returnedData.getBytes(), header, response);
						clientResponse.send();
						break;
				}
			} else {
				String url = header.get("URL_RESOURCE");
				if(url.equals("/")) {
					String returnedData = "";
					String redirectedLocation = "/hello-world"
					FoundResponse clientResponse = new FoundResponse(returnedData.getBytes(), header, response)
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