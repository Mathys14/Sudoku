
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.Border;

public class SudokuInterface extends JFrame {
    private JButton boutonResoudre;
    private JButton boutonReinitialiser;
    private JButton boutonSauvegarder;
    private boolean modeManuel = true;
    private JPanel panneauSudoku;
    private JPanel panneauBouton;
    private JTextField[][] cellulesSudoku;

    public SudokuInterface(Configuration config) {
        setTitle("Jeu du Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        initialiserComposants(config);
    }

    private void initialiserComposants(Configuration config) {
        panneauSudoku = new JPanel();
        panneauSudoku.setLayout(new GridLayout(9, 9));
        cellulesSudoku = new JTextField[9][9];

        panneauBouton = new JPanel();

        Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
        panneauSudoku.setBorder(border);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cellulesSudoku[i][j] = new JTextField(1);
                panneauSudoku.add(cellulesSudoku[i][j]);

                final int row = i; 
                final int col = j;
                cellulesSudoku[i][j].addActionListener(new ActionListener() {
                    @Override 
                    public void actionPerformed(ActionEvent e){
                        JTextField source = (JTextField) e.getSource();
                        String text = source.getText();
                        if(text.isEmpty()) {
                            config.remplirCase(row, col, 0);
                        }else {
                            int valeur = Integer.parseInt(text);
                            if(valeur < 1 || valeur > 9) {
                                JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre entre 1 et 9.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                source.setText("");
                            } else {
                                if (config.caseDisponible(row, col, valeur)) {
                                    config.remplirCase(row, col, valeur);
                                    if (config.Fin()) {
                                        JOptionPane.showMessageDialog(null, "Bravo !", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
                                    
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Impossible de rentrer cette valeur ici.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                source.setText("");
                                }
                                
                            }
                        }
                    }
                });

                cellulesSudoku[i][j].setBorder(border);

                // Mettre la valeur de la case dans la cellule correspondante
                int valeur = config.getCase(i, j);
                if (valeur != 0) {
                    cellulesSudoku[i][j].setText(Integer.toString(valeur));
                    // Vérifier si la case est fixe (présente dans la grille de départ)
                    if (config.getCaseDepart(i, j) == 1) {
                        cellulesSudoku[i][j].setEditable(false); // Marquer la cellule comme non modifiable
                        cellulesSudoku[i][j].setBackground(Color.LIGHT_GRAY);
                    } else {
                        cellulesSudoku[i][j].setBackground(Color.WHITE);
                    }
                }
            }
        }

        boutonResoudre = new JButton("Résoudre");
        boutonResoudre.setEnabled(modeManuel);
        boutonResoudre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean resolu = config.resoudreSudoku();
                if(resolu){
                    reload(config);
                } else {
                    JOptionPane.showMessageDialog(null, "La grille n'a pas de solution valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        boutonReinitialiser = new JButton("Réinitialiser");
        boutonReinitialiser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.restartGame();
                reload(config);
            }
        });

        boutonSauvegarder = new JButton("Sauvegarder");
        boutonSauvegarder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SudokuFileHandler sFileHandler2 = new SudokuFileHandler(config);
                sFileHandler2.SudokuFileSave(config);
                sFileHandler2.setVisible(true);
            }
        });

        panneauBouton.add(boutonResoudre);
        panneauBouton.add(boutonReinitialiser);
        panneauBouton.add(boutonSauvegarder);

        add(panneauSudoku, BorderLayout.CENTER);
        add(panneauBouton, BorderLayout.SOUTH);
    }

    public void reload(Configuration config) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int valeur = config.getCase(i, j);
                if (valeur != 0) {
                    cellulesSudoku[i][j].setText(Integer.toString(valeur));
                    if (config.getCaseDepart(i, j) == 1) {
                        cellulesSudoku[i][j].setEditable(false);
                        cellulesSudoku[i][j].setBackground(Color.LIGHT_GRAY);
                    } else {
                        cellulesSudoku[i][j].setEditable(true);
                        cellulesSudoku[i][j].setBackground(Color.WHITE);
                    }
                } else {
                    cellulesSudoku[i][j].setText("");
                    cellulesSudoku[i][j].setEditable(true);
                    cellulesSudoku[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    public static void main(String[] args) {
        Configuration config = new Configuration();
        SwingUtilities.invokeLater(() -> {
            SudokuInterface sudokuInterface = new SudokuInterface(config);
            sudokuInterface.setVisible(true);
        });
    }
}
