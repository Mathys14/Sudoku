import javax.swing.*;


public class Start {
    public static void start() {
        Configuration config = new Configuration();


        JFrame terminal = new JFrame("Startup");
        terminal.setSize(600, 400);
        STools.centerFrame(terminal);
        terminal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
        config.initializeGrid(terminal, config);     

        terminal.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            start();
        });
    }
}
