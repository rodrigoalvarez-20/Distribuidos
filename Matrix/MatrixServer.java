package Matrix;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MatrixServer {
    private int serverNumber;

    public MatrixServer(int serverNumber) {
        this.serverNumber = serverNumber;
    }

    public void listenConnection() {

        try {
            ServerSocket server = new ServerSocket(1000 + serverNumber);
            Socket conn = server.accept();
            DataInputStream inputData = new DataInputStream(conn.getInputStream());
            DataOutputStream outputData = new DataOutputStream(conn.getOutputStream());

            int m = inputData.readInt();
            int n = inputData.readInt();

            double[][] matA = new double[m][n];
            double[][] matB = new double[m][n];
            
            for(int i = 0; i < m; i++ )
                for(int j = 0; j < n; j++)
                    matA[i][j] = inputData.readDouble();
            
            for(int i = 0; i < m; i++ )
                for(int j = 0; j < n; j++)
                    matB[i][j] = inputData.readDouble();
            
            double[][] respMat = multiplyMatrixes(matA, matB, m, n);
           for (double[] i : respMat)
                for (double j : i)
                    outputData.writeDouble(j);
            server.close();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }

    private static double[][] multiplyMatrixes(double[][] matrixA, double[][] matrixB, int m, int n) {
        double[][] res = new double[m][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < m ; j++)
                for (int k = 0; k < n; k++){
                    res[i][j] += (matrixA[i][k] * matrixB[j][k]);
                }
        return res;
    }

    private static void displayMatrix(double[][] matrix) {
        for (double[] i : matrix) {
            for (double j : i) {
                System.out.print("" + j + " ");
            }
            System.out.println();
        }
    }

}