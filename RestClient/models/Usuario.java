package RestClient.models;
import com.google.gson.*;

import RestClient.utils.AdaptadorGsonBase64;

public class Usuario {
    public String email;
    public String nombre;
    public String apellido_paterno;
    public String apellido_materno;
    public String fecha_nacimiento;
    public String telefono;
    public String genero;
    public byte[] foto;

    // @FormParam necesita un metodo que convierta una String al objeto de tipo
    // Usuario
    public static Usuario valueOf(String s) throws Exception {
        Gson j = new GsonBuilder().registerTypeAdapter(byte[].class, new AdaptadorGsonBase64()).create();
        return j.fromJson(s, Usuario.class);
    }
}