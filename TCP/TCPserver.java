import java.io.*;
import java.net.*;
class TCPserver {
   public static void main(String argv[]) throws Exception
      {
         String clientSentence;
         String capitalizedSentence;
         ServerSocket welcomeSocket = new ServerSocket
         (6789);//ip set in client this port is important.

         while(true) {
            Socket connectionSocket = welcomeSocket.
            accept();
            BufferedReader inFromClient =
               new BufferedReader(new InputStreamReader(
                  connectionSocket.getInputStream()));
            DataOutputStream outToClient =
               new DataOutputStream(
                  connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            capitalizedSentence = 
                  clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);
         }
      }
}
