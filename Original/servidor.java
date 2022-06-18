package Original;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class servidor {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1000);
            Socket conn = server.accept();

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            DataInputStream in = new DataInputStream(conn.getInputStream());

            int a = in.readInt();
            System.out.println(a);

            double b = in.readDouble();

            System.out.println(b);

            byte[] buff = new byte[4];

            read(in, buff, 0, 4);

            String str = new String(buff, "UTF-8");

            System.out.println(str);

            out.write(str.toUpperCase().getBytes());

            buff = new byte[5 * 8];

            read(in, buff, 0, 5*8);

            ByteBuffer bData = ByteBuffer.wrap(buff);

            for (int i = 0; i < 5; i++){
                System.out.println(bData.getDouble());
            }

            server.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }
}
