package utils;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClient {

	private static final String USER_AGENT = "Curl";
	public static final String JSON_TYPE = "application/json";
	public static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded"; 
	
	public static HttpResponseClient sendGET(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return new HttpResponseClient(responseCode, response.toString());
	}

	public static HttpResponseClient sendPOST(String url, String contentType, String data) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		byte[] dataBytes = data.getBytes("UTF-8");
		con.setRequestMethod("POST");
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestProperty("Content-Type", contentType);
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Length", Integer.toString(dataBytes.length));
		con.setDoOutput(true);


		try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
			wr.write( dataBytes );
		} catch (IOException e) {
			e.printStackTrace();
		}

		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return new HttpResponseClient(responseCode, response.toString());
	}

	public static CompletableFuture<HttpResponseClient> sendGETAsync(String url) throws IOException {
		ExecutorService ioBound = Executors.newCachedThreadPool();
		return CompletableFuture.supplyAsync(() -> {
			try {
				HttpResponseClient data = sendGET(url);
				return data;
			} catch(IOException e) {
				return null;
			} 
		}, ioBound);
	}

	public static CompletableFuture<HttpResponseClient> sendPOSTAsync(String url, String contentType, String postData) throws IOException {
		ExecutorService ioBound = Executors.newCachedThreadPool();
		return CompletableFuture.supplyAsync(() -> {
			try {
				HttpResponseClient data = sendPOST(url, contentType, postData);
				return data;
			} catch(IOException e) {
				return null;
			} 
		}, ioBound);
	}

}
