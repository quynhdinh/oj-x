package org.ojx.gui.admin;

import javax.swing.*;
import org.ojx.model.User;
import org.ojx.service.UserService;
import org.ojx.service.impl.UserServiceImpl;
import org.ojx.repository.UserRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewUserScreen extends JFrame {
    private User user;
    private UserService userService;
    
    // UI Components
    private JTextField userIdField;
    private JTextField userTypeField;
    private JTextField userNameField;
    private JTextField emailField;
    private JTextField nameField;
    private JTextField countryField;
    private JTextField ratingField;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton resetPasswordButton;
    private JButton quitButton;
    
    public ViewUserScreen(User user) {
        this.user = user;
        this.userService = new UserServiceImpl(new UserRepository());
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        setupFrame();
    }
    
    // Static factory method for easy creation
    public static void showUserDetail(User user) {
        ViewUserScreen screen = new ViewUserScreen(user);
        screen.setVisible(true);
    }
    
    private void initializeComponents() {
        // Create text fields - only user_id is read-only
        userIdField = new JTextField(20);
        userIdField.setEditable(false);
        userIdField.setBackground(new Color(245, 245, 245)); // Light gray for read-only
        
        userTypeField = new JTextField(20);
        userTypeField.setEditable(true);
        userTypeField.setBackground(Color.WHITE);
        
        userNameField = new JTextField(20);
        userNameField.setEditable(true);
        userNameField.setBackground(Color.WHITE);
        
        emailField = new JTextField(20);
        emailField.setEditable(true);
        emailField.setBackground(Color.WHITE);
        
        nameField = new JTextField(20);
        nameField.setEditable(true);
        nameField.setBackground(Color.WHITE);
        
        countryField = new JTextField(20);
        countryField.setEditable(true);
        countryField.setBackground(Color.WHITE);
        
        ratingField = new JTextField(20);
        ratingField.setEditable(true);
        ratingField.setBackground(Color.WHITE);
        
        // Create buttons
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        resetPasswordButton = new JButton("Reset Password");
        quitButton = new JButton("Quit");
        
        updateButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setPreferredSize(new Dimension(100, 30));
        resetPasswordButton.setPreferredSize(new Dimension(130, 30));
        quitButton.setPreferredSize(new Dimension(100, 30));
        
        // Set button colors
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);
        
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        
        resetPasswordButton.setBackground(new Color(255, 193, 7));
        resetPasswordButton.setForeground(Color.BLACK);
        resetPasswordButton.setFocusPainted(false);
        resetPasswordButton.setOpaque(true);
        resetPasswordButton.setBorderPainted(false);
        
        quitButton.setBackground(new Color(108, 117, 125));
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(false);
        
        // Add tooltips
        updateButton.setToolTipText("Save changes to user information");
        deleteButton.setToolTipText("Delete this user account");
        resetPasswordButton.setToolTipText("Reset user's password to a default value");
        quitButton.setToolTipText("Close this window");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel with form fields
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("User Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // Reset gridwidth for form fields
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // User ID
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(userIdLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(userIdField, gbc);
        
        // User Type
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel userTypeLabel = new JLabel("User Type:");
        userTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(userTypeLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(userTypeField, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel userNameLabel = new JLabel("Username:");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(userNameLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(userNameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(emailField, gbc);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(nameField, gbc);
        
        // Country
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel countryLabel = new JLabel("Country:");
        countryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(countryLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(countryField, gbc);
        
        // Rating
        gbc.gridx = 0; gbc.gridy = 7; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(ratingLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(ratingField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(updateButton);
        buttonPanel.add(resetPasswordButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(quitButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdate();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelete();
            }
        });
        
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleResetPassword();
            }
        });
        
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });
        
        // Close window when ESC is pressed
        getRootPane().registerKeyboardAction(
            _ -> handleQuit(),
            KeyStroke.getKeyStroke("ESCAPE"),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void populateFields() {
        if (user != null) {
            userIdField.setText(String.valueOf(user.getUserId()));
            userTypeField.setText(user.getUserType());
            userNameField.setText(user.getUserName());
            emailField.setText(user.getEmail());
            nameField.setText(user.getName());
            countryField.setText(user.getCountry());
            ratingField.setText(String.valueOf(user.getRating()));
        }
    }
    
    private void setupFrame() {
        setTitle("OJX - View User Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        
        // Set minimum size
        Dimension preferredSize = getPreferredSize();
        setMinimumSize(new Dimension(
            Math.max(preferredSize.width, 400),
            Math.max(preferredSize.height, 300)
        ));
        
        // Center the window
        setLocationRelativeTo(null);
        
        // Set focus to quit button
        SwingUtilities.invokeLater(() -> quitButton.requestFocus());
    }
    
    private void handleUpdate() {
        try {
            // Validate input fields
            String userType = userTypeField.getText().trim();
            String userName = userNameField.getText().trim();
            String email = emailField.getText().trim();
            String name = nameField.getText().trim();
            String country = countryField.getText().trim();
            String ratingText = ratingField.getText().trim();
            
            // Basic validation
            if (userType.isEmpty() || userName.isEmpty() || email.isEmpty() || name.isEmpty()) {
                showErrorMessage("Please fill in all required fields.");
                return;
            }
            
            // Validate email format
            if (!email.contains("@") || !email.contains(".")) {
                showErrorMessage("Please enter a valid email address.");
                return;
            }
            
            // Validate rating
            int rating;
            try {
                rating = Integer.parseInt(ratingText);
                if (rating < 0) {
                    showErrorMessage("Rating must be a non-negative number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Please enter a valid rating number.");
                return;
            }
            
            // Update user object
            User.Builder builder = new User.Builder()
                .userId(user.getUserId())
                .userType(userType)
                .userName(userName)
                .email(email)
                .name(name)
                .country(country)
                .rating(rating)
                .password(user.getPassword()); // Keep existing password
            
            User updatedUser = builder.build();
            
            int success = userService.update(updatedUser);
            
            if (success > 0) {
                this.user = updatedUser;
                showSuccessMessage("User information updated successfully!");
            } else {
                showErrorMessage("Failed to update user. Please try again.");
            }
            
        } catch (Exception e) {
            showErrorMessage("Error updating user: " + e.getMessage());
        }
    }
    
    private void handleResetPassword() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to reset the password for user '" + user.getUserName() + "'?\n\n" +
            "This will change their password to: 'password123'\n" +
            "The user will need to change their password after next login.\n\n" +
            "Do you want to continue?",
            "Confirm Password Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                // Reset password to default
                String newPassword = "password123";
                boolean success = userService.resetPassword(user.getUserId(), newPassword);
                
                if (success) {
                    showSuccessMessage("Password has been reset successfully!\n" +
                        "New password: " + newPassword + "\n\n" +
                        "Please inform the user to change their password after next login.");
                } else {
                    showErrorMessage("Failed to reset password. Please try again.");
                }
                
            } catch (Exception e) {
                showErrorMessage("Error resetting password: " + e.getMessage());
            }
        }
    }
    
    private void handleDelete() {
        // Prevent deletion of admin accounts
        if ("admin".equalsIgnoreCase(user.getUserType())) {
            showErrorMessage("Cannot delete admin accounts for security reasons.");
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete user '" + user.getUserName() + "'?\n\n" +
            "This action cannot be undone and will:\n" +
            "• Remove the user account permanently\n" +
            "• Delete all associated submissions\n" +
            "• Remove user from leaderboards\n\n" +
            "Do you want to continue?",
            "Confirm User Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                // Attempt to delete the user
                boolean deleted = userService.deleteUser(user.getUserId());
                
                if (deleted) {
                    showSuccessMessage("User '" + user.getUserName() + "' has been deleted successfully.");
                    dispose(); // Close the window after successful deletion
                } else {
                    showErrorMessage("Failed to delete user. Please try again.");
                }
            } catch (Exception e) {
                showErrorMessage("Error deleting user: " + e.getMessage());
            }
        }
    }
    
    private void handleQuit() {
        dispose(); // Close the window
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showInfoMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Getters for external access
    public User getUser() {
        return user;
    }
    
    // Main method for testing the ViewUserScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                // Create a sample user for testing
                User sampleUser = new User.Builder()
                    .userId(1)
                    .userType("user")
                    .userName("john_doe")
                    .email("john@example.com")
                    .name("John Doe")
                    .country("USA")
                    .rating(1200)
                    .password("pass123")
                    .build();
                
                new ViewUserScreen(sampleUser).setVisible(true);
            }
        });
    }
}
