package org.ojx.gui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.ojx.model.User;
import org.ojx.service.UserService;
import org.ojx.service.impl.UserServiceImpl;
import org.ojx.repository.UserRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserManagementScreen extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton viewUserButton;
    private JButton quitButton;
    private JLabel totalUsersLabel;
    
    private UserService userService;
    
    public UserManagementScreen() {
        userService = new UserServiceImpl(new UserRepository());
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadUsers();
    }
    
    private void initializeComponents() {
        // Create table model with columns
        String[] columnNames = {
            "User ID", "Username", "User Type", "Email", "Name", "Country", "Rating"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(25);
        userTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        userTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // User ID
        userTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Username
        userTable.getColumnModel().getColumn(2).setPreferredWidth(100); // User Type
        userTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Email
        userTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Name
        userTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Country
        userTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Rating
        
        // Create buttons
        viewUserButton = new JButton("View User");
        quitButton = new JButton("Quit");
        
        viewUserButton.setPreferredSize(new Dimension(120, 35));
        quitButton.setPreferredSize(new Dimension(100, 35));
        
        // Create total users label
        totalUsersLabel = new JLabel("Total: 0 users");
        totalUsersLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add tooltips
        userTable.setToolTipText("Click on a user to select, then use 'View User' button or double-click to view details");
        viewUserButton.setToolTipText("View details of the selected user");
        quitButton.setToolTipText("Close this screen");
        
        // Initially disable view button until a user is selected
        viewUserButton.setEnabled(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Info panel (total users)
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(totalUsersLabel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));

        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 400));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("System Users"));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(viewUserButton);
        buttonPanel.add(quitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add to frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // View user button
        viewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewUser();
            }
        });
        
        // Quit button
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });
        
        // Table selection listener
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewUserButton.setEnabled(userTable.getSelectedRow() != -1);
            }
        });
        
        // Double-click on table row to view user details
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && userTable.getSelectedRow() != -1) {
                    handleViewUser();
                }
            }
        });
        
        // Allow Enter key to view selected user
        userTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "viewUser");
        userTable.getActionMap().put("viewUser", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userTable.getSelectedRow() != -1) {
                    handleViewUser();
                }
            }
        });
    }
    
    private void setupFrame() {
        setTitle("OJX - User Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void loadUsers() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load users from service
            List<User> users = userService.getAll();
            
            // Populate table
            for (User user : users) {
                Object[] rowData = {
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserType(),
                    user.getEmail(),
                    user.getName(),
                    user.getCountry(),
                    user.getRating()
                };
                tableModel.addRow(rowData);
            }
            
            // Update total count
            totalUsersLabel.setText("Total: " + users.size() + " users");
            
        } catch (Exception e) {
            showErrorMessage("Error loading users: " + e.getMessage());
        }
    }
    
    private void handleViewUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Please select a user to view.");
            return;
        }
        
        // Get user ID from the table
        Object userIdObj = tableModel.getValueAt(selectedRow, 0);
        
        if (userIdObj instanceof Integer) {
            int userId = (Integer) userIdObj;
            
            // Show user details
            try {
                User user = userService.getById(userId).orElse(null);
                if (user != null) {
                    showUserDetails(user);
                } else {
                    showErrorMessage("User not found with ID: " + userId);
                }
            } catch (Exception e) {
                showErrorMessage("Error loading user details: " + e.getMessage());
            }
            
        } else {
            showErrorMessage("Invalid user selection.");
        }
    }
    
    private void showUserDetails(User user) {
        StringBuilder details = new StringBuilder();
        details.append("User Details:\n\n");
        details.append("User ID: ").append(user.getUserId()).append("\n");
        details.append("Username: ").append(user.getUserName()).append("\n");
        details.append("User Type: ").append(user.getUserType()).append("\n");
        details.append("Email: ").append(user.getEmail()).append("\n");
        details.append("Name: ").append(user.getName()).append("\n");
        details.append("Country: ").append(user.getCountry()).append("\n");
        details.append("Rating: ").append(user.getRating()).append("\n");
        details.append("Password: ").append(user.getPassword()).append("\n");
        
        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "User Details - " + user.getUserName(),
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void handleQuit() {
        dispose(); // Close this window
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Getters for external access
    public JTable getUserTable() {
        return userTable;
    }
    
    public void refreshUserList() {
        loadUsers();
    }
    
    // Main method for testing the UserManagementScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                new UserManagementScreen().setVisible(true);
            }
        });
    }
}
