package personallibertymanger;
import java.awt.event.*; 
import javax.swing.*; 
import java.awt.*; 
import java.io.*; 
import java.util.*; 

public class GOAL extends JFrame {
    private JPanel panel; 
    private JLabel messageLabel; 
    private JTextField taskInput; 
    private JButton addButton; 
    private JButton removeButton; 
    private DefaultListModel<String> listModel; 
    private JList<String> todoList; 
    private static final String GOALS_FILE = "goals.txt"; 
    private static final String CONFIG_FILE = "config.txt"; 

    public GOAL() {
        setTitle("Goals of the year"); 
        loadWindowSize(); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        buildPanel(); 
        add(panel); 
        loadGoals(); 
        setLocationRelativeTo(null); 
        setVisible(true); 

        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveGoals(); 
                saveWindowSize(); 
            }
        });
    }

    private void buildPanel() {
        panel = new JPanel(); 
        panel.setLayout(new BorderLayout()); 
        panel.setBackground(new Color(173, 216, 230)); 

        listModel = new DefaultListModel<>(); 
        todoList = new JList<>(listModel); 
        JScrollPane listScrollPane = new JScrollPane(todoList); 

        JPanel inputPanel = new JPanel(); 
        inputPanel.setLayout(new BorderLayout()); 
        inputPanel.setBackground(new Color(173, 216, 230)); 

        messageLabel = new JLabel("Enter your goal:"); 
        taskInput = new JTextField(20); 
        addButton = new JButton("Add goal"); 
        removeButton = new JButton("Remove goal"); 

        inputPanel.add(messageLabel, BorderLayout.NORTH); 
        inputPanel.add(taskInput, BorderLayout.CENTER); 
        inputPanel.add(addButton, BorderLayout.EAST); 

        panel.add(inputPanel, BorderLayout.NORTH); 
        panel.add(listScrollPane, BorderLayout.CENTER); 
        panel.add(removeButton, BorderLayout.SOUTH); 

        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String task = taskInput.getText().trim(); 
                    if (!task.isEmpty()) {
                        listModel.addElement(task); 
                        taskInput.setText(""); 
                    } else {
                        throw new IllegalArgumentException("Goal cannot be empty"); 
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedIndex = todoList.getSelectedIndex(); 
                    if (selectedIndex != -1) {
                        listModel.remove(selectedIndex); 
                    } else {
                        throw new IllegalStateException("No goal selected for removal"); 
                    }
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadGoals() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                listModel.addElement(line); 
            }
        } catch (FileNotFoundException e) {
            saveGoals();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading goals file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveGoals() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(GOALS_FILE))) {
            for (int i = 0; i < listModel.getSize(); i++) {
                writer.println(listModel.getElementAt(i)); 
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving goals.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadWindowSize() {
        try (Scanner scanner = new Scanner(new File(CONFIG_FILE))) {
            if (scanner.hasNextInt()) {
                int width = scanner.nextInt(); 
                int height = scanner.nextInt(); 
                setSize(width, height); 
            } else {
                setSize(310, 160); 
            }
        } catch (FileNotFoundException e) {
            setSize(310, 160); 
            saveWindowSize();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading window size.", "Error", JOptionPane.ERROR_MESSAGE);
            setSize(310, 160); 
        }
    }

    private void saveWindowSize() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            writer.println(getWidth() + " " + getHeight()); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving window size.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new GOAL(); 
    }
}
