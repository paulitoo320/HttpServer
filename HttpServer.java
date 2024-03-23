import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public class HttpServer {
    private ServerSocket serveurSocket;
    private ExecutorService pool;
    private Queue<Socket> fileAttente = new LinkedList<>();
    private static final int TAILLE_INITIALE_POOL = 20;
    private static final int TAILLE_MAX_POOL = 40;
    private static final int TAILLE_FILE_ATTENTE = 20;

    public void demarrer(int port) throws IOException {
        serveurSocket = new ServerSocket(port);
        pool = new ThreadPoolExecutor(
                TAILLE_INITIALE_POOL,
                TAILLE_MAX_POOL,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(TAILLE_FILE_ATTENTE));

        Thread gestionnaireFile = new Thread(() -> {
            while (!Thread.interrupted()) {
                Socket socketClient;
                synchronized (this) {
                    if (fileAttente.isEmpty() ||
                            ((ThreadPoolExecutor) pool).getActiveCount() >= TAILLE_MAX_POOL) {
                        continue;
                    }
                    socketClient = fileAttente.poll();
                }
                if (socketClient != null) {
                    pool.execute(new ClientTraitement(socketClient));
                }
            }
        });
        gestionnaireFile.start();

        while (!serveurSocket.isClosed()) {
            Socket socketClient = serveurSocket.accept();
            synchronized (this) {
                if (((ThreadPoolExecutor) pool).getActiveCount() < TAILLE_MAX_POOL ||
                        ((ThreadPoolExecutor) pool).getQueue().size() < TAILLE_FILE_ATTENTE) {
                    pool.execute(new ClientTraitement(socketClient));
                } else if (fileAttente.size() < TAILLE_FILE_ATTENTE) {
                    fileAttente.add(socketClient);
                } else {
                    socketClient.close(); // Refuser la nouvelle connexion
                }
            }
        }
    }

    public void arreter() throws IOException {
        serveurSocket.close();
        pool.shutdown(); // Arrêter le pool de threads
    }

    public static void main(String[] args) throws IOException {
        HttpServer serveur = new HttpServer();
        serveur.demarrer(8080); // Démarrer avec des paramètres prédéfinis
    }
}
