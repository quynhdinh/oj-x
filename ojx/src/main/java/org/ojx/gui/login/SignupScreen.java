package org.ojx.gui.login;

import javax.swing.*;

import org.ojx.model.User;
import org.ojx.repository.UserRepository;
import org.ojx.service.UserService;
import org.ojx.service.impl.UserServiceImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupScreen extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField nameField;
    private JTextField countryField;
    private JButton signUpButton;
    private JButton cancelButton;

    private UserService userService;
    
    public SignupScreen(Frame parent) {
        super(parent, "OJX Registration", true); // true makes it modal
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        userService = new UserServiceImpl(new UserRepository());
    }
    
    // Default constructor for standalone usage
    public SignupScreen() {
        super((Frame) null, "OJX Registration", false);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        // Create text fields with larger column count
        usernameField = new JTextField(25);
        passwordField = new JPasswordField(25);
        confirmPasswordField = new JPasswordField(25);
        emailField = new JTextField(25);
        nameField = new JTextField(25);
        countryField = new JTextField(25);
        // Set initial values for testing
        usernameField.setText("john");
        passwordField.setText("password123");
        confirmPasswordField.setText("password123");
        emailField.setText("john@johnnydang.com");
        nameField.setText("Johnny Dang");
        countryField.setText("USA");
        // Set initial values for testing
        usernameField.setToolTipText("Enter a unique username (max 50 characters)");
        passwordField.setToolTipText("Enter your password");
        confirmPasswordField.setToolTipText("Confirm your password");
        emailField.setToolTipText("Enter your email address");
        nameField.setToolTipText("Enter your full name");
        countryField.setToolTipText("Enter your country");
        // Create buttons
        signUpButton = new JButton("Sign Up");
        cancelButton = new JButton("Cancel");
        
        // Set preferred sizes for text fields - making them much larger
        Dimension fieldSize = new Dimension(250, 35);
        usernameField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);
        confirmPasswordField.setPreferredSize(fieldSize);
        emailField.setPreferredSize(fieldSize);
        nameField.setPreferredSize(fieldSize);
        countryField.setPreferredSize(fieldSize);
        
        // Set preferred sizes for buttons
        Dimension buttonSize = new Dimension(100, 35);
        signUpButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        
        // Add tooltips
        usernameField.setToolTipText("Enter a unique username (max 50 characters)");
        passwordField.setToolTipText("Enter your password");
        confirmPasswordField.setToolTipText("Confirm your password");
        emailField.setToolTipText("Enter your email address");
        nameField.setToolTipText("Enter your full name");
        countryField.setToolTipText("Enter your country");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        mainPanel.add(titleLabel, gbc);
        
        // Reset grid width for form fields
        gbc.gridwidth = 1;
        
        // Username field
        addFormField(mainPanel, gbc, "Username:", usernameField, 1);
        
        // Password field
        addFormField(mainPanel, gbc, "Password:", passwordField, 2);
        
        // Confirm Password field
        addFormField(mainPanel, gbc, "Confirm Password:", confirmPasswordField, 3);
        
        // Email field
        addFormField(mainPanel, gbc, "Email:", emailField, 4);
        
        // Name field
        addFormField(mainPanel, gbc, "Full Name:", nameField, 5);
        
        // Country field
        addFormField(mainPanel, gbc, "Country:", countryField, 6);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(signUpButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 5);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, gbc);
        
        // Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 10);
        panel.add(field, gbc);
    }
    
    private void setupEventHandlers() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });
        
        // Allow Enter key to trigger sign up
        getRootPane().setDefaultButton(signUpButton);
    }
    
    private void setupFrame() {
        setTitle("OJX Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);
    }
    
    private void handleSignUp() {
        // Validate all fields
        if (!validateFields()) {
            return;
        }
        
        // Get field values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String name = nameField.getText().trim();
        String country = countryField.getText().trim();

        User user = new User.Builder()
                .userName(username)
                .userType("user")
                .password(password)
                .email(email)
                .name(name)
                .country(country)
                .rating(1400)
                .build();
        System.out.println(user);
        userService.save(user);
        showSuccessMessage("Account created successfully!\nUsername: " + username + "\nEmail: " + email);
        
        clearFields();
        dispose();
    }
    
    private boolean validateFields() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText().trim();
        String name = nameField.getText().trim();
        String country = countryField.getText().trim();
        
        // Username validation
        if (username.isEmpty()) {
            showErrorMessage("Username is required");
            usernameField.requestFocus();
            return false;
        }
        if (username.length() > 50) {
            showErrorMessage("Username must be 50 characters or less");
            usernameField.requestFocus();
            return false;
        }
        
        // Password validation
        if (password.isEmpty()) {
            showErrorMessage("Password is required");
            passwordField.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            showErrorMessage("Password must be at least 6 characters long");
            passwordField.requestFocus();
            return false;
        }
        
        // Confirm password validation
        if (!password.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match");
            confirmPasswordField.requestFocus();
            return false;
        }
        
        // Email validation
        if (email.isEmpty()) {
            showErrorMessage("Email is required");
            emailField.requestFocus();
            return false;
        }
        if (!isValidEmail(email)) {
            showErrorMessage("Please enter a valid email address");
            emailField.requestFocus();
            return false;
        }
        if (email.length() > 100) {
            showErrorMessage("Email must be 100 characters or less");
            emailField.requestFocus();
            return false;
        }
        
        // Name validation
        if (name.isEmpty()) {
            showErrorMessage("Full name is required");
            nameField.requestFocus();
            return false;
        }
        if (name.length() > 100) {
            showErrorMessage("Name must be 100 characters or less");
            nameField.requestFocus();
            return false;
        }
        
        // Country validation
        if (country.isEmpty()) {
            showErrorMessage("Country is required");
            countryField.requestFocus();
            return false;
        }
        if (country.length() > 50) {
            showErrorMessage("Country must be 50 characters or less");
            countryField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        // Simple email validation
        return email.contains("@") && email.contains(".") && email.indexOf("@") < email.lastIndexOf(".");
    }
    
    private void handleCancel() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel registration?",
            "Confirm Cancel",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        emailField.setText("");
        nameField.setText("");
        countryField.setText("");
        usernameField.requestFocus();
    }
    
    // Getters for accessing field values (useful for testing or integration)
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public String getEmail() {
        return emailField.getText().trim();
    }
    
    public String getFullName() {
        return nameField.getText().trim();
    }
    
    public String getCountry() {
        return countryField.getText().trim();
    }
    
    // Main method for testing the SignupScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                new SignupScreen().setVisible(true);
            }
        });
    }
}
