import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TLSClient {
    public static void main(String[] arstring) {
        try {
            // Create and load the TrustStore object
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(new FileInputStream("truststore.jks"), "truststorePassword".toCharArray());

            // Create and initialize the TrustManagerFactory object
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ts);

            // Create and initialize the SSLContext object
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, tmf.getTrustManagers(), null);

            // Create the SSLSocketFactory
            SSLSocketFactory ssf = sc.getSocketFactory();
            // Create and connect the SSL socket to the server
            SSLSocket s = (SSLSocket) ssf.createSocket("localhost", 8888);

            // Create input and output streams for communication
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));

            // Send a message to the server
            String out = "Hello, World!!!!";
            w.write(out, 0, out.length());
            w.newLine();
            w.flush();

            // Receive a message from the server
            String in = r.readLine();
            System.out.println("Received: " + in);

            // Close the streams and the socket
            w.close();
            r.close();
            s.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
