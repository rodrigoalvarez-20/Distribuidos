package Chat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Chat {

    static String MULTICAST_IP = "230.0.0.0";
    static int MULTICAST_PORT = 50000;

    static class Worker extends Thread {
        String usr_name;

        public Worker(String name){
            this.usr_name = name;
        }

        @Override
        public void run() {
            try{
                MulticastSocket mSocket = new MulticastSocket(MULTICAST_PORT);
                mSocket.joinGroup(InetAddress.getByName(MULTICAST_IP));
                while (true) {
                    byte[] rcvData = recibe_mensaje_multicast(mSocket, 255);
                    String msg = new String(rcvData, StandardCharsets.UTF_8);
                    
                    if (!msg.split(" ")[0].equals(usr_name)) {
                        System.out.println(msg.trim());
                        System.out.println("Ingrese el mensaje a enviar: ");
                    }
                }
            }catch(IOException sckError){
                System.out.println(sckError.getMessage());
                System.exit(0);
            }
        }
    }
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Uso: java Chat {nombre_usuario}");
            System.exit(0);
        }

        String usr_name = args[0];
        try {
            MulticastSocket multiSocket = new MulticastSocket();
            new Worker(usr_name).start();
            while(true){
                String msgToSend = "";
                System.out.println("Ingrese el mensaje a enviar: ");
                msgToSend = System.console().readLine();
                String msgFmt = usr_name + " dice " + msgToSend;
                ByteBuffer buffer = StandardCharsets.UTF_8.encode(msgFmt);
                envia_mensaje_multicast(buffer.array(), MULTICAST_IP ,MULTICAST_PORT);
                multiSocket.setLoopbackMode(true);
            }
        }catch(IOException socketEx){
            System.out.println(socketEx.getMessage());
            System.exit(0);
        }
    }


    static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
        socket.close();
    }

    static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException {
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }

}
