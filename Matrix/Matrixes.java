package Matrix;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Matrixes {

    static class Worker extends Thread {
        String hostname;
        Object obj = new Object();
        int serverPort;
        double[][] arrA;
        double[][] arrB;
        double[][] arrC;

        Worker(String host, int port, double[][] arrA, double[][] arrB) {
            this.hostname = host;
            this.serverPort = port;
            this.arrA = arrA;
            this.arrB = arrB;
        }

        @Override
        public void run() {
            try {
                Socket conn = null;
                for (;;) {
                    try {
                        conn = new Socket(this.hostname, 1000 + this.serverPort);
                        break;
                    } catch (Exception ex) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                DataInputStream input = new DataInputStream(conn.getInputStream());

                out.writeInt(this.arrA.length); // M
                out.writeInt(this.arrA[0].length); // N
                out.flush();
                for (double[] i : arrA)
                    for (double j : i)
                        out.writeDouble(j);
                out.flush();

                for (double[] i : arrB)
                    for (double j : i)
                        out.writeDouble(j);
                out.flush();

                int newSize = arrA.length;

                double[][] matResp = new double[newSize][newSize];

                for (int i = 0; i < newSize; i++)
                    for (int j = 0; j < newSize; j++)
                        synchronized (obj) {
                            matResp[i][j] = input.readDouble();
                        }

                conn.close();

                this.arrC = matResp;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Parametros faltantes");
            System.out.println("Uso: java Matrixes {tipo_programa} {tamaño_matriz} {dir_nodo1} {dir_nodo2} {dir_nodo3} || Si tipo_programa = 0");
            System.out.println("Uso: java Matrixes {tipo_programa} || Si tipo_programa = 0");
        }

        int pType = Integer.parseInt(args[0]);

        if (pType == 0) {
            int sizeMatrix = Integer.parseInt(args[1]);
            String node1 = args[2];
            String node2 = args[3];
            String node3 = args[4];
            // El cliente, debe de inicializar la matriz
            double[][] matrixA = new double[sizeMatrix][sizeMatrix];
            double[][] matrixB = new double[sizeMatrix][sizeMatrix];

            initMatrix(matrixA, matrixB, sizeMatrix);

            transpose(matrixB, sizeMatrix);

            // Dividir en 2 cada una de las matrices
            ArrayList<double[][]> mtA = splitMatrix(matrixA, sizeMatrix);
            ArrayList<double[][]> mtB = splitMatrix(matrixB, sizeMatrix);

            try {
                Worker w1 = new Worker(node1, 1, mtA.get(0), mtB.get(0));
                Worker w2 = new Worker(node2, 2, mtA.get(0), mtB.get(1));
                Worker w3 = new Worker(node3, 3, mtA.get(1), mtB.get(0));
                
                w1.start();
                w2.start();
                w3.start();

                w1.join();
                w2.join();
                w3.join();

                double[][] c1 = w1.arrC;
                double[][] c2 = w2.arrC;
                double[][] c3 = w3.arrC;
                
                double[][] c4 = multiplyMatrixes(mtA.get(1), mtB.get(1), sizeMatrix);
                
                double[][] matC = new double[sizeMatrix][sizeMatrix];

                for (int i = 0; i < sizeMatrix; i++) {
                    for (int j = 0; j < sizeMatrix; j++) {
                        if (i < sizeMatrix / 2 && j < sizeMatrix / 2) {
                            matC[i][j] = c1[i][j];
                        } else if (i < sizeMatrix / 2 && j >= sizeMatrix / 2) {
                            matC[i][j] = c2[i][j % (sizeMatrix / 2)];
                        } else if (i >= sizeMatrix / 2 && j < sizeMatrix / 2) {
                            matC[i][j] = c3[i % (sizeMatrix / 2)][j];
                        } else if (i >= sizeMatrix / 2 && j >= sizeMatrix / 2) {
                            matC[i][j] = c4[i % (sizeMatrix / 2)][j % (sizeMatrix / 2)];
                        }
                    }

                }

                double check = checksum(matC);

                System.out.println("El checksum de la matriz resultante es: " + check);

                if (sizeMatrix < 100) {
                    System.out.println("Matrices A:");
                    displayMatrix(matrixA);
                    System.out.println("Matriz B (transpuesta):");
                    displayMatrix(matrixB);
                    System.out.println("Matriz resultante:");
                    displayMatrix(matC);
                } 
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            MatrixServer mtsrv = new MatrixServer(pType);
            mtsrv.listenConnection();
        }

    }

    private static void displayMatrix(double[][] matrix) {
        for (double[] i : matrix) {
            for (double j : i) {
                System.out.print("" + j + " ");
            }
            System.out.println();
        }
    }

    private static void initMatrix(double[][] matrixA, double[][] matrixB, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixA[i][j] = (double) i + 5 * j;
                matrixB[i][j] = (double) 5 * i - j;
            }
        }
    }

    private static void transpose(double[][] matrix, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i > j) {
                    double temp = matrix[i][j];
                    matrix[i][j] = matrix[j][i];
                    matrix[j][i] = temp;
                }
            }
        }
    }

    private static double[][] multiplyMatrixes(double[][] matrixA, double[][] matrixB, int size) {
        int newSize = size / 2;
        double[][] res = new double[newSize][newSize];

        for(int i = 0; i < newSize; i++){
            for(int j = 0; j < newSize; j++){
                res[i][j] = 0;
            }
        }

        for (int i = 0; i < newSize; i++) // Filas
            for (int j = 0; j < newSize; j++) // Columnas
                for (int k = 0; k < size; k++) //Iterar sobre todas las columnas de la segunda matriz
                    res[i][j] += (matrixA[i][k] * matrixB[j][k]);
        return res;
    }

    private static ArrayList<double[][]> splitMatrix(double[][] matrix, int size) {
        ArrayList<double[][]> matrixes = new ArrayList<>();
        int newSize = size / 2;
        double[][] m1 = new double[newSize][size];
        double[][] m2 = new double[newSize][size];

        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < size; j++) {
                m1[i][j] = matrix[i][j];
                m2[i][j] = matrix[newSize + i][j];
            }
        }

        matrixes.add(m1);
        matrixes.add(m2);

        return matrixes;
    }

    private static double checksum(double[][] matrix) {
        double checksum = 0;

        for (double[] i : matrix) {
            for (double j : i) {
                checksum += j;
            }
        }

        return checksum;

    }

}
