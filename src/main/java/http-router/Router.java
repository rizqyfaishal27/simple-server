package router;

import java.util.HashMap;

public class Router {

	private HashMap<String, String> routerList;

	public Router() {
		routerList = new HashMap<String, String>();
	}

	public static void register(String url, String action) {
		routerList.put(url, action);
	}

	public HashMap<String, String> getUrlRoute() {
		return routerList;
	}
}