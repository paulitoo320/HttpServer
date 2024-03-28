import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;


public class ServeurInterface {
    private static boolean serveurDemarre = false; // Pour suivre l'état du serveur
    private static final HttpServer serveur = new HttpServer(); // Instance de votre serveur
    private static JButton startStopButton; // Bouton pour démarrer/arrêter le serveur

    public static void main(String[] args) {
        JFrame frame = new JFrame("HTTP-SERVER");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(128, 0, 128)); // Couleur de fond

        // En-tête
        JLabel header = new JLabel("HTTP-SERVER", SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(Color.yellow);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(header, BorderLayout.NORTH);

        // Panneau d'informations
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(128, 0, 128));
        JLabel addressLabel = new JLabel("127.0.0.1:8080", SwingConstants.CENTER);
        addressLabel.setForeground(Color.white);
        JLabel rootLabel = new JLabel("Document Root : dossierRacine", SwingConstants.CENTER);
        rootLabel.setForeground(Color.white);
        infoPanel.add(addressLabel);
        infoPanel.add(rootLabel);
        frame.add(infoPanel, BorderLayout.CENTER);

        // Boutons de démarrage/arrêt et d'ouverture
        startStopButton = new JButton("Démarrer");
        JButton openSiteButton = new JButton("Ouvrir le site");

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startStopButton);
        buttonPanel.add(openSiteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Action pour démarrer/arrêter le serveur
        startStopButton.addActionListener(e -> {
            if (!serveurDemarre) {
                startStopButton.setText("Arrêter");
                try {
                    serveur.demarrer(8080); // Démarrer le serveur
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                startStopButton.setText("Démarrer");
                try {
                    serveur.arreter(); // Arrêter le serveur
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            serveurDemarre = !serveurDemarre; // Mettre à jour l'état du serveur
        });

        // Action pour ouvrir le site dans le navigateur
        openSiteButton.addActionListener(e -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("http://127.0.0.1:8080"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Finalisation de la fenêtre
        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setVisible(true); // Rendre la fenêtre visible
    }
}
