package logger;

import utils.TimestampUtil;

public class Logger {

	public static void outputMessage(String text) {
		System.out.println(TimestampUtil.now() + " -------> " + text);
	}

	public static void errorMessage(String text) {
		System.err.println(TimestampUtil.now() + " -------> " + text);
	}

	public static void request(Socket client) {
		System.out.println(TimestampUtil.now() + " -------> " + text);
	}

}