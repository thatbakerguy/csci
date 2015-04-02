import java.io.*;
import java.net.*;
import java.util.*;

public final class Web

{

	public static void main(String argv[]) throws Exception
	{
		// set port number
		int port = 6789;
		ServerSocket WebSocket = new ServerSocket(port);
		while (true) {
		Socket connectionSocket = WebSocket.accept();
		HttpRequest request = new HttpRequest(connectionSocket);
		Thread thread = new Thread(request); 
		thread.start();
		}
	}
}
