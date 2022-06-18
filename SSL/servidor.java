package Original;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.net.ssl.SSLServerSocketFactory;

public class servidor {
    public static void main(String[] args) {
        try {
            SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

            ServerSocket server = socket_factory.createServerSocket(1000);
            Socket conn = server.accept();


            server.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
