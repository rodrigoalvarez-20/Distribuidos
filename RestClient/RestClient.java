package RestClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import RestClient.models.Usuario;

public class RestClient {

    private static String baseUrl = "http://20.225.168.70/Servicio/rest/ws/";
    public static void main(String[] args) {
        int opcUsr;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Menú");
            System.out.println("1. Añadir usuario");
            System.out.println("2. Consulta usuario");
            System.out.println("3. Borrar usuario");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opcion: ");
            opcUsr = sc.nextInt();

            if(opcUsr == 1){
                addUser();
            }
        }while(opcUsr != 4);

        sc.close();
    }

    private static void addUser(){
        Scanner sc = new Scanner(System.in);
        Usuario usr = new Usuario();
        System.out.print("Introduce el nombre: ");
        usr.nombre = sc.nextLine();
        System.out.print("Introduce el apellido paterno: ");
        usr.apellido_paterno = sc.nextLine();
        System.out.print("Introduce el apellido materno: ");
        usr.apellido_materno = sc.nextLine();
        System.out.print("Introduce el email: ");
        usr.email = sc.nextLine();
        System.out.print("Introduce el telefono: ");
        usr.telefono = sc.nextLine();
        System.out.print("Introduce el genero (M o F): ");
        usr.genero = sc.nextLine();
        System.out.print("Introduce la fecha de nacimiento (YYYY-mm-dd): ");
        usr.fecha_nacimiento = sc.nextLine();
        usr.foto = null;

        System.out.println(usr);
        
       // makeRequest("alta_usuario", userData);
        sc.close();
    }

    private static void makeRequest(String path, HashMap<String, String> params){
        try {
            URL restUrl = new URL(baseUrl + path);
            HttpURLConnection con = (HttpURLConnection) restUrl.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Host", "20.225.168.70");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String encParams = encodeParams(params);
            System.out.println(encParams);
            byte[] reqData = encParams.getBytes(StandardCharsets.UTF_8);
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(reqData);
            os.flush();
            os.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());

        } catch (MalformedURLException urlEx) {
            System.out.println("Error en la URL");
            System.out.println(urlEx.getMessage());
            System.exit(-1);
        } catch (IOException conErr) {
            System.out.println("Error al codificar los parametros");
            System.out.println(conErr.getLocalizedMessage());
            System.exit(-1);
        }
    }


    private static String encodeParams(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }


}
