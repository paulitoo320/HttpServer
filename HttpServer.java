import java.io.*;
import java.net.*;

public class HttpServer {
    private ServerSocket serveurSocket;
    private Thread serveurThread;
    private volatile boolean estEnFonction = false;

    public void demarrer(int port) throws IOException {
        serveurSocket = new ServerSocket(port);
        estEnFonction = true;
        serveurThread = new Thread(() -> {
            while (estEnFonction) {
                try {
                    Socket socketClient = serveurSocket.accept();
                    ClientTraitement traitementClient = new ClientTraitement(socketClient);
                    new Thread(traitementClient).start();
                } catch (IOException e) {
                    if (estEnFonction) {
                        e.printStackTrace();
                    }
                }
            }
        });
        serveurThread.start();
    }

    public void arreter() throws IOException {
        estEnFonction = false;
        serveurSocket.close();
        serveurThread.interrupt();
    }
}
