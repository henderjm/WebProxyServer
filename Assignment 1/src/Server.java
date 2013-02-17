import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.*;

public class Server extends Thread {
	UrlHandling blocked_urls = new UrlHandling();
	private String[] DataBase_BlockedUrls = new String[20];
	@SuppressWarnings("restriction")
	public static void main(String[] args) throws IOException {
		// Server s = new Server(8080);
		HttpServer hs = HttpServer.create(new InetSocketAddress(8080), 0);
		hs.createContext("/", new Handler());
		System.out.println(hs.getAddress());
		hs.setExecutor(Executors.newCachedThreadPool());
		hs.start();

		System.out.println("Awaiting requests. . .");
		// String tmp = "";
		// while((tmp = input.readLine()) != null){
		// System.out.println(tmp);
		// out.writeBytes(tmp);
		// }
	}
	public String[] getBlockedURLs(){
		return this.DataBase_BlockedUrls;
	}
	public void addUrlToBlockedList(String bURL) {
		/*
		 * Find a free index
		 * limited space
		 * may change to arraylist
		 */
	}
	public static boolean urlIsSafe(String url) {
		UrlHandling uh = new UrlHandling();
		String[] DataBase_BlockedUrls = uh.getBlockedUrls();
		for(int i = 0; i < DataBase_BlockedUrls.length; i++) {
			if(url.equals(DataBase_BlockedUrls[i]))
				return false;
		}
	
		return true;
	}

	public static int sendGetRequest(String requestURL,
			StringBuffer output) throws IOException {
		System.out.println("URL creating");
		URL url = new URL("http://" + requestURL);
		InetAddress address = InetAddress.getByName(url.getHost());
		String ip = address.getHostAddress(); // gets urls address in the form 0.0.0.0
		System.out.println("IP is: " + ip);
		HttpURLConnection httpcon = null;
		int return_code = 0;
		if (urlIsSafe(ip)) {
			httpcon = (HttpURLConnection) url
					.openConnection();
			httpcon.setUseCaches(false);
			httpcon.setDoInput(true); // true if we want to read server's
										// response
			httpcon.setDoOutput(false); // false indicates this is a GET request
			String tmp;

			BufferedReader input = new BufferedReader(new InputStreamReader(
					httpcon.getInputStream()));
			String req = input.readLine();
			System.out.println(req);
			System.out.println(input.toString().length());
			while ((tmp = input.readLine()) != null) {
				// System.out.println(tmp);
				output.append(tmp);
			}

			System.out.println("\tFinished SGR");
			input.close();
			return_code = 200;
		}
		else{
			return_code = 404;
		}
		return return_code;
	}

	@SuppressWarnings("restriction")
	public static class Handler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String requestURL = "";
			StringBuffer responseBuffer = new StringBuffer();
			responseBuffer
					.append("<b> This is the HTTP Server Home Page.... </b><BR>");
			responseBuffer.append("The HTTP Client request is ....<BR>");
			int code;
			DataOutputStream output = null;
			OutputStream os = t.getResponseBody();
			Headers reqHeaders = t.getRequestHeaders();
			Set<String> keySet = reqHeaders.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<String> values = reqHeaders.get(key);
				System.out.println(key + " = " + values.toString());
				String s = key + " = " + values.toString() + "\n";
				if (key.equals("Host")) {
					requestURL = values.toString().substring(1,
							values.toString().length() - 1);
					System.out.println("URL to be checked: " + requestURL);

					code = sendGetRequest(requestURL, responseBuffer);

					String response = t.getRequestMethod();
					System.out.println("Response: " + response + "\nContext: "
							+ t.getHttpContext());
					Headers respHeaders = t.getResponseHeaders();
					respHeaders.set("Content-Type", "text/html");

					t.sendResponseHeaders(code, responseBuffer.length());
				}
				os.write(responseBuffer.toString().getBytes());
			}
			os.flush();
			os.close();
			t.close();
			System.out.println("Hello");
		}
	}
}
