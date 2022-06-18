package Junk;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastRecv {
    public static void main(String[] args) {
        
        System.setProperty("java.net.preferIPv4Stack", "true");
        try{
            InetAddress group = InetAddress.getByName("230.0.0.0");

            MulticastSocket sck = new MulticastSocket(5000);

            sck.joinGroup(group);

            byte[] buff = new byte[5];

            DatagramPacket dPack = new DatagramPacket(buff, buff.length);

            sck.receive(dPack);

            byte[] rcvData = dPack.getData();

            System.out.println(new String(rcvData, "UTF-8"));

            sck.leaveGroup(group);

            sck.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }
        



    }
}
