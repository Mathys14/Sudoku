import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SudokuFileHandler extends JFrame {
    private JTextField inputField;
    private JButton submitButton;
    private JButton exitButton;

    public SudokuFileHandler(Configuration config) {
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        // STOOLS CENTERFRAME COMMENTED OUT - NEEDS TO BE DEFINED OR REMOVED
        setLayout(new FlowLayout());

        JButton openFileButton = new JButton("Charger une Partie");
        openFileButton.addActionListener(new OpenFileButtonListener(config));

        JButton otherButton = new JButton("Nouvelle Partie");
        otherButton.addActionListener(new OtherButtonListener(config));

        add(openFileButton);
        add(otherButton);
    }

    private class OpenFileButtonListener implements ActionListener {
        private Configuration config;

        public OpenFileButtonListener(Configuration config) {
            this.config = config;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();

            // Specify the default directory
            fileChooser.setCurrentDirectory(new File("rep/"));

            // Configure file selection mode to accept only files
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = fileChooser.showOpenDialog(SudokuFileHandler.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                loadSudoku(selectedFile.getName(), config);
                // Close the window created from Sudoku.java's main
                SudokuFileHandler.this.dispose();
            }
        }
    }

    private class OtherButtonListener implements ActionListener {
        private Configuration config;

        public OtherButtonListener(Configuration config) {
            this.config = config;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SudokuInterface sudokuInterface = new SudokuInterface(config);
            sudokuInterface.setVisible(true);
            SudokuFileHandler.this.dispose();
        }
    }

    public void SudokuFileSave(Configuration config) {
        if (config.getFileName() != null) {
            SaveFile(config, config.getFileName());
        } else {
            setTitle("Sauvegarder");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(300, 175);
            // STOOLS CENTERFRAME COMMENTED OUT - NEEDS TO BE DEFINED OR REMOVED
            SaveFileInterface(config);
        }
    }

    public void SaveFileInterface(Configuration config) {
        inputField = new JTextField(20);
        submitButton = new JButton("Sauvegarder");
        exitButton = new JButton("Annuler");
    
        submitButton.addActionListener(new SaveButtonListener(config));

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SudokuFileHandler.this.dispose();
            }
        });
    
        Container container = getContentPane();
        container.setLayout(new FlowLayout());
        SudokuFileHandler.this.getContentPane().removeAll();
        SudokuFileHandler.this.revalidate();
        container.add(inputField, BorderLayout.CENTER);
        container.add(submitButton, BorderLayout.SOUTH);
        container.add(exitButton, BorderLayout.SOUTH);
    }

    private class SaveButtonListener implements ActionListener {
        private Configuration config;

        public SaveButtonListener(Configuration config) {
            this.config = config;
        }

        public void actionPerformed(ActionEvent e) {
            // Get user input text
            String userInput = inputField.getText();
            // Process the input text (you can modify this according to your needs)
            SaveFile(config, userInput);
            SudokuFileHandler.this.dispose();
        }
    }

    public void SaveFile(Configuration config, String fileName) {
        String directoryPath = System.getProperty("user.dir");
        int insertPosition = fileName.length() - 4;
        String mainFilePath = null;
        String dependentFilePath = null;
        if (config.getFileName() != null) {
            mainFilePath = directoryPath + "/rep/" + fileName;
            dependentFilePath = directoryPath + "/rep/dep/" + fileName.substring(0, insertPosition) + "dep" + fileName.substring(insertPosition);
        } else {
            mainFilePath = directoryPath + "/rep/" + fileName + ".gri";
            dependentFilePath = directoryPath + "/rep/dep/" + fileName + "dep.gri";
        }

        try {

            // Create the "rep" directory if it doesn't exist
            File repDirectory = new File(directoryPath + "/rep/");
            if (!repDirectory.exists()) {
                repDirectory.mkdirs(); // Create the directory and all necessary parent directories if needed
            }

            // Create the main file if it doesn't exist
            File mainFile = new File(mainFilePath);
            if (!mainFile.exists()) {
                mainFile.createNewFile();
            }
    
            // Create the "rep/dep" subdirectory if it doesn't exist
            File depDirectory = new File(directoryPath + "/rep/dep/");
            if (!depDirectory.exists()) {
                depDirectory.mkdirs(); // Create the directory and all necessary parent directories if needed
            }
    
            // Create the dependent file if it doesn't exist
            File dependentFile = new File(dependentFilePath);
            if (!dependentFile.exists()) {
                dependentFile.createNewFile();
            }
            
            // Write to the files

            FileOutputStream fos = new FileOutputStream(mainFile);
            DataOutputStream dataOut = new DataOutputStream(fos);

            FileOutputStream fosDep = new FileOutputStream(dependentFile);
            DataOutputStream dataOutDep = new DataOutputStream(fosDep);
    
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    dataOut.writeBytes(Integer.toString(config.getCase(i, j)));
                    dataOutDep.writeBytes(Integer.toString(config.getCaseDepart(i, j)));
                }
            }
    
            // Close the BufferedWriter

            dataOut.close();
            dataOutDep.close();



            // Normally this works, so we leave it commented.
            // System.out.println("Data written successfully to the files.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error writing to files", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load the opened game.
     */
    public void loadSudoku(String fileName, Configuration config) {
        config.loadGrille(fileName);
        int insertPosition = fileName.length() - 4;
        String fileNameDep = "rep/dep/" + fileName.substring(0, insertPosition) + "dep" + fileName.substring(insertPosition);
        fileName = "rep/" + fileName;
        loadGrilleDepart(fileNameDep, config);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < 9) {
                int lineLength = line.length();
                // Check if line length exceeds 9 characters
                if (lineLength > 9) {
                    JOptionPane.showMessageDialog(null, "Error: Line " + (row + 1) + " in file " + fileName + " is too long.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(ABORT);
                }

                // Pad line with zeros if shorter than 9 characters
                if (lineLength < 9) {
                    StringBuilder paddedLine = new StringBuilder();
                    for (int i = 0; i < 9 - lineLength; i++) {
                        paddedLine.append("0");
                    }
                    paddedLine.append(line);
                    line = paddedLine.toString();
                }

                for (int col = 0; col < 9; col++) {
                    char ch = line.charAt(col);
                    if (Character.isDigit(ch)) {
                        config.setValue(row, col, Character.getNumericValue(ch));
                    } else {
                        // Treat non-digit characters as empty cells
                        config.setValue(row, col, 0);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(ABORT);
        }
        SudokuInterface sudokuInterface = new SudokuInterface(config);
        
        sudokuInterface.setVisible(true);
    }

    /**
     * @param fileName 
     */
    private void loadGrilleDepart(String fileName, Configuration config) {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < 9) {
                int lineLength = line.length();
   
                if (lineLength > 9) {
                    JOptionPane.showMessageDialog(null, "Error: Line " + (row + 1) + " in file " + fileName + " is too long.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(ABORT);
                }

            
                if (lineLength < 9) {
                    StringBuilder paddedLine = new StringBuilder();
                    for (int i = 0; i < 9 - lineLength; i++) {
                        paddedLine.append("0");
                    }
                    paddedLine.append(line);
                    line = paddedLine.toString();
                }

                for (int col = 0; col < 9; col++) {
                    char ch = line.charAt(col);
                    if (Character.isDigit(ch)) {
                        config.setValueDepart(row, col, Character.getNumericValue(ch));
                    } else {
                        
                        config.setValueDepart(row, col, 0);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(ABORT);
        }
    }
}
