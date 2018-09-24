package utils;

import java.sql.Timestamp;

public class TimestampUtil {

	public static String now() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.toString();
	}
}