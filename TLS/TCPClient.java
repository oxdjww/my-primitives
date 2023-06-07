import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 5001);
        System.out.println("Connected to the server");

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + '\n');

        String modifiedSentence = inFromServer.readLine();
        System.out.println("From Server: " + modifiedSentence);

        clientSocket.close();
    }
}
