package org.ojx.gui.profile;

import javax.swing.*;

import org.ojx.model.User;
import org.ojx.repository.UserRepository;
import org.ojx.service.UserService;
import org.ojx.service.impl.UserServiceImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class UserProfileScreen extends JFrame {
    private JTextField userIdField;
    private JTextField userTypeField;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField nameField;
    private JTextField countryField;
    private JTextField ratingField;
    private JButton saveButton;
    private JButton quitButton;
    private int userId;
    private UserService userService;

    public UserProfileScreen(int userId) {
        this.userId = userId;
        userService = new UserServiceImpl(new UserRepository());
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadUserData();
    }

    private void initializeComponents() {
        // Create text fields with much larger column count
        userIdField = new JTextField(20);
        userTypeField = new JTextField(20);
        userNameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        nameField = new JTextField(20);
        countryField = new JTextField(20);
        ratingField = new JTextField(20);

        // Make userIdField and ratingField non-editable
        userIdField.setEditable(false);
        userIdField.setBackground(Color.LIGHT_GRAY);
        ratingField.setEditable(false);
        ratingField.setBackground(Color.LIGHT_GRAY);

        // Create buttons
        saveButton = new JButton("Save");
        quitButton = new JButton("Quit");

        // Set preferred sizes for text fields - making them smaller and more compact
        Dimension fieldSize = new Dimension(280, 35);
        userIdField.setPreferredSize(fieldSize);
        userTypeField.setPreferredSize(fieldSize);
        userNameField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);
        emailField.setPreferredSize(fieldSize);
        nameField.setPreferredSize(fieldSize);
        countryField.setPreferredSize(fieldSize);
        ratingField.setPreferredSize(fieldSize);

        // Set minimum and maximum sizes to ensure they don't shrink
        userIdField.setMinimumSize(fieldSize);
        userTypeField.setMinimumSize(fieldSize);
        userNameField.setMinimumSize(fieldSize);
        passwordField.setMinimumSize(fieldSize);
        emailField.setMinimumSize(fieldSize);
        nameField.setMinimumSize(fieldSize);
        countryField.setMinimumSize(fieldSize);
        ratingField.setMinimumSize(fieldSize);

        // Set font size to make text fields more prominent
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);
        userIdField.setFont(fieldFont);
        userTypeField.setFont(fieldFont);
        userNameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        emailField.setFont(fieldFont);
        nameField.setFont(fieldFont);
        countryField.setFont(fieldFont);
        ratingField.setFont(fieldFont);

        // Set preferred sizes for buttons
        Dimension buttonSize = new Dimension(100, 35);
        saveButton.setPreferredSize(buttonSize);
        quitButton.setPreferredSize(buttonSize);

        // Add tooltips
        userIdField.setToolTipText("User ID (cannot be edited)");
        userTypeField.setToolTipText("User type (admin/problem_setter/user)");
        userNameField.setToolTipText("Your username");
        passwordField.setToolTipText("Your password");
        emailField.setToolTipText("Your email address");
        nameField.setToolTipText("Your full name");
        countryField.setToolTipText("Your country");
        ratingField.setToolTipText("Your current rating");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title
        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 30, 20);
        mainPanel.add(titleLabel, gbc);

        // Reset grid width for form fields
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);

        // User ID field
        addFormField(mainPanel, gbc, "User ID:", userIdField, 1);

        // User Type field
        addFormField(mainPanel, gbc, "User Type:", userTypeField, 2);

        // Username field
        addFormField(mainPanel, gbc, "Username:", userNameField, 3);

        // Password field
        addFormField(mainPanel, gbc, "Password:", passwordField, 4);

        // Email field
        addFormField(mainPanel, gbc, "Email:", emailField, 5);

        // Name field
        addFormField(mainPanel, gbc, "Full Name:", nameField, 6);

        // Country field
        addFormField(mainPanel, gbc, "Country:", countryField, 7);

        // Rating field
        addFormField(mainPanel, gbc, "Rating:", ratingField, 8);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, gbc);

        // Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panel.add(field, gbc);
    }

    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });

        // Allow Enter key to trigger save
        getRootPane().setDefaultButton(saveButton);
    }

    private void setupFrame() {
        setTitle("User Profile - OJX");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);
    }

    private void makeUserTypeFieldUnEditable() {
        // If userType is not admin, gray the userTypeField
        userTypeField.setEditable(false);
        userTypeField.setBackground(Color.LIGHT_GRAY);
    }

    private void loadUserData() {
        Optional<User> userOpt = userService.getById(userId);
        if (!userOpt.isPresent()) {
            showErrorMessage("User not found");
            return;
        }
        User user = userOpt.get();
        userIdField.setText(String.valueOf(user.getUserId()));
        userTypeField.setText(user.getUserType());
        if (!user.getUserType().equals("admin")) {
            makeUserTypeFieldUnEditable();
        }
        userNameField.setText(user.getUserName());
        passwordField.setText(user.getPassword());
        emailField.setText(user.getEmail());
        nameField.setText(user.getName());
        countryField.setText(user.getCountry());
        ratingField.setText(String.valueOf(user.getRating()));
    }

    private void handleSave() {
        // Validate all fields
        if (!validateFields()) {
            return;
        }

        // Get field values
        String userType = userTypeField.getText().trim();
        String userName = userNameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String name = nameField.getText().trim();
        String country = countryField.getText().trim();
        User new_User = User.builder()
                .userId(userId)
                .userType(userType)
                .userName(userName)
                .password(password)
                .email(email)
                .name(name)
                .country(country)
                .build();
        userService.update(new_User);
        showSuccessMessage("Profile updated successfully!");
    }

    private boolean validateFields() {
        String userType = userTypeField.getText().trim();
        String userName = userNameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String name = nameField.getText().trim();
        String country = countryField.getText().trim();
        String rating = ratingField.getText().trim();

        // User type validation
        if (userType.isEmpty()) {
            showErrorMessage("User type is required");
            userTypeField.requestFocus();
            return false;
        }
        // User type validation
        if (!userType.equals("admin") && !userType.equals("problem_setter") && !userType.equals("user")) {
            showErrorMessage("User type is not correct. It should be one of: admin, problem_setter, user");
            userTypeField.requestFocus();
            return false;
        }

        // Username validation
        if (userName.isEmpty()) {
            showErrorMessage("Username is required");
            userNameField.requestFocus();
            return false;
        }
        if (userName.length() > 50) {
            showErrorMessage("Username must be 50 characters or less");
            userNameField.requestFocus();
            return false;
        }

        // Password validation
        if (password.isEmpty()) {
            showErrorMessage("Password is required");
            passwordField.requestFocus();
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

        // Rating validation
        if (rating.isEmpty()) {
            showErrorMessage("Rating is required");
            ratingField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Simple email validation
        return email.contains("@") && email.contains(".") && email.indexOf("@") < email.lastIndexOf(".");
    }

    private void handleQuit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit without saving?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

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

    // Main method for testing the UserProfileScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new UserProfileScreen(1).setVisible(true);
            }
        });
    }
}
