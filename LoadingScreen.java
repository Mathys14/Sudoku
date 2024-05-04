import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class LoadingScreen {
    public static void start() {
        Configuration config = new Configuration();


        JFrame terminal = new JFrame("Startup");
        terminal.setSize(600, 400);
        terminal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        terminal.add(scrollPane);

        // Lancer le processus de chargement
        Thread loadingThread = new Thread(() -> {
            String filePath = "includes/startup.txt"; // Chemin du fichier texte
            long delay = 100; // Délai entre les lignes (1 seconde)

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final String lineCopy = line; // Final copy of 'line'
                    // Ajouter la ligne au JTextArea
                    SwingUtilities.invokeLater(() -> textArea.append(lineCopy + "\n"));
                    try {
                        Thread.sleep(delay); // Pause pour simuler le chargement
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Terminer et fermer le JFrame
                SwingUtilities.invokeLater(() -> {
                    textArea.append("Chargement terminé, fermeture du terminal...\n");
                });

                Thread.sleep(500); // Attendre 2 secondes avant de fermer le terminal
                Sudoku.main(config);
                terminal.dispose();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        loadingThread.start(); // Démarrer le processus de chargement
        terminal.setVisible(true); // Afficher la fenêtre
        
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            start();
        });
    }
}
