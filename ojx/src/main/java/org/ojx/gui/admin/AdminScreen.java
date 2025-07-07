package org.ojx.gui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminScreen extends JFrame {
    
    // UI Components
    private JButton userManagementButton;
    private JButton contestManagementButton;
    private JButton problemManagementButton;
    private JButton backButton;
    
    public AdminScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        // Create management buttons
        userManagementButton = new JButton("User Management");
        contestManagementButton = new JButton("Contest Management");
        problemManagementButton = new JButton("Problem Management");
        backButton = new JButton("Back");
        
        // Set preferred sizes for consistent button appearance
        Dimension buttonSize = new Dimension(200, 50);
        userManagementButton.setPreferredSize(buttonSize);
        contestManagementButton.setPreferredSize(buttonSize);
        problemManagementButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(new Dimension(100, 35));
        
        // Set fonts and styling
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        userManagementButton.setFont(buttonFont);
        contestManagementButton.setFont(buttonFont);
        problemManagementButton.setFont(buttonFont);
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Set colors and styling
        Color primaryColor = new Color(52, 144, 220);
        Color hoverColor = new Color(41, 128, 185);
        
        // Style management buttons
        styleButton(userManagementButton, primaryColor, hoverColor);
        styleButton(contestManagementButton, primaryColor, hoverColor);
        styleButton(problemManagementButton, primaryColor, hoverColor);
        
        // Style back button differently
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        
        // Add hover effect for back button
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(90, 98, 104));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(108, 117, 125));
            }
        });
        
        // Add tooltips
        userManagementButton.setToolTipText("Manage users, roles, and permissions");
        contestManagementButton.setToolTipText("Create, edit, and manage contests");
        problemManagementButton.setToolTipText("Create, edit, and manage problems");
        backButton.setToolTipText("Go back to main menu");
    }
    
    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(52, 58, 64));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Choose a management option");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(subtitleLabel, gbc);
        
        // User Management Button
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(userManagementButton, gbc);
        
        // Contest Management Button
        gbc.gridy = 3;
        mainPanel.add(contestManagementButton, gbc);
        
        // Problem Management Button
        gbc.gridy = 4;
        mainPanel.add(problemManagementButton, gbc);
        
        // Back Button
        gbc.gridy = 5;
        gbc.insets = new Insets(30, 0, 0, 0);
        mainPanel.add(backButton, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Add admin icon or decoration
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel iconLabel = new JLabel("⚙");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(52, 144, 220));
        headerPanel.add(iconLabel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void setupEventHandlers() {
        userManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUserManagement();
            }
        });
        
        contestManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleContestManagement();
            }
        });
        
        problemManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProblemManagement();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBack();
            }
        });
        
        // Allow Enter key on focused button
        getRootPane().setDefaultButton(userManagementButton);
    }
    
    private void setupFrame() {
        setTitle("OJX - Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set application icon if available
        try {
            // You can add an icon here if you have one
            // setIconImage(Toolkit.getDefaultToolkit().getImage("path/to/admin-icon.png"));
        } catch (Exception e) {
            // Icon loading failed, continue without icon
        }
    }
    
    private void handleUserManagement() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserManagementScreen().setVisible(true);
            }
        });
    }
    
    private void handleContestManagement() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContestManagementScreen().setVisible(true);
            }
        });
    }
    
    private void handleProblemManagement() {
        showInfoMessage("Problem Management", 
            "Problem Management features:\n\n" +
            "• Create new problems\n" +
            "• Edit existing problems\n" +
            "• Manage test cases\n" +
            "• Set problem difficulty\n" +
            "• Organize problem categories\n" +
            "• Review problem submissions\n\n" +
            "This feature will be implemented in the next version.");
    }
    
    private void handleBack() {
        dispose(); // Close admin screen
    }
    
    private void showInfoMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Main method for testing the AdminScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                new AdminScreen().setVisible(true);
            }
        });
    }
}
