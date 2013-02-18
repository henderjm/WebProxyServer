import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
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
		ManagementConsole mc = new ManagementConsole();
		HttpServer hs = HttpServer.create(new InetSocketAddress(8080),2);
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

	public String[] getBlockedURLs() {
		return this.DataBase_BlockedUrls;
	}

	public void addUrlToBlockedList(String bURL) {
		/*
		 * Find a free index limited space may change to arraylist
		 */
	}


	/*
	 * Host key found
	 */
//	public static int sendGetRequest(URL requestURL, StringBuffer output)
//			throws IOException {
//		System.out.println("URL creating");
//		URL url = requestURL;
//		InetAddress address = InetAddress.getByName(url.getHost());
//		String ip = address.getHostAddress(); // gets urls address in the form
//												// 0.0.0.0
//		System.out.println("IP is: " + ip);
//		HttpURLConnection httpcon = null;
//		int return_code = 0;
//		if (urlIsSafe(ip)) {
//			httpcon = (HttpURLConnection) url.openConnection();
//			httpcon.setUseCaches(false);
//			httpcon.setDoInput(true); // true if we want to read server's
//										// response
//			httpcon.setDoOutput(false); // false indicates this is a GET request
//			String tmp;
//
//			BufferedReader input = new BufferedReader(new InputStreamReader(
//					httpcon.getInputStream()));
//			System.out.println(input.toString().length());
//			while ((tmp = input.readLine()) != null) {
//				// System.out.println(tmp);
//				output.append(tmp);
//			}
//
//			System.out.println("\tFinished SGR");
//			input.close();
//			return_code = 200;
//		} else {
//			return_code = 404;
//		}
//		return return_code;
//	}

	/*
	 * Cookie handling
	 */
	public static void cookieHandling(String requestURL, String cname,
			String cvalue) {

		try {
			URL url = new URL("http://" + requestURL);
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.setRequestProperty("Cookie", cname + '=' + cvalue);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("restriction")
	public static class Handler implements HttpHandler {
		private HashMap<String, StringBuffer> cache = new HashMap<String, StringBuffer>(1);
		private ManagementConsole mc = new ManagementConsole();
		
		public void handle(HttpExchange exchange) throws IOException {
			String requestURL = "";
			StringBuffer responseBuffer = new StringBuffer();
//			responseBuffer
//					.append("<b> This is the HTTP Server Home Page.... </b><BR>");
//			responseBuffer.append("The HTTP Client request is ....<BR>");
			int code;
			DataOutputStream output = null;
			OutputStream os = exchange.getResponseBody();
			Headers reqHeaders = exchange.getRequestHeaders();
			Headers respHeaders = exchange.getResponseHeaders();
			Set<String> keySet = reqHeaders.keySet();

			List<String> b = reqHeaders.get("Host");
			URL reqURL = new URL("http://".concat(b.toString().substring(1,
					b.toString().length() - 1)));
			System.out.println("URL to be checked: " + reqURL.toString());
			// HttpURLConnection urlconn = (HttpURLConnection)
			// reqURL.openConnection();
			code = openingConnection(reqURL, reqHeaders, respHeaders,
					responseBuffer);
			System.out.println("Opening connection now");
			exchange.sendResponseHeaders(code, responseBuffer.length());
			os.write(responseBuffer.toString().getBytes());

			Iterator<String> it = keySet.iterator();
			// // while (it.hasNext()) {
			// // String key = it.next();
			// // List<String> values = reqHeaders.get(key);
			// // System.out.println(key + " = " + values.toString());
			// // String s = key + " = " + values.toString() + "\n";
			// // if (key.equals("Cookie")) {
			// // System.out.println("Cookie handling");
			// // String myCookie;
			// //
			// // //
			// reddit_first=%7B%22organic_pos%22%3A%2012%2C%20%22firsttime%22%3A%20%22first%22%7D
			// // myCookie = values.toString().substring(1,
			// values.toString().indexOf(';'));
			// // String cookieName = myCookie.substring(0,
			// myCookie.indexOf('='));
			// // String cookieValue =
			// myCookie.substring(myCookie.indexOf('=')+1, myCookie.length());
			// //
			// // System.out.println(cookieName);
			// // System.out.println(cookieValue);
			// // if(keySet.contains("Host")){
			// // System.out.println("Found Host");
			// // int index= values.indexOf("Host");
			// // requestURL = values.get(index).substring(1,
			// // values.toString().length() - 1);
			// // cookieHandling(requestURL, cookieName, cookieValue);
			// // }
			// // }
			// // if (key.equals("Host")) {
			// // requestURL = values.toString().substring(1,
			// // values.toString().length() - 1);
			// // System.out.println("URL to be checked: " + requestURL);
			// //
			// // code = sendGetRequest(requestURL, responseBuffer);
			// //
			// // String response = exchange.getRequestMethod();
			// // System.out.println("Response: " + response + "\nContext: "
			// // + exchange.getHttpContext());
			// // Headers respHeaders = exchange.getResponseHeaders();
			// // respHeaders.set("Content-Type", "text/html");
			// //
			// // exchange.sendResponseHeaders(code, responseBuffer.length());
			// // }
			//
			// os.write(responseBuffer.toString().getBytes());
			// }
			os.flush();
			os.close();
			exchange.close();
			System.out.println("\tHashtable size: "+cache.size());
		}
		
		public int openingConnection(URL url, Headers reqHeaders,
				Headers respHeaders, StringBuffer responseBuffer)
				throws IOException {
			int return_code = 0;
			InetAddress address = InetAddress.getByName(url.getHost());
			String ip = address.getHostAddress(); // gets urls address in the
													// form 0.0.0.0
			System.out.println("IP is: " + ip);
			HttpURLConnection httpcon = null;

			if (urlIsSafe(ip)) {
				System.out.println("SAFE TO PROCEED");
				httpcon = (HttpURLConnection) url.openConnection();
				httpcon.setUseCaches(false);
				httpcon.setDoInput(true); // true if we want to read server's
											// response
				httpcon.setDoOutput(false); // false indicates this is a GET
											// request
				String tmp;

				if (reqHeaders.containsKey("Cookie")) {
					System.out.println("Cookie found");
					List<String> cookie = reqHeaders.get("Cookie");
					Iterator<String> it = cookie.iterator();
					while (it.hasNext()) {
						String mycookie = it.next();
						System.out.println(mycookie);
						mycookie = mycookie.substring(0, mycookie.length());
						httpcon.setRequestProperty("Cookie", mycookie);
					}
				}
				if (cache.containsKey(ip)) {
					System.out.println("Website in cache!");
					responseBuffer.append(cache.get(ip));
					System.out.println("Website: "+ip+"/tStringbuffer length: "+responseBuffer.length());
					//return return_code = 200;
				} else {
					BufferedReader input = new BufferedReader(
							new InputStreamReader(httpcon.getInputStream()));
					System.out.println(input.toString().length());
					while ((tmp = input.readLine()) != null) {
						responseBuffer.append(tmp);
					}
					cache.put(ip, responseBuffer);
					mc.print_to_cache_screen("Website: "+ip+"/tStringbuffer length: "+responseBuffer.length());
					input.close();
				}
				
				return_code = 200;
				
				// add cache name to GUI
				httpcon.connect();
			}
			respHeaders.set("Content-Type", "text/html");

			return return_code;
		}
		public boolean urlIsSafe(String url) {
//			UrlHandling uh = new UrlHandling();
//			String[] DataBase_BlockedUrls = uh.getBlockedUrls();
//			for (int i = 0; i < DataBase_BlockedUrls.length; i++) {
//				if (url.equals(DataBase_BlockedUrls[i]))
//					return false;
//			}
//			
			
			return !mc.blockedIP(url);
		}
	}
}
