package SSL;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

class cliente {
    public static void main(String[] args) {
        try {

            int portDes = Integer.parseInt(args[0]);

            Socket conn = new Socket("localhost", portDes * 1000);
            //DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            DataInputStream in = new DataInputStream(conn.getInputStream());

            double resp = in.readDouble();

            System.out.println(resp);

            /* out.writeInt(123);
            out.writeDouble(1234567890.1234567890);
            out.write("Hola".getBytes());

            byte[] buff = new byte[4];
            
            //in.read(buff, 0,4);
            read(in, buff, 0, 4);

            System.out.println(new String(buff, "UTF-8"));

            ByteBuffer b = ByteBuffer.allocate(5*8);

            b.putDouble(1.1);
            b.putDouble(1.2);
            b.putDouble(1.3);
            b.putDouble(1.4);
            b.putDouble(1.5);

            byte[] a = b.array();

            out.write(a); */

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while(longitud > 0){
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }

}
