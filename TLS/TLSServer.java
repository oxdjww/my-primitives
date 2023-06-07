import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TLSServer {
    public static void main(String[] arstring) {
        try {
            // Create and load the KeyStore object
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("keystore.jks"), "keystorePassword".toCharArray());

            // Create and initialize the KeyManagerFactory object
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, "keystorePassword".toCharArray());

            // Create and initialize the SSLContext object
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);

            // Create the SSLServerSocketFactory
            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            // Create and bind the SSLServerSocket to port 8888
            SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8888);

            // Establish SSL connection with the client
            System.out.println("Server started:");
            SSLSocket c = (SSLSocket) s.accept();

            // Create input and output streams for communication with the client
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
            BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));

            // Receive a message from the client
            String m = r.readLine();
            
            // Send the received message back to the client
            w.write(m, 0, m.length());
            w.newLine();
            w.flush();

            // Close the streams and the socket
            w.close();
            r.close();
            c.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
