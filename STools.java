import javax.swing.*;
import java.awt.*;

public class STools {

    public static void centerFrame(JFrame frame) {
        // Obtenir la taille de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Obtenir la taille de la fenêtre
        Dimension frameSize = frame.getSize();

        // Calculer la position centrée
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;

        // Définir la position de la fenêtre
        frame.setLocation(x, y);
    }
}