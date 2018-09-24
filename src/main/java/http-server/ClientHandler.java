package server;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class ClientHandler implements Runnable {

	private Socket clientSocket;
	
	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {
		BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		BufferedWriter response  = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
	}
}