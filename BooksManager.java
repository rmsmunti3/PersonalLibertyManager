package personallibertymanger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BooksManager {

    private static final String BOOKS_FILE = "books_data.dat"; 
    private List<Category> categoriesList = new ArrayList<>(); 

    public void showBooksManager() {
        
        List<Category> categoriesList = CategoryManagerWindow.categoriesList;
        JFrame booksFrame = new JFrame("Books Manager");
        booksFrame.setSize(400, 300);
        booksFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        booksFrame.setLayout(new BorderLayout());

        
        booksFrame.getContentPane().setBackground(new Color(137, 207, 240));

        
        JPanel addBookPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        addBookPanel.setBackground(new Color(137, 207, 240));
        JLabel nameLabel = new JLabel("Book Name:");
        JTextField nameField = new JTextField(15);
        JLabel authorLabel = new JLabel("Author's Name:");
        JTextField authorField = new JTextField(15);
        addBookPanel.add(nameLabel);
        addBookPanel.add(nameField);
        addBookPanel.add(authorLabel);
        addBookPanel.add(authorField);

        
        DefaultListModel<String> booksListModel = new DefaultListModel<>();
        JList<String> booksList = new JList<>(booksListModel);
        JScrollPane scrollPane = new JScrollPane(booksList);

        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(137, 207, 240));
        JButton addBookButton = new JButton("Add Book");
        JButton addBookToCategoryButton = new JButton("Add to Category");
        JButton deleteBookButton = new JButton("Delete Book");
        buttonsPanel.add(addBookButton);
        buttonsPanel.add(addBookToCategoryButton);
        buttonsPanel.add(deleteBookButton);

        booksFrame.add(addBookPanel, BorderLayout.NORTH);
        booksFrame.add(scrollPane, BorderLayout.CENTER);
        booksFrame.add(buttonsPanel, BorderLayout.SOUTH);

        
        loadBooks(booksListModel);

        
        addBookButton.addActionListener(e -> {
            String bookName = nameField.getText();
            String authorName = authorField.getText();

            if (!bookName.isEmpty() && !authorName.isEmpty()) {
                String book = bookName + " by " + authorName;
                booksListModel.addElement(book);
                nameField.setText("");
                authorField.setText("");

                
                saveBooks(booksListModel);
            } else {
                JOptionPane.showMessageDialog(booksFrame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        deleteBookButton.addActionListener(e -> {
            String selectedBook = booksList.getSelectedValue();
            if (selectedBook != null) {
                booksListModel.removeElement(selectedBook);

                
                saveBooks(booksListModel);
            } else {
                JOptionPane.showMessageDialog(booksFrame, "Select a book to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        
        addBookToCategoryButton.addActionListener(e -> {
            String selectedBook = booksList.getSelectedValue();
            if (selectedBook == null) {
                JOptionPane.showMessageDialog(booksFrame, "Select a book to add to a category.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            List<String> categoryNames = new ArrayList<>();
            for (Category category : categoriesList) {
                categoryNames.add(category.getName());
            }

            if (categoryNames.isEmpty()) {
                JOptionPane.showMessageDialog(booksFrame, "No categories available. Add categories first.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String selectedCategory = (String) JOptionPane.showInputDialog(
                booksFrame,
                "Select a category:",
                "Add to Category",
                JOptionPane.QUESTION_MESSAGE,
                null,
                categoryNames.toArray(),
                categoryNames.get(0)
            );

            if (selectedCategory != null) {
                
                for (Category category : categoriesList) {
                    if (category.getName().equals(selectedCategory)) {
                        category.addBook(selectedBook); 
                        CategoryManagerWindow.saveCategories(); 
                        JOptionPane.showMessageDialog(booksFrame, "Book added to category: " + selectedCategory);
                        break;
                    }
                }
            }
        });

        booksFrame.setVisible(true);
    }

    
    private void saveBooks(DefaultListModel<String> booksListModel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            List<String> books = new ArrayList<>();
            for (int i = 0; i < booksListModel.size(); i++) {
                books.add(booksListModel.getElementAt(i));
            }
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void loadBooks(DefaultListModel<String> booksListModel) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_FILE))) {
            List<String> books = (List<String>) ois.readObject();
            for (String book : books) {
                booksListModel.addElement(book);
            }
        } catch (FileNotFoundException e) {
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}