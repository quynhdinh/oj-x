package org.ojx.gui;

import javax.swing.*;

import org.ojx.gui.admin.AdminScreen;
import org.ojx.gui.contest.ContestScreen;
import org.ojx.gui.login.LoginScreen;
import org.ojx.gui.problem.ProblemsetScreen;
import org.ojx.gui.profile.UserProfileScreen;
import org.ojx.gui.submission.SubmissionScreen;
import org.ojx.model.User;
import org.ojx.service.UserService;
import org.ojx.service.impl.UserServiceImpl;
import org.ojx.repository.UserRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class HomeScreen extends JFrame {
    private JButton problemsetButton;
    private JButton contestButton;
    private JButton submissionButton;
    private JButton profileButton;
    private JButton adminToolsButton;
    private JButton signoutButton;
    private JLabel welcomeLabel;
    private int userId;
    private UserService userService;
    private boolean isAdmin = false;
    
    public HomeScreen(int userId) {
        this.userId = userId;
        this.userService = new UserServiceImpl(new UserRepository());
        
        initializeComponents();
        updateWelcomeLabel(); // Call this before setupLayout to set isAdmin flag
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        // Create welcome label with user info
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Create buttons
        problemsetButton = new JButton("Problemset");
        contestButton = new JButton("Contest");
        submissionButton = new JButton("Submissions");
        profileButton = new JButton("Profile");
        adminToolsButton = new JButton("Admin Tools");
        signoutButton = new JButton("Sign Out");
        
        // Set preferred sizes for buttons
        Dimension buttonSize = new Dimension(150, 40);
        problemsetButton.setPreferredSize(buttonSize);
        contestButton.setPreferredSize(buttonSize);
        submissionButton.setPreferredSize(buttonSize);
        profileButton.setPreferredSize(buttonSize);
        adminToolsButton.setPreferredSize(buttonSize);
        signoutButton.setPreferredSize(buttonSize);
        
        // Set fonts
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        problemsetButton.setFont(buttonFont);
        contestButton.setFont(buttonFont);
        submissionButton.setFont(buttonFont);
        profileButton.setFont(buttonFont);
        adminToolsButton.setFont(buttonFont);
        signoutButton.setFont(buttonFont);
        
        // Add tooltips
        problemsetButton.setToolTipText("Browse and solve programming problems");
        contestButton.setToolTipText("View and participate in contests");
        submissionButton.setToolTipText("View your submission history");
        profileButton.setToolTipText("Manage your profile and settings");
        adminToolsButton.setToolTipText("Access administrative tools and management features");
        signoutButton.setToolTipText("Sign out of your account");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create main panel with vertical layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("OJX - Online Judge System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // Welcome label
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 20, 30, 20);
        mainPanel.add(welcomeLabel, gbc);
        
        // Problemset button
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(problemsetButton, gbc);
        
        // Contest button
        gbc.gridy = 3;
        mainPanel.add(contestButton, gbc);
        
        // Submission button
        gbc.gridy = 4;
        mainPanel.add(submissionButton, gbc);
        
        // Profile button
        gbc.gridy = 5;
        mainPanel.add(profileButton, gbc);

        // Admin Tools button (only for admins)
        if (isAdmin) {
            gbc.gridy = 6;
            mainPanel.add(adminToolsButton, gbc);
        }

        // Sign Out button
        gbc.gridy = isAdmin ? 7 : 6;
        gbc.insets = new Insets(10, 20, 30, 20);
        mainPanel.add(signoutButton, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        problemsetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProblemset();
            }
        });
        
        contestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleContest();
            }
        });
        
        submissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmission();
            }
        });
        
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfile();
            }
        });

        adminToolsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdminTools();
            }
        });

        signoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                    HomeScreen.this,
                    "Are you sure you want to sign out?",
                    "Confirm Sign Out",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    // Close current home screen
                    dispose();
                    
                    // Open login screen
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new LoginScreen().setVisible(true);
                        }
                    });
                }
            }
        });
    }
    
    private void setupFrame() {
        setTitle("OJX Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);
    }
    
    private void handleProblemset() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProblemsetScreen(userId).setVisible(true);
            }
        });
    }
    
    private void handleContest() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContestScreen().setVisible(true);
            }
        });
    }
    
    private void handleSubmission() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SubmissionScreen(userId).setVisible(true);
            }
        });
    }
    
    private void handleProfile() {
        // Open the UserProfileScreen with the current userId
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserProfileScreen(userId).setVisible(true);
            }
        });
    }
    
    private void handleAdminTools() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminScreen().setVisible(true);
            }
        });
    }
    
    private void updateWelcomeLabel() {
        try {
            Optional<User> userOpt = userService.getById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String role = user.getUserType();
                String username = user.getUserName();

                // Check if user is admin
                isAdmin = "admin".equalsIgnoreCase(role);

                welcomeLabel.setText("Hi " + role + " " + username + "!");
                welcomeLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
            } else {
                welcomeLabel.setText("Hi User (Unknown)!");
                welcomeLabel.setForeground(Color.GRAY);
                isAdmin = false;
            }
        } catch (Exception e) {
            welcomeLabel.setText("Hi User!");
            welcomeLabel.setForeground(Color.GRAY);
            isAdmin = false;
        }
    }
    
    // Main method for testing the MainScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new HomeScreen(1).setVisible(true);
            }
        });
    }
}
