package personallibertymanger;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import javax.swing.*; 
import java.awt.*; 
import java.io.*; 
import java.util.List; 

public class TODOLIST_ extends JFrame {
    private JPanel panel; 
    private JLabel messageLabel; 
    private JTextField taskInput; 
    private JButton addButton; 
    private JButton removeButton; 
    private DefaultListModel<String> listModel; 
    private JList<String> todoList; 
    private final String SAVE_FILE = "todolist_data.txt"; 
    private final String SIZE_FILE = "window_size.txt"; 

    public TODOLIST_() {
        setTitle("To-Do List"); 
        loadWindowSize(); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        buildPanel(); 
        add(panel); 
        setLocationRelativeTo(null); 
        loadTasks(); 
        setVisible(true); 

        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveTasks(); 
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

        messageLabel = new JLabel("Enter a task:"); 
        taskInput = new JTextField(20); 
        addButton = new JButton("Add Task"); 
        removeButton = new JButton("Remove Task"); 

        inputPanel.add(messageLabel, BorderLayout.NORTH); 
        inputPanel.add(taskInput, BorderLayout.CENTER); 
        inputPanel.add(addButton, BorderLayout.EAST); 

        panel.add(inputPanel, BorderLayout.NORTH); 
        panel.add(listScrollPane, BorderLayout.CENTER); 
        panel.add(removeButton, BorderLayout.SOUTH); 

        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = taskInput.getText().trim(); 
                if (!task.isEmpty()) { 
                    listModel.addElement(task); 
                    taskInput.setText(""); 
                } else {
                    
                    JOptionPane.showMessageDialog(panel, "Please enter a task!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = todoList.getSelectedIndex(); 
                if (selectedIndex != -1) { 
                    listModel.remove(selectedIndex); 
                } else {
                    
                    JOptionPane.showMessageDialog(panel, "Please select a task to remove!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    
    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            for (int i = 0; i < listModel.getSize(); i++) { 
                writer.println(listModel.get(i)); 
            }
        } catch (IOException e) {
            
            JOptionPane.showMessageDialog(this, "Error saving tasks!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String task; 
            while ((task = reader.readLine()) != null) { 
                listModel.addElement(task); 
            }
        } catch (IOException e) {
            
        }
    }

    
    private void saveWindowSize() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SIZE_FILE))) {
            writer.println(getWidth()); 
            writer.println(getHeight()); 
        } catch (IOException e) {
            
            JOptionPane.showMessageDialog(this, "Error saving window size!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void loadWindowSize() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SIZE_FILE))) {
            int width = Integer.parseInt(reader.readLine()); 
            int height = Integer.parseInt(reader.readLine()); 
            setSize(width, height); 
        } catch (IOException | NumberFormatException e) {
            setSize(310, 160); 
        }
    }

    public static void main(String[] args) {
        new TODOLIST_(); 
    }
}

