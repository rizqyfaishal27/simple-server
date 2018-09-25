package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import logger.Logger;
import server.ClientHandler;
import router.Router;

public class HttpServer {
	private int port;
	private Router router;

	public HttpServer(int port, Router router) {
		this.port = port;
		this.router = router;
	}

	public void run() throws CloneNotSupportedException {
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			Logger.outputMessage("Server started at localhost:" + port);

			while(true) {
				Socket client = serverSocket.accept();
				Thread clientThread = new Thread(new ClientHandler(client, 
						new Router(router.getUrlRoute(), router.getUrlLazy())));
				clientThread.start();
			}
		} catch (IOException error) {
			Logger.errorMessage(error.toString());
		}
	}
}