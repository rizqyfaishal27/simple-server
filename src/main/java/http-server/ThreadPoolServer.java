package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import logger.Logger;
import server.ClientHandlerWorker;
import webapp.WebApp;

public class ThreadPoolServer implements Runnable {
	private int port;
	private WebApp webApp;
	private int amountThread;
	protected Thread currentThread;
	private boolean isStopped = false;
	private ServerSocket serverSocket;
	private ExecutorService threadPool;


	public ThreadPoolServer(int port, int amountThread) {
		this.port = port;
		this.threadPool = Executors.newFixedThreadPool(amountThread);
		this.amountThread = amountThread;
	}

	public void setWebApp(WebApp webApp) {
		this.webApp = webApp;
	}

	public void stopServer() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch(IOException e) {
			Logger.errorMessage(e.toString());
		}
	}

	public void run() {
		synchronized(this) {
			this.currentThread = Thread.currentThread();
		}
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			this.serverSocket = serverSocket;
			Logger.outputMessage("Server started at localhost:" + port 
				+ " with number of thread " + this.amountThread + " in thread pool.");

			while(!this.isStopped) {
				try {
					Socket client = serverSocket.accept();
					this.threadPool.execute(new ClientHandlerWorker(client, this.webApp));
				} catch(RuntimeException e) {
					Logger.errorMessage(e.toString());
				}
			}
			this.threadPool.shutdown();
			Logger.outputMessage("Server stopped.");
		} catch (IOException error) {
			Logger.errorMessage(error.toString());
		}
	}
}