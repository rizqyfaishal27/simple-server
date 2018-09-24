package router;

import java.util.HashMap;
import java.io.BufferedOutputStream;

import response.Response;
import response.NotFoundResponse;

public class Router {

	private HashMap<String, Response> routerList;

	public Router() {
		routerList = new HashMap<String, Response>();
		routerList.put("*", new NotFoundResponse());
	}

	public void register(String url, Response action) {
		routerList.put(url, action);
	}

	public HashMap<String, String> getUrlRoute() {
		return routerList;
	}

	public void sendResponse(String url , HashMap<String, String> requestHeader, BufferedOutputStream responseStream) {
		if(routerList.containsKey(url)) {
			
		}
	}
}