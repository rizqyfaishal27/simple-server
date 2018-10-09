package webapp;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


import server.HttpServer;
import request.Request;
import webapp.controllers.BaseController;
import webapp.controllers.StaticFileController;


public class WebApp {
	private HttpServer server;
	private Router router;

	public WebApp(HttpServer server) throws IOException{
		this.server = server;
		configureRouter();
		this.server.setWebApp(this);
		
	}

	public void configureRouter() throws IOException {
		this.router = Router.configRouter();
	}

	public Router getRouter() {
		return this.router;
	}

	public void resolveUrlToResponse(Request request, BufferedOutputStream responseStream) 
		throws ClassNotFoundException, NoSuchMethodException, 
				InstantiationException, IllegalAccessException, 
				InvocationTargetException, IOException {
		String method = request.getMethod();
		BaseController controller = (BaseController) router.resolveController(request, responseStream);
		controller.sendResponse();
	}

	public void bootingUpServer() {
		this.server.run();
	}

}