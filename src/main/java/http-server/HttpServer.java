package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import logger.Logger;
import server.ClientHandler;
import webapp.WebApp;

public class HttpServer {
	private int port;
	private WebApp webApp;

	public HttpServer(int port) {
		this.port = port;
	}

	public void setWebApp(WebApp webApp) {
		this.webApp = webApp;
	}

	public void run() {
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			Logger.outputMessage("Server started at localhost:" + port);

			while(true) {
				Socket client = serverSocket.accept();
				Thread clientThread = new Thread(new ClientHandler(client, this.webApp));
				clientThread.start();
			}
		} catch (IOException error) {
			Logger.errorMessage(error.toString());
		}
	}
}