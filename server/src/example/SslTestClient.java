/**
 * Simple SslClient that implements Handshake
 */

import java.io.DataOutputStream;
import java.io.DataInputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SslTestClient {
  private SSLSocket socket;

  SslTestClient() throws Exception {
    SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    socket = (SSLSocket) factory.createSocket(System.getProperty("HOST", "127.0.0.1"),
            Integer.parseInt(System.getProperty("PORT", "5432")));
  }

  void run() throws Exception {
    DataOutputStream os = new DataOutputStream(socket.getOutputStream());
    DataInputStream is = new DataInputStream(socket.getInputStream());
    // Hello message with protocol version 0x01
    byte[] hello = { 0x01, 0x00, 0x00, 0x00, 0x01, 0x01 };
    os.write(hello);

    // Wait for Welcome message
    byte[] welcome = new byte[5];
    is.read(welcome);
    if (welcome[0] == 0x02) {
      System.out.println("Welcome received");
    } else {
      System.out.println(String.format("Wrong message received: %d", welcome[0]));
    }
  }

  public static void main(String argv[]) throws Exception {
    SslTestClient client = new SslTestClient();
    client.run();
  }
}
