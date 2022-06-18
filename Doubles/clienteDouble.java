package Doubles;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class clienteDouble {
    public static void main(String[] args) {
        try {
            Socket conn = null;
            // Intento de reconexión
            for (;;){
                try {
                    conn = new Socket("localhost", 1000);
                    break;
                }catch(Exception ex){
                    Thread.sleep(3000);
                }
            }
            
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());


            //Primera parte 
            //Solo dobles
            //sendDoubles(out);

            //Segunda parte
            //Bytebuffer
            //sendBuffer(out);

            // Nota: Se tardó 30 ms en mandar todos, utilizando writeDouble

            // Nota: Se tardó 0 ms en mandar todos, utilizando ByteBuffer

            // Resulta mas eficiente empacarlos

            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void sendDoubles(DataOutputStream out) throws IOException{
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 10000; i++){
            out.writeDouble( (double) i );
        }
        long end = System.currentTimeMillis();
        System.out.println("Total: " + (end - start) + " ms");
    }

    static void sendBuffer(DataOutputStream out) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(10000 * 8);

        for (int i = 1; i <= 10000; i++) {
            b.putDouble((double) i);
        }

        long start = System.currentTimeMillis();

        out.write(b.array());

        long end = System.currentTimeMillis();

        System.out.println("Total: " + (end - start) + " ms");
    }

}
