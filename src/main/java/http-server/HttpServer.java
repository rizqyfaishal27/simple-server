package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import logger.Logger;
import server.ClientHandler;

public class HttpServer {
	private int port;

	public HttpServer(int port) {
		this.port = port;
	}

	public void run() {
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			Logger.outputMessage("Server started at localhost:" + port);

			while(true) {
				Socket client = serverSocket.accept();
				Thread clientThread = new Thread(new ClientHandler(client));
				clientThread.start();
			}
		} catch (IOException error) {
			Logger.errorMessage(error.toString());
		}
	}
}