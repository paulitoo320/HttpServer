import java.io.*;
import java.net.*;

public class HttpServer {
    private ServerSocket serveurSocket;

    public void demarrer(int port) throws IOException {
        serveurSocket = new ServerSocket(port);
        while (true) {
            Socket socketClient = serveurSocket.accept();
            traiterRequeteClient(socketClient);
            socketClient.close();
        }
    }

    private void traiterRequeteClient(Socket socketClient) throws IOException {
        BufferedWriter sortie = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
        sortie.write("HTTP/1.1 200 OK\r\n\r\nBonjour, monde!");
        sortie.flush();
    }

    public static void main(String[] args) throws IOException {
        HttpServer serveur = new HttpServer();
        serveur.demarrer(8080);
    }
}
