package utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampUtil {

	public static String now() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date dateTime = new Date();
		dateTime.setTime(timestamp.getTime());
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(dateTime);
		return formattedDate.toString();
	}
}