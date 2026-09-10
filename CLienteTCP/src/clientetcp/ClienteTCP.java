package clientetcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTCP {

	public static void main(String[] args) {
        String host = "localhost";
        int puerto = 9090;

        String datosUsuario = "Brandon Carmona|brandon.carmona@correo.com|Password123*";

        System.out.println("[Cliente TCP] Conectándose al servidor en " + host + ":" + puerto + "...");

        try (Socket socket = new Socket(host, puerto);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        	salida.write("Brandon Carmona\n");
            salida.write("brandon.carmona@correo.com\n");
            salida.write("Password123*\n");
            
            salida.flush();
            
            System.out.println("[Cliente TCP] Datos enviados con éxito.");
        } catch (Exception e) {
            System.err.println("[Cliente TCP] Error de conexión con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
}
