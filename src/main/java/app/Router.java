package webapp;

import java.util.HashMap;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;



import request.Request;
import webapp.controllers.BaseController;
import webapp.controllers.IndexPageController;
import webapp.controllers.StaticFileController;
import webapp.controllers.HelloWorldController;
import webapp.controllers.NotFoundController;
import webapp.controllers.BackgroundImageController;
import webapp.controllers.StyleController;
import webapp.controllers.HelloWorldPostController;
import webapp.controllers.NotImplementedController;
import webapp.controllers.BadRequestController;
import webapp.controllers.InfoController;
import webapp.controllers.ApiSpesificationController;
import webapp.controllers.PlusOneController;
import webapp.controllers.HelloAPIController;

import webapp.controllers.ewallet.PingController;
import webapp.controllers.ewallet.CheckQuorumController;
// import webapp.controllers.ewallet.Registerontroller;
// import webapp.controllers.ewallet.GetSaldoController;
// import webapp.controllers.ewallet.GetTotalSaldoController;
// import webapp.controllers.ewallet.TransferController;


import utils.AppConfig;


public class Router {

	HashMap<String, String> routesGET;
	HashMap<String, String> routesPOST;

	public Router() {
		routesGET = new HashMap<String, String>();
		routesPOST = new HashMap<String, String>();
	}

	public void register(String method, String url, String controller) {
		switch(method) {
			case Request.GET:
				routesGET.put(url, controller);
				break;
			case Request.POST:
				routesPOST.put(url, controller);
				break;
		}
	}

	public static Router configRouter() throws IOException {
		Router router = new Router();
		router = createStaticFileRouter(router, AppConfig.DIRECTORY_ROOT);
		router = additionalConfig(router);
		return router;
	}

	public static Router createStaticFileRouter(Router router, String baseDirectory) throws IOException {
		File baseDir = new File(baseDirectory);
		traverseRecursive(router, baseDir, baseDirectory);
		router = additionalConfig(router);
		return router;
	}


	public static Router traverseRecursive(Router router, File dir, String rootDir) throws IOException {
		File[] files = dir.listFiles();
		for(File temp: files) {
			if(temp.isDirectory()) {
				traverseRecursive(router, temp, rootDir);
			} else {
				String path = temp.getPath();
				String cleanPath = path.substring(rootDir.length(), path.length());
				router.register(Request.GET, cleanPath,  StaticFileController.class.getName());
			}
		}
		return router;
	}

	public static Router additionalConfig(Router router) {
		router.register(Request.GET, "/", IndexPageController.class.getName());
		router.register(Request.GET, "/hello-world", HelloWorldController.class.getName());
		router.register(Request.GET, "/background", BackgroundImageController.class.getName());
		router.register(Request.GET, "/style", StyleController.class.getName());
		router.register(Request.GET, "/info", InfoController.class.getName());
		router.register(Request.GET, "/api/spesifikasi.yaml", ApiSpesificationController.class.getName());
		router.register(Request.GET, "/api/plus_one/<int:num>", PlusOneController.class.getName());
		router.register(Request.GET, "/ewallet/check_quorum", CheckQuorumController.class.getName());

		
		router.register(Request.POST, "/api/hello", HelloAPIController.class.getName());
		router.register(Request.POST, "/hello-world", HelloWorldPostController.class.getName());
		router.register(Request.POST, "/ewallet/ping", PingController.class.getName());

		// router.register(Request.POST, "/getSaldo", GetSaldoController.class.getName());
		// router.register(Request.POST, "/getTotalSaldo", GetTotalController.class.getName());
		// router.register(Request.POST, "/transfer", TransferController.class.getName());
		// router.register(Request.POST, "/ewallet/register", RegisterController.class.getName());

		return router;
	}

	public BaseController resolveController(Request request, BufferedOutputStream responseStream) 
		throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
			
		if(!request.isValid()) {
			switch(request.getRejectedReason()) {
				case Request.NOT_IMPLEMENTED:
					return (BaseController) (new NotImplementedController(request, responseStream));
				case Request.BAD_REQUEST:
					return (BaseController) (new BadRequestController(request, responseStream));
			}
		}
		String url = request.getUrl();
		switch(request.getMethod()) {
			case Request.GET:
				if(routesGET.containsKey(url)) {
					String className = routesGET.get(url);
					if(className.equals(StaticFileController.class.getName())) {
						return (BaseController) (new StaticFileController(request.getUrl(), request, responseStream));
					}
					Constructor controllerClass = Class
						.forName(routesGET.get(url)).getConstructor(Request.class, BufferedOutputStream.class);
					return (BaseController) controllerClass.newInstance(request, responseStream);
				} else {
					return (BaseController) new NotFoundController(request, responseStream);
				}
			case Request.POST:
				if(routesPOST.containsKey(url)) {
					String className = routesPOST.get(url);
					Constructor controllerClass = Class
						.forName(routesPOST.get(url)).getConstructor(Request.class, BufferedOutputStream.class);
					return (BaseController) controllerClass.newInstance(request, responseStream);
				} else {
					return (BaseController) new NotFoundController(request, responseStream);
				}
			default:
				return (BaseController) (new NotImplementedController(request, responseStream));
		}		
	}
}