package Junk;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastSender {
    public static void main(String[] args) {

        //java prg Hola 230.0.0.0 5000

        String message = args[0];
        String host = args[1];
        int port = Integer.parseInt(args[2]);

        byte[] buff = message.getBytes();

        System.setProperty("java.net.preferIPv4Stack", "true");

        try {
            DatagramSocket dGram = new DatagramSocket();
            InetAddress group = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(buff, buff.length, group, port);

            dGram.send(packet);

            dGram.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
