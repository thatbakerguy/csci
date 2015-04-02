import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

/*
* Server to process ping requests over UDP. java PingClient host port
*this program interfaces with the server program provieded on the website and 
*is initiated by appending the ip adress of the server 127.0.0.1 for local ip and the port number after it
*/
public class PingClient
{

public static void main(String[] args) throws Exception
{
// Get command line argument.
if (args.length != 2) {
System.out.println("Required arguments: host port");
return;
}

String ServerName =args[0];	
int port = Integer.parseInt(args[1]);

DatagramSocket socket = new DatagramSocket();
InetAddress IPAddress =InetAddress.getByName(ServerName);

for(int i=0;i<10;i++){

long SendTime = System.currentTimeMillis();
String Message = "Ping "+ i + " " + SendTime + "\n";
DatagramPacket request =
new DatagramPacket(Message.getBytes(), Message.length(),IPAddress,port );
socket.send(request);
DatagramPacket reply =
new DatagramPacket(new byte[1024], 1024);

socket.setSoTimeout(1000);

try
{
socket.receive(reply);
}catch(IOException E)
{

}

Thread.sleep(1000);

}


}

private static void printData(DatagramPacket request) throws Exception
{

byte[] buf = request.getData();


ByteArrayInputStream bais = new ByteArrayInputStream(buf);
InputStreamReader isr = new InputStreamReader(bais);
BufferedReader br = new BufferedReader(isr);
String line = br.readLine();
System.out.println(
"Received from " +
request.getAddress().getHostAddress() +
": " +
new String(line) );
}
} 
