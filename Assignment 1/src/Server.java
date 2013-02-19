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
	
	
	@SuppressWarnings("restriction")
	public static void main(String[] args) throws IOException {
		HttpServer hs = HttpServer.create(new InetSocketAddress(8080),0);
		hs.createContext("/", new Handler());
		System.out.println(hs.getAddress());
		hs.setExecutor(Executors.newCachedThreadPool());
		
		hs.start();

		System.out.println("Web Proxy Server Started. . .");
		
	}
	/*
	 * Code to handle http requests from client
	 */

	@SuppressWarnings("restriction")
	public static class Handler implements HttpHandler {
		private HashMap<String, StringBuffer> cache = new HashMap<String, StringBuffer>(1);
		private ManagementConsole mc = new ManagementConsole();
		
		public void handle(HttpExchange exchange) throws IOException {
			s("... NEW REQUEST ...\n");
			s(exchange.getLocalAddress() + " has connected to the server"+'\n');
			StringBuffer responseBuffer = new StringBuffer(); // write body
//			responseBuffer
//					.append("<b> This is the HTTP Server Home Page.... </b><BR>");
//			responseBuffer.append("The HTTP Client request is ....<BR>");
			int code;
			OutputStream os = exchange.getResponseBody(); // will forward request to browser
			Headers reqHeaders = exchange.getRequestHeaders();  // request headers from client
			Headers respHeaders = exchange.getResponseHeaders(); // send headers to browser

			List<String> b = reqHeaders.get("Host"); // Find "Host" Header
			URL reqURL = new URL("http://".concat(b.toString().substring(1,
					b.toString().length() - 1))); // getting url
			s("Client requested url: " + reqURL.toString() +'\n');
			code = openingConnection(reqURL, reqHeaders, respHeaders,
					responseBuffer); // return http code and forward to browser
			System.out.println("Opening connection now");
			exchange.sendResponseHeaders(code, responseBuffer.length());
			os.write(responseBuffer.toString().getBytes());


			os.flush();
			os.close();
			exchange.close();
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
						respHeaders.set("Set-Cookie", mycookie);
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
				Set<String> keySet = reqHeaders.keySet();
				Iterator<String> it = keySet.iterator();
				while(it.hasNext()) {
					String h = it.next();
					List<String> values = reqHeaders.get(h);
					s("Request: " + h + " = " + values.toString() + "\n");
				}
				return_code = 200;
				
				// add cache name to GUI
				httpcon.connect();
			} else {
				responseBuffer
				.append("<b> Requested URL has been blocked by an Admin.... </b><BR>");
		responseBuffer.append("Please contact an admin to allow access ....<BR>");
				return_code = 404;
			}
			respHeaders.set("Content-Type", "text/html");

			return return_code;
		}
		public boolean urlIsSafe(String url) {
			return !mc.blockedIP(url);
		}
		private void s(String send) {
			mc.print_to_log_screen(send);
		}
	}
}
