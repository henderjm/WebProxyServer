//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.URL;
//import java.net.URLConnection;
//
//
//public class blah {
//	public Server(int listen_port){
//		port = listen_port;
//		start();
//	}
////	@Override
//	public void run() {
//		//ServerSocket serversocket = null;
//		// print to GUI here, making connection
//		// try to connect to server
//		try {
//			serversocket = new ServerSocket(port);
////			serversocket.s
//		} catch(Exception e) { // print any found errors
//			System.err.println("FATAL: "+e.getMessage());
//		}
//		System.out.println("Connected ok");
//		while(true) {
//			System.out.println("Awaiting requests. . .");
//			try {
//				Socket clientsocket = serversocket.accept();
//				// get Clients IP
//				InetAddress client = clientsocket.getInetAddress();
//				// reading http request
//				if(!tested) {
//					System.out.println("HELLO");
//					URL u = new URL("http://www.yahoo.com");
//					URLConnection uc = u.openConnection();
//					String host = u.getHost();
//					int port = u.getPort();
//					String file = u.getFile();
//					String request = "GET " + file + " HTTP/1.0\r\n"
//					        + "User-Agent: MechaMozilla\r\nAccept: text/*\r\n\r\n";
//					        // This next line is problematic on non-ASCII systems
//					        byte[] b = request.getBytes();
//					System.out.println(request);
//					input = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
//					output = new DataOutputStream(clientsocket.getOutputStream());
//					processRequest(input, output);
//					System.out.println("\nfinished processing client request!");
//					tested = true;
//				}
//				
//			} catch(Exception e) {
//				System.err.println(e.getMessage());
//			}
//		}
//		
//	}
//	public void processRequest(BufferedReader input, DataOutputStream output) {
//		/*
//		 * print out url contents
//		 */
//		String tmp;
//		try {
//			while((tmp = input.readLine()) != null){
//				if(tmp.equals("")){
//					break;		// required to stop hang time
//				}
//				System.out.println("Input : " + tmp);
//			}
//			output.writeBytes(constructHeader(200));
//			input.close();
//			output.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	private String constructHeader(int http_code) {
//		String header = "";
//		header += "HTTP/1.0";
//		switch(http_code) {
//		case 200 : header += " 200 OK"; break;
//		
//		}
//		header += "\r\n";
//		header += "Connection: close\r\n";
//		header += "Server: MarksServer v0\r\n";
//		header += "Content-Type: text/html\r\n";
//		header += "\r\n"; // end of new http header shit
//		return header;
//	}
//	
//	
//}
