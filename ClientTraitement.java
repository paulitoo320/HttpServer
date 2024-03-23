import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class ClientTraitement implements Runnable {
    private Socket socketClient;

    public ClientTraitement(Socket socket) {
        this.socketClient = socket;
    }

    @Override
    public void run() {
        try (BufferedReader entree = new BufferedReader(new InputStreamReader(socketClient.getInputStream(), "UTF-8"));
             BufferedWriter sortie = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream(), "UTF-8"))) {

            String ligne = entree.readLine();
            if (ligne != null && !ligne.isEmpty()) {
                String[] tokens = ligne.split(" ");
                if (tokens.length >= 3 && tokens[0].equals("GET")) {
                    String cheminRelatif = tokens[1];
                    Path dossierRacine = Paths.get("dossierRacine"); // Remplacez par votre chemin réel

                    Path cheminDemande = dossierRacine.resolve(cheminRelatif.substring(1)).normalize();
                    if (!cheminDemande.startsWith(dossierRacine)) {
                        sortie.write("HTTP/1.1 403 Forbidden\r\n\r\n<h1>403 Forbidden</h1>");
                    } else if (Files.exists(cheminDemande)) {
                        if (Files.isDirectory(cheminDemande)) {
                            String style = "<style>" +
                                    "body { font-family: Arial, sans-serif; line-height: 1.6; }" +
                                    "ul { list-style-type: none; padding: 0; display: flex; flex-wrap: wrap; }" +
                                    "li { margin: 5px; flex: 0 1 30%; box-sizing: border-box; }" + // Adjust so that 3 items fit per row
                                    "a { text-decoration: none; color: blue; }" +
                                    "a:hover { text-decoration: underline; }" +
                                    "hr { margin-top: 20px; width: 100%; }" +
                                    "</style>";


                            // Séparer les répertoires et les fichiers
                            List<Path> dirs = Files.list(cheminDemande).filter(Files::isDirectory).collect(Collectors.toList());
                            List<Path> files = Files.list(cheminDemande).filter(Files::isRegularFile).collect(Collectors.toList());

                            String filesList = files.stream()
                                    .map(dossierRacine::relativize)
                                    .map(path -> String.format("<li><a href=\"/%s\">&#128196; %s</a></li>", path.toString(), path.getFileName()))
                                    .collect(Collectors.joining());

                            String dirsList = dirs.stream()
                                    .map(dossierRacine::relativize)
                                    .map(path -> String.format("<li><a href=\"/%s\">&#128193; %s</a></li>", path.toString(), path.getFileName()))
                                    .collect(Collectors.joining());

                            sortie.write("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n<html><head>" + style + "</head><body><ul>" + filesList + "</ul><hr><ul>" + dirsList + "</ul></body></html>");
                        } else {
                            byte[] contenuFichier = Files.readAllBytes(cheminDemande);
                            String typeMIME = Files.probeContentType(cheminDemande);

                            sortie.write("HTTP/1.1 200 OK\r\nContent-Type: " + typeMIME + "; charset=UTF-8\r\nContent-Length: " + contenuFichier.length + "\r\n\r\n");
                            sortie.flush();

                            OutputStream sortieFichier = socketClient.getOutputStream();
                            sortieFichier.write(contenuFichier, 0, contenuFichier.length);
                            sortieFichier.flush();
                        }
                    } else {
                        sortie.write("HTTP/1.1 404 Not Found\r\n\r\n<h1>404 Not Found</h1>");
                    }
                    sortie.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socketClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
