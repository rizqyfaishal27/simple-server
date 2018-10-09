package webapp;

import java.util.HashMap;
import java.util.ArrayList;

public class RouterMatcher {

	public static HashMap<String, String> getMatcherList() {
		HashMap<String, String> matcherList = new HashMap<String, String>();
		matcherList.put("/api/plus_one/(?<number>\\d+)$", "/api/plus_one/<int:num>");
		return matcherList;
	}

	public static HashMap<String, ArrayList<String>> getMatcherKeys() {
		HashMap<String, ArrayList<String>> matcherKeys = new HashMap<String, ArrayList<String>>();
		ArrayList<String> temp1 = new ArrayList<String>();
		temp1.add("number");
		matcherKeys.put("/api/plus_one/(?<number>\\d+)$", temp1);
		return matcherKeys;
	}
}