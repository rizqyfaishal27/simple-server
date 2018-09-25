package router;

import java.util.HashMap;
import java.io.BufferedOutputStream;
import java.io.IOException;

import response.Response;
import response.NotFoundResponse;

public class Router {

	private HashMap<String, Response> routerList;
	private HashMap<String, Boolean> routerLazy;

	public Router() {
		routerList = new HashMap<String, Response>();
		routerLazy = new HashMap<String, Boolean>();
		routerList.put("*", new NotFoundResponse());
		routerLazy.put("*", false);
	}

	public void register(String url, Response action, boolean lazy) {
		routerList.put(url, action);
		routerLazy.put(url, lazy);
	}

	public HashMap<String, Response> getUrlRoute() {
		return routerList;
	}

	public void sendResponse(String url , HashMap<String, String> requestHeader, BufferedOutputStream responseStream) throws IOException {
		if(routerList.containsKey(url)) {
			Response response = routerList.get(url);
			boolean lazy = routerLazy.get(url);
			response.setResponseStream(responseStream);
			response.setRequestHeader(requestHeader);
			response.send();
		} else {
			Response notFoundResponse = routerList.get("*");
			notFoundResponse.setResponseStream(responseStream);
			notFoundResponse.setRequestHeader(requestHeader);
			notFoundResponse.setReturnedData(new String("").getBytes());
			notFoundResponse.send();
		}
	}
}