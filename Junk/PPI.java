package Junk;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PPI {

    static class Worker extends Thread {
        Socket sck;
        int id;
        Object obj = new Object();
        double resp = 0;

        Worker(Socket cnx, int id) {
            this.sck = cnx;
            this.id = id;
        }

        @Override
        public void run() {
            try{
                DataOutputStream out = new DataOutputStream(this.sck.getOutputStream());
                for(int i = 0; i <= 999999; i++){
                    synchronized(obj){
                        this.resp += 4.0/(8*i+2*(this.id-2)+3);
                    }
                }
                out.writeDouble(-resp);
            }catch(Exception ex){
                ex.printStackTrace();
                System.exit(1);
            }
            
        }
    }

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Uso: java PPI {No_Nodo}");
            System.exit(1);
        }
        
        int noExc = Integer.parseInt(args[0]);

        if (noExc == 0){
            // TODO Arrancar el cliente
        }else if((noExc % 2) != 0){
            // TODO implementar par
        }else {
            try {
                ServerSocket socket = new ServerSocket(noExc * 1000);
                Socket cn = socket.accept();
                Worker wk = new Worker(cn, noExc);
                wk.start();
                wk.join();
                cn.close();
                socket.close();
            }catch (Exception  ex){
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
}
