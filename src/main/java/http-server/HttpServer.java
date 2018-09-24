package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import logger.Logger;

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
				System.out.println(client);
			}
		} catch (IOException error) {
			Logger.errorMessage(error.toString());
		}
	}
}