package org.ojx.gui.contest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ojx.model.Contest;
import org.ojx.model.Problem;
import org.ojx.service.ProblemService;
import org.ojx.service.impl.ProblemServiceImpl;
import org.ojx.repository.ProblemRepository;
import org.ojx.repository.TestCaseRepository;
import org.ojx.gui.problem.ViewProblemScreen;

public class ContestDetailScreen extends JFrame {
    private Contest contest;
    private ProblemService problemService;
    private boolean isContestActive;
    
    // UI Components
    private JTextField contestIdField;
    private JTextField contestNameField;
    private JTextField startTimeField;
    private JTextField durationField;
    private JTextField statusField;
    private JPanel problemsPanel;
    private JTextArea pointsArea;
    private JButton closeButton;
    
    public ContestDetailScreen(Contest contest) {
        super("Contest Details");
        this.contest = contest;
        this.problemService = new ProblemServiceImpl(new ProblemRepository(), new TestCaseRepository());
        
        // Determine if contest is currently active
        long currentTime = System.currentTimeMillis() / 1000;
        long contestEndTime = contest.getStartedAt() + (contest.getLength() * 60 * 1000L);
        this.isContestActive = currentTime >= contest.getStartedAt() && currentTime < contestEndTime;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        setupFrame();
    }
    
    // Static factory method for easy creation
    public static void showContestDetail(JFrame parent, Contest contest) {
        ContestDetailScreen dialog = new ContestDetailScreen(contest);
        dialog.setVisible(true);
    }
    
    private void initializeComponents() {
        // Create read-only text fields
        contestIdField = new JTextField(20);
        contestIdField.setEditable(false);
        
        contestNameField = new JTextField(20);
        contestNameField.setEditable(false);
        
        startTimeField = new JTextField(20);
        startTimeField.setEditable(false);
        
        durationField = new JTextField(20);
        durationField.setEditable(false);
        
        statusField = new JTextField(20);
        statusField.setEditable(false);
        
        // Create read-only text areas
        problemsPanel = new JPanel();
        problemsPanel.setLayout(new BoxLayout(problemsPanel, BoxLayout.Y_AXIS));
        problemsPanel.setBorder(BorderFactory.createTitledBorder("Problems"));
        
        pointsArea = new JTextArea(4, 20);
        pointsArea.setEditable(false);
        pointsArea.setLineWrap(true);
        pointsArea.setWrapStyleWord(true);
        pointsArea.setBorder(BorderFactory.createTitledBorder("Points Distribution"));
        
        // Create buttons
        closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 30));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel with form fields
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Contest ID
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Contest ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(contestIdField, gbc);
        
        // Contest Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(new JLabel("Contest Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(contestNameField, gbc);
        
        // Start Time
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(startTimeField, gbc);
        
        // Duration
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(new JLabel("Duration:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(durationField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(statusField, gbc);
        
        // Problem IDs
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Problems:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.3;
        mainPanel.add(new JScrollPane(problemsPanel), gbc);
        
        // Points
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Points:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.3;
        mainPanel.add(new JScrollPane(pointsArea), gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        buttonPanel.add(closeButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Close dialog when ESC is pressed
        getRootPane().registerKeyboardAction(
            _ -> dispose(),
            KeyStroke.getKeyStroke("ESCAPE"),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void populateFields() {
        if (contest != null) {
            contestIdField.setText(String.valueOf(contest.getContestId()));
            contestNameField.setText(contest.getContestName());
            
            // Format start time
            LocalDateTime startTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(contest.getStartedAt()),
                ZoneId.systemDefault()
            );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startTimeField.setText(startTime.format(formatter));
            
            // Format duration
            int durationMinutes = contest.getLength();
            if (durationMinutes < 60) {
                durationField.setText(durationMinutes + " minutes");
            } else {
                int hours = durationMinutes / 60;
                int minutes = durationMinutes % 60;
                if (minutes == 0) {
                    durationField.setText(hours + " hour" + (hours > 1 ? "s" : ""));
                } else {
                    durationField.setText(hours + " hour" + (hours > 1 ? "s" : "") + " " + minutes + " minutes");
                }
            }
            
            // Determine status
            long currentTime = System.currentTimeMillis();
            long contestEndTime = contest.getStartedAt() * 1000 + (contest.getLength() * 60 * 1000L);
            String status;
            if (currentTime < contest.getStartedAt() * 1000) {
                status = "Upcoming";
                statusField.setForeground(Color.BLUE);
            } else if (currentTime >= contest.getStartedAt() * 1000 && currentTime < contestEndTime) {
                status = "Active";
                statusField.setForeground(Color.GREEN);
            } else {
                status = "Ended";
                statusField.setForeground(Color.RED);
            }
            statusField.setText(status);
            
            // Parse and create clickable problem list
            String problemIds = contest.getProblemIds();
            problemsPanel.removeAll(); // Clear any existing components
            
            if (problemIds != null && !problemIds.trim().isEmpty()) {
                List<String> problemIdList = Arrays.asList(problemIds.split(","));
                
                // Create header for problems list
                JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                
                JLabel headerLabel = new JLabel("Contest Problems (" + problemIdList.size() + " total)");
                headerLabel.setFont(new Font("Arial", Font.BOLD, 12));
                headerLabel.setForeground(Color.DARK_GRAY);
                headerPanel.add(headerLabel);
                
                problemsPanel.add(headerPanel);
                problemsPanel.add(Box.createVerticalStrut(8));
                
                // Create problem list using Stream API
                IntStream.range(0, problemIdList.size())
                    .forEach(i -> {
                        String problemId = problemIdList.get(i).trim();
                        
                        // Create problem panel with border and hover effect
                        JPanel problemItemPanel = createProblemItemPanel(problemId, i + 1, isContestActive);
                        problemsPanel.add(problemItemPanel);
                        
                        // Add spacing between problems
                        if (i < problemIdList.size() - 1) {
                            problemsPanel.add(Box.createVerticalStrut(3));
                        }
                    });
                
                // Add status info if contest is not active
                if (!isContestActive) {
                    problemsPanel.add(Box.createVerticalStrut(15));
                    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                    infoPanel.setBackground(new Color(255, 248, 220)); // Light yellow background
                    infoPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255, 193, 7), 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));
                    infoPanel.setOpaque(true);
                    
                    JLabel infoIcon = new JLabel("ℹ ");
                    infoIcon.setForeground(new Color(255, 193, 7));
                    infoIcon.setFont(new Font("Arial", Font.BOLD, 14));
                    
                    JLabel infoLabel = new JLabel("Problems are only viewable during active contests");
                    infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
                    infoLabel.setForeground(new Color(133, 100, 4));
                    
                    infoPanel.add(infoIcon);
                    infoPanel.add(infoLabel);
                    problemsPanel.add(infoPanel);
                }
            } else {
                // No problems assigned
                JPanel noProblemsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
                noProblemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                noProblemsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                noProblemsPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                noProblemsPanel.setBackground(Color.WHITE);
                noProblemsPanel.setOpaque(true);
                
                JLabel noProblemsLabel = new JLabel("No problems assigned to this contest");
                noProblemsLabel.setForeground(Color.GRAY);
                noProblemsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                noProblemsPanel.add(noProblemsLabel);
                
                problemsPanel.add(noProblemsPanel);
            }
            
            problemsPanel.revalidate();
            problemsPanel.repaint();
            
            // Parse and format points using Stream API
            String points = contest.getPoints();
            if (points != null && !points.trim().isEmpty()) {
                List<String> pointsList = Arrays.asList(points.split(","));
                String formattedPoints = IntStream.range(0, pointsList.size())
                    .mapToObj(i -> "Problem " + (i + 1) + ": " + pointsList.get(i).trim() + " points")
                    .collect(Collectors.joining("\n"));
                pointsArea.setText(formattedPoints);
            } else {
                pointsArea.setText("No points distribution specified");
            }
        }
    }
    
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        
        // Set minimum size
        Dimension preferredSize = getPreferredSize();
        setMinimumSize(new Dimension(
            Math.max(preferredSize.width, 500),
            Math.max(preferredSize.height, 400)
        ));
        
        // Center the frame relative to screen
        setLocationRelativeTo(null);
        
        // Set focus to close button
        SwingUtilities.invokeLater(() -> closeButton.requestFocus());
    }
    
    private void openProblemDetail(String problemIdStr) {
        try {
            int problemId = Integer.parseInt(problemIdStr);
            Optional<Problem> problemOpt = problemService.getProblemById(problemId);
            
            if (problemOpt.isPresent()) {
                // Open ViewProblemScreen with problem ID and a default user ID (1)
                // Note: In a real application, you would pass the actual logged-in user ID
                ViewProblemScreen viewScreen = new ViewProblemScreen(problemId, 1);
                viewScreen.setVisible(true);
                
                // Ensure the window is focused and brought to the top
                viewScreen.toFront();
                viewScreen.requestFocus();
                viewScreen.setAlwaysOnTop(true);
                
                // Remove always on top after a short delay to prevent it from staying on top permanently
                SwingUtilities.invokeLater(() -> {
                        viewScreen.setAlwaysOnTop(false);
                });
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Problem with ID " + problemId + " not found.", 
                    "Problem Not Found", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid problem ID: " + problemIdStr, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading problem: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createProblemItemPanel(String problemId, int problemNumber, boolean isClickable) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        itemPanel.setPreferredSize(new Dimension(400, 45));
        
        // Set different styling based on whether it's clickable
        if (isClickable) {
            itemPanel.setBackground(new Color(248, 249, 250));
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 144, 220), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            // Add hover effect
            itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    itemPanel.setBackground(new Color(232, 244, 255));
                    itemPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 144, 220), 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                    ));
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    itemPanel.setBackground(new Color(248, 249, 250));
                    itemPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 144, 220), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                }
                
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    openProblemDetail(problemId);
                }
            });
        } else {
            itemPanel.setBackground(new Color(248, 248, 248));
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        }
        
        itemPanel.setOpaque(true);
        
        // Left side - Problem info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        
        // Problem number badge
        JLabel numberLabel = new JLabel(String.valueOf(problemNumber));
        numberLabel.setOpaque(true);
        numberLabel.setBackground(isClickable ? new Color(52, 144, 220) : Color.GRAY);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numberLabel.setPreferredSize(new Dimension(24, 24));
        numberLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        numberLabel.setFont(new Font("Arial", Font.BOLD, 11));
        
        // Problem ID label
        JLabel problemLabel = new JLabel("  Problem " + problemId);
        problemLabel.setFont(new Font("Arial", Font.BOLD, 13));
        problemLabel.setForeground(isClickable ? new Color(52, 144, 220) : Color.GRAY);
        
        leftPanel.add(numberLabel);
        leftPanel.add(problemLabel);
        
        // Right side - Status/Action indicator
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        
        if (isClickable) {
            JLabel actionLabel = new JLabel("Click to view >");
            actionLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            actionLabel.setForeground(new Color(108, 117, 125));
            rightPanel.add(actionLabel);
        } else {
            JLabel lockLabel = new JLabel("🔒 Locked");
            lockLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            lockLabel.setForeground(Color.GRAY);
            rightPanel.add(lockLabel);
        }
        
        itemPanel.add(leftPanel, BorderLayout.WEST);
        itemPanel.add(rightPanel, BorderLayout.EAST);
        
        return itemPanel;
    }
}
