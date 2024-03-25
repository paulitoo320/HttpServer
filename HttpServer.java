import java.io.*;
import java.net.*;

public class HttpServer {
    private ServerSocket serveurSocket;

    public void demarrer(int port) throws IOException {
        serveurSocket = new ServerSocket(port);

        while (!serveurSocket.isClosed()) {
            Socket socketClient = serveurSocket.accept();
            ClientTraitement traitementClient = new ClientTraitement(socketClient);
            new Thread(traitementClient).start();
        }
    }

    public void arreter() throws IOException {
        serveurSocket.close();
    }

    public static void main(String[] args) throws IOException {
        HttpServer serveur = new HttpServer();
        serveur.demarrer(8080);
    }
}
