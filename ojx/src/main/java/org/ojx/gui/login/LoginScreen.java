package org.ojx.gui.login;

import javax.swing.*;

import org.ojx.repository.UserRepository;
import org.ojx.service.UserService;
import org.ojx.service.impl.UserServiceImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private UserService userService;

    public LoginScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        userService = new UserServiceImpl(new UserRepository());
    }

    private void initializeComponents() {
        // Create text fields
        usernameField = new JTextField(25);
        passwordField = new JPasswordField(25);

        // Create buttons
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Set preferred sizes - making text fields larger
        Dimension fieldSize = new Dimension(250, 35);
        usernameField.setText("admin");
        passwordField.setText("a");
        usernameField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);

        Dimension buttonSize = new Dimension(100, 35);
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title
        JLabel titleLabel = new JLabel("OJX - Online Judge System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 30, 20);
        mainPanel.add(titleLabel, gbc);

        // Username label and field
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 20);
        mainPanel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 20, 10, 10);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 20);
        mainPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleLogin();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        // Allow Enter key to trigger login
        getRootPane().setDefaultButton(loginButton);
    }

    private void setupFrame() {
        setTitle("OJX Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);
    }

    private void handleLogin() throws SQLException {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty()) {
            showErrorMessage("Please enter a username");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showErrorMessage("Please enter a password");
            passwordField.requestFocus();
            return;
        }

        int type = userService.isAuthenticated(username, password);
        if (type == -1) {
            showErrorMessage("Invalid username or password");
            passwordField.setText("");
            passwordField.requestFocus();
            return;
        }
        showSuccessMessage("Login successful as " + (type == 2 ? "an admin" : "a user") + "! Welcome " + username);
    }

    private void handleRegister() {
        // Open the SignupScreen as a modal dialog
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SignupScreen signupScreen = new SignupScreen(LoginScreen.this);
                signupScreen.setVisible(true);
            }
        });
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Getters for testing or external access
    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }
}