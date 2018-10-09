package logger;
import java.net.Socket;
import utils.TimestampUtil;

import logger.ConsoleColors;

public class Logger {

	public static void outputMessage(String text) {
		System.out.println(ConsoleColors.YELLOW_BOLD + TimestampUtil.now() + ConsoleColors.RESET 
			+  " -------> " + ConsoleColors.GREEN_BOLD + text + ConsoleColors.RESET);
	}

	public static void errorMessage(String text) {
		System.out.println(ConsoleColors.YELLOW_BOLD + TimestampUtil.now() + ConsoleColors.RESET 
			+  " -------> " + ConsoleColors.RED_BOLD +  text + ConsoleColors.RESET);
	}

	public static void requestLogger(String method, String url, String httpVersion , int httpStatusCode, int contentLength) {
		outputMessage("\"" + method + " " + url + " " + httpVersion  + "\" " + httpStatusCode + " " + contentLength);
	}

	public static void errorRequestLogger(String method, String url, String httpVersion , int httpStatusCode, int contentLength) {
		errorMessage("\"" + method + " " + url + " " + httpVersion  + "\" " + httpStatusCode + " " + contentLength);
	}

}