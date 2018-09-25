package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class ReadLineHelper {

	public static byte[] readLine(BufferedReader input) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		while(true) {
			int data = input.read();
			if(data == -1 || data == '\n') {
				return buffer.toByteArray();
			} else if (data == '\r') {
				input.mark(1);
				data = input.read();
				if(data != '\n') {
					input.reset();
				}
				return buffer.toByteArray();
			} else {
				buffer.write(data);
			}
		}

	}

	public static byte[] readLine(BufferedReader input, int length) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		while(buffer.size() < length) {
			int data = input.read();
			if(data == -1 || data == '\n') {
				return buffer.toByteArray();
			} else if (data == '\r') {
				input.mark(1);
				data = input.read();
				if(data != '\n') {
					input.reset();
				}
				return buffer.toByteArray();
			} else {
				buffer.write(data);
			}
		}
		return buffer.toByteArray();
	}
}