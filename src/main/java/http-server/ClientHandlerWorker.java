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
import java.lang.reflect.InvocationTargetException;


import logger.Logger;
import utils.UrlQueryParamsParser;
import httpstatus.HttpStatusCode;
import utils.TimestampUtil;
import webapp.WebApp;

import request.Request;


public class ClientHandlerWorker implements Runnable {

	private Socket clientSocket;
	private WebApp webApp;
	
	public ClientHandlerWorker(Socket clientSocket, WebApp webApp) {
		this.clientSocket = clientSocket;
		this.webApp = webApp;
	}

	public void run() {
		try {
			BufferedReader requestStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedOutputStream responseStream  = new BufferedOutputStream(clientSocket.getOutputStream());
			Request request = Request.parse(requestStream);

			webApp.resolveUrlToResponse(request, responseStream);

			requestStream.close();
			clientSocket.close();

			return;
		} catch(IOException error) {
			error.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
		} catch(InstantiationException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace(); 
		} catch(InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}