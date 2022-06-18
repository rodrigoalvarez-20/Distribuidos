package Doubles;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class servidorDouble {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1000);
            Socket conn = server.accept();
            DataInputStream in = new DataInputStream(conn.getInputStream());

            // Recibir doubles

            //recvDoubles(in);

            // Recibir el byteBuffer

            recvBuffer(in);

            // Nota: Se tardó 164 ms en recibirlos utilizando el readDouble

            // Nota: Se tardó 19 ms en recibirlos utilizando el ByteBuffer

            server.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws IOException {
        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }

    static void recvBuffer(DataInputStream in) throws IOException{
        byte[] buff = new byte[10000 * 8];

        long start = System.currentTimeMillis();

        read(in, buff, 0, 10000 * 8);

        ByteBuffer bData = ByteBuffer.wrap(buff);

        long end = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            System.out.println(bData.getDouble());
        }

        System.out.println("Total: " + (end - start) + " ms");

    }

    static void recvDoubles(DataInputStream in) throws IOException{
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 10000; i++) {
            System.out.println(in.readDouble());
        }
        long end = System.currentTimeMillis();
        System.out.println("Total: " + (end - start) + " ms");
    }

}
