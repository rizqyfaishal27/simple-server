package webapp.models;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.StringBuilder;
import java.io.IOException;

import utils.FileUtil;

public class AppState {
	private int count;

	private static final AppState INSTANCE = new AppState();
	private static final String statePath = "/root/server.state";

	private AppState() {
		this.count = setUpState();
	}

	public synchronized void incrementCount() {
		this.count = this.count + 1;
	}

	public synchronized int getCount() {
		return this.count;
	}

	public synchronized void writeStateToFile() throws IOException {
		FileUtil.writeToTextFile(statePath, Integer.toString(this.count));
	}

	public static AppState getInstance() {
		return INSTANCE;
	}

	private static int setUpState() {
		try {
			String text = FileUtil.readTextFile(statePath);
			int count = Integer.parseInt(text);
			return count;
		} catch (Exception e) {
			return 0;
		}
	}
}