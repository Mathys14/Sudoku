import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.*;



/**
 * Classe pour représenter la configuration du Sudoku.
 */
public class Configuration {

    private int[][] grilleDepart;
    private int[][] grille;
    private String nameFile = null;

    /**
    * Constructeur de la classe Configuration.
    * Initialise la grille du Sudoku et résout une grille valide.
    */
    public Configuration() {
        // Initialisation de la grille du Sudoku
        this.grille = new int[9][9];
        this.grilleDepart = new int[9][9];
        
        // Remplissage initial avec des zéros
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.grille[i][j] = 0;
                this.grilleDepart[i][j] = 0;
            }
        }
    }

    /**
    * Initialise une grille valide de Sudoku.
    */
    public void initializeGrid(JFrame terminal, Configuration config) {
        Thread loadingThread = new Thread(() -> {
            // Générer une grille valide et la résoudre


            int essai = 1;

            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            terminal.add(scrollPane);

            textArea.append("Génération de la grille.\n\n");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'initialisation", "Erreur", JOptionPane.ERROR_MESSAGE);
                terminal.dispose();
            }

            textArea.append("Essai n°" + essai + " . . .");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'initialisation", "Erreur", JOptionPane.ERROR_MESSAGE);
                terminal.dispose();
            }

            randomGrille();

            // Vérifier si la grille est résoluble
            while (!resoudreSudoku()) {
                // Si la grille n'est pas résoluble, réinitialiser et réessayer
                this.grille = new int[9][9];
                this.grilleDepart = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        this.grille[i][j] = 0;
                        this.grilleDepart[i][j] = 0;
                    }
                }
                // Générer une nouvelle grille et la résoudre

                textArea.append(" : Failed\n");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'initialisation", "Erreur", JOptionPane.ERROR_MESSAGE);
                    terminal.dispose();
                }

                textArea.append("Nouvel essai\n");
                essai++;
                textArea.append("Essai n°" + essai + " . . .");

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'initialisation", "Erreur", JOptionPane.ERROR_MESSAGE);
                    terminal.dispose();
                }

                randomGrille();



            }


            textArea.append(" : Success\n\n");
            textArea.append("Starting . . .");

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'initialisation", "Erreur", JOptionPane.ERROR_MESSAGE);
                terminal.dispose();
            }
            
            // Réinitialiser le jeu après la génération de la grille

            terminal.dispose();

            Sudoku.main(config);

            restartGame();
        });
        
        loadingThread.start();
        
    }

    /**
    * Affiche la grille a la console de manière moche et dégueulasse.<br>
    * 
    * Ne sert plus a rien...
    */    
    public static void afficheStringGrille(int[][] grille) {
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.println(i+","+j+": " + grille[i][j] + ",");
            }
        }
    }

    /**
    * Remplit une case de la grille si celle ci peut etre remplie.
    */
    public void remplirCase(int x, int y, int valeur) {
        if (caseDisponible(x, y, valeur)) {
            this.grille[x][y] = valeur;
        }
    }

    /**
     * Regarde si la valeur donnée est dans la ligne.
     * 
     * @return boolean.
     */
    private boolean valeurEstDansLigne(int valeur, int x) {
		boolean reponse = false;
		for(int i=0; i<9; i++) {
			if(this.grille[x][i]==valeur) {
				reponse = true;
				break;
			}
		}
		return reponse;
	}
	
    /**
     * Regarde si la valeur donnée est dans la colonne.
     * 
     * @return boolean.
     */
	private boolean valeurEstDansColonne(int valeur, int y) {
		boolean reponse = false;
		for(int i=0; i<9; i++) {
			if(this.grille[i][y]==valeur) {
				reponse = true;
				break;
			}
		}
		return reponse;
	}
	
    /**
     * Regarde si la valeur donnée est dans le carré.
     * 
     * @return boolean.
     */
	private boolean valeurEstDansCarre(int valeur, int x, int y) {
		boolean reponse = false;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				if (this.grille[x+i][y+j]==valeur) {
					reponse = true;
					break;
				}
			}
		}
		return reponse;
	}

    /**
    * Regarde si la valeur peut être ajoutée a la grille.
    * 
    * @return Renvoie true si oui, false si non.
    */
    public boolean caseDisponible(int x, int y, int valeur) {
        if (valeur == 0) {
            return true;
        }

        boolean reponse = true;
		if(valeurEstDansLigne(valeur, x) || valeurEstDansColonne(valeur, y) || valeurEstDansCarre(valeur, x-x%3, y-y%3)) {
			reponse = false;
		}
		return reponse;
    }

    /**
     * @return Valeur de la case [x][y].
     */
    public int getCase(int x, int y) {
        return this.grille[x][y];
    }

    /**
     * @return Valeur de la case [x][y] de la grille de départ.
     */
    public int getCaseDepart(int x, int y) {
        return this.grilleDepart[x][y];
    }

    /**
     * Définit la grille aléatoire de départ.
     */
    public void randomGrille() {
        Random random = new Random();
    
        for (int x = 0; x < 9; x++) {
            // Nombre de cases à remplir aléatoirement pour cette ligne
            int nbCasesRandom = random.nextInt(9); // Permet de remplir jusqu'à 9 cases aléatoirement
    
            for (int i = 0; i < nbCasesRandom; i++) {
                // Coordonnées aléatoires pour la case
                int yCaseRandom = random.nextInt(9);
                int valeurRandom = random.nextInt(9) + 1; // Les valeurs vont de 1 à 9 inclusivement
    
                // Vérification si la case est disponible pour cette valeur
                if (caseDisponible(x, yCaseRandom, valeurRandom)) {
                    remplirCase(x, yCaseRandom, valeurRandom);
                    this.grilleDepart[x][yCaseRandom] = 1;
                }
            }
        }
    }

    /**
     * J'en avais marre de afficheStringGrille() alors ça c'est plus compréhensible.<br>
     * 
     * Cela affiche la grille dans le terminal de manière plus humainement compréhensible.
     */
    public void printSudoku(int[][] grille) {
        for (int i = 0; i < grille.length; i++) {
            if (i % 3 == 0) {
                System.out.println("+---+---+---+---+---+---+");
            }
            for (int j = 0; j < grille[i].length; j++) {
                if (j % 3 == 0) {
                    System.out.print("| ");
                }
                if (grille[i][j] == 0) {
                    System.out.print("  "); // Case vide
                } else {
                    System.out.print(grille[i][j] + " ");
                }
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+---+---+---+");
    }


    public void loadGrille(String fileName) {
        this.grille = new int[9][9];
        this.grilleDepart = new int[9][9];
        this.nameFile = fileName;
    }

    public void setValue(int x, int y, int value) {
        this.grille[x][y] = value;
    }

    public void setValueDepart(int x, int y, int value) {
        this.grilleDepart[x][y] = value;
    }

    /**
     * Résoud la partie.
     * 
     * @return boolean
     */
    public boolean resoudreSudoku() {
        if (Fin()) {
            return true;
        }

        int[] cellule = celluleVide();
        for (int i = 1; i <= 9; i++) {
            if (caseDisponible(cellule[0], cellule[1], i)) {
                this.grille[cellule[0]][cellule[1]] = i;

                if (resoudreSudoku()) {
                    return true;
                }

                this.grille[cellule[0]][cellule[1]] = 0;
            }
        }
        
        return false;
    }


    /**
     * Regarde si la partie est finie.
     * 
     * @return true si la partie est finie, false sinon.
     */
    public boolean Fin() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.grille[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Cherche une cellule vide dans la grille.
     * 
     * @return coordonnées [x][y] de la première cellule vide. 
     */
    public int[] celluleVide() {
        int[] cellule = new int[2];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.grille[i][j] == 0) {
                    cellule[0] = i;
                    cellule[1] = j;
                    return cellule;
                }
            }
        }

        cellule[0] = -1;
        cellule[1] = -1;

        return cellule;
    }


    /**
     * Remet la partie a 0.
     */
    public void restartGame() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.grilleDepart[i][j] == 0) {
                    this.grille[i][j] = 0;
                }
            }
        }
    }

    /**
     * @return nom du fichier chargé.
     */
    public String getFileName() {
        return this.nameFile;
    }

    /**
     * met le nameFile a la valeur donnée.
     */
    public void setFileName(String fileName) {
        this.nameFile = fileName;
    }

}