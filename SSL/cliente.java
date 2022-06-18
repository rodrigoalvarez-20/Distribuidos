package SSL;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

class cliente {
    public static void main(String[] args) {
        try {
            SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Socket conn = cliente.createSocket("localhost", 1000);
            //DataInputStream in = new DataInputStream(conn.getInputStream());

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
