package org.ojx.gui.admin;

import javax.swing.*;
import org.ojx.model.Contest;
import org.ojx.dto.ContestDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateCreateContestScreen extends JFrame {
    private Contest contest;

    // UI Components
    private JTextField contestIdField;
    private JTextField contestNameField;
    private JTextField lengthField;
    private JTextField startedAtField;
    private JTextArea problemIdsArea;
    private JTextArea pointsArea;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton quitButton;

    public UpdateCreateContestScreen(Contest contest) {
        this.contest = contest;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        setupFrame();
    }

    // Static factory method for easy creation
    public static void showContestEditor(Contest contest) {
        UpdateCreateContestScreen screen = new UpdateCreateContestScreen(contest);
        screen.setVisible(true);
    }

    private void initializeComponents() {
        // Create text fields
        contestIdField = new JTextField(20);
        contestIdField.setEditable(false);
        contestIdField.setBackground(new Color(245, 245, 245)); // Light gray for read-only

        contestNameField = new JTextField(20);
        contestNameField.setBackground(Color.WHITE);

        lengthField = new JTextField(20);
        lengthField.setBackground(Color.WHITE);

        startedAtField = new JTextField(20);
        startedAtField.setBackground(Color.WHITE);

        // Create text areas for longer fields
        problemIdsArea = new JTextArea(3, 20);
        problemIdsArea.setBackground(Color.WHITE);
        problemIdsArea.setLineWrap(true);
        problemIdsArea.setWrapStyleWord(true);
        problemIdsArea.setBorder(BorderFactory.createLoweredBevelBorder());

        pointsArea = new JTextArea(3, 20);
        pointsArea.setBackground(Color.WHITE);
        pointsArea.setLineWrap(true);
        pointsArea.setWrapStyleWord(true);
        pointsArea.setBorder(BorderFactory.createLoweredBevelBorder());

        // Create buttons
        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete");
        quitButton = new JButton("Quit");

        saveButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setPreferredSize(new Dimension(100, 30));
        quitButton.setPreferredSize(new Dimension(100, 30));

        // Set button colors
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);

        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);

        quitButton.setBackground(new Color(108, 117, 125));
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(false);

        // Add tooltips
        contestIdField.setToolTipText("Contest ID (cannot be edited)");
        contestNameField.setToolTipText("Name of the contest");
        lengthField.setToolTipText("Contest duration in minutes");
        startedAtField.setToolTipText("Start time (Unix timestamp)");
        problemIdsArea.setToolTipText("Comma-separated problem IDs (e.g., 1,2,3)");
        pointsArea.setToolTipText("Comma-separated points for each problem (e.g., 100,200,300)");
        saveButton.setToolTipText("Save changes to contest");
        deleteButton.setToolTipText("Delete this contest");
        quitButton.setToolTipText("Close this window");

        if (!isUpdateForm())
            deleteButton.setVisible(false);
    }

    private boolean isUpdateForm() {
        return this.contest.getContestId() != 0;
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
        JLabel titleLabel = new JLabel(isUpdateForm() ? "Update Contest" : "Create Contest");
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

        // Contest ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        JLabel contestIdLabel = new JLabel("Contest ID:");
        contestIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(contestIdLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(contestIdField, gbc);

        // Contest Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel contestNameLabel = new JLabel("Contest Name:");
        contestNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(contestNameLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(contestNameField, gbc);

        // Length
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lengthLabel = new JLabel("Length (minutes):");
        lengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(lengthLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(lengthField, gbc);

        // Started At
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel startedAtLabel = new JLabel("Start Time:");
        startedAtLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(startedAtLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(startedAtField, gbc);

        // Problem IDs
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel problemIdsLabel = new JLabel("Problem IDs:");
        problemIdsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(problemIdsLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        JScrollPane problemIdsScrollPane = new JScrollPane(problemIdsArea);
        problemIdsScrollPane.setPreferredSize(new Dimension(300, 80));
        mainPanel.add(problemIdsScrollPane, gbc);

        // Points
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel pointsLabel = new JLabel("Points:");
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(pointsLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        JScrollPane pointsScrollPane = new JScrollPane(pointsArea);
        pointsScrollPane.setPreferredSize(new Dimension(300, 80));
        mainPanel.add(pointsScrollPane, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(quitButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelete();
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
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void populateFields() {
        if (contest != null) {
            contestIdField.setText(String.valueOf(contest.getContestId()));
            contestNameField.setText(contest.getContestName());
            lengthField.setText(String.valueOf(contest.getLength()));

            // Format timestamp for display
            if (contest.getStartedAt() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(contest.getStartedAt() * 1000L); // Convert from Unix timestamp
                startedAtField.setText(sdf.format(date) + " (" + contest.getStartedAt() + ")");
            } else {
                startedAtField.setText(String.valueOf(contest.getStartedAt()));
            }

            problemIdsArea.setText(contest.getProblemIds() != null ? contest.getProblemIds() : "");
            pointsArea.setText(contest.getPoints() != null ? contest.getPoints() : "");
        }
    }

    private void setupFrame() {
        setTitle("OJX - Update Contest");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();

        // Set minimum size
        Dimension preferredSize = getPreferredSize();
        setMinimumSize(new Dimension(
                Math.max(preferredSize.width, 500),
                Math.max(preferredSize.height, 400)));

        // Center the window
        setLocationRelativeTo(null);

        // Set focus to contest name field
        SwingUtilities.invokeLater(() -> contestNameField.requestFocus());
    }

    private void handleSave() {
        try {
            // Validate input fields
            if (!validateFields()) {
                return;
            }

            // Get field values
            String contestName = contestNameField.getText().trim();
            int length = Integer.parseInt(lengthField.getText().trim());
            long startedAt = parseStartTime(startedAtField.getText().trim());
            String problemIds = problemIdsArea.getText().trim();
            String points = pointsArea.getText().trim();

            // Create ContestDTO for update
            ContestDTO updatedContest = new ContestDTO(
                    contestName, length, startedAt, problemIds, points);

            showInfoMessage("Save Contest",
                    "Contest update functionality will include:\n\n" +
                            "• Update contest information\n" +
                            "• Modify contest schedule\n" +
                            "• Change problem assignments\n" +
                            "• Update point distribution\n\n" +
                            "Contest Data to Save:\n" +
                            "Name: " + updatedContest.contestName() + "\n" +
                            "Length: " + updatedContest.length() + " minutes\n" +
                            "Start Time: " + updatedContest.startedAt() + "\n" +
                            "Problems: " + updatedContest.problemIds() + "\n" +
                            "Points: " + updatedContest.points() + "\n\n" +
                            "This feature will be implemented in the next version.");

        } catch (Exception e) {
            showErrorMessage("Error saving contest: " + e.getMessage());
        }
    }

    private void handleDelete() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete contest '" + contest.getContestName() + "'?\n\n" +
                        "This action cannot be undone and will:\n" +
                        "• Remove the contest permanently\n" +
                        "• Delete all associated submissions\n" +
                        "• Remove contest from leaderboards\n\n" +
                        "Do you want to continue?",
                "Confirm Contest Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            showInfoMessage("Delete Contest",
                    "Contest deletion functionality will include:\n\n" +
                            "• Remove contest from database\n" +
                            "• Clean up associated data\n" +
                            "• Update leaderboards\n" +
                            "• Notify participants\n\n" +
                            "Contest '" + contest.getContestName() + "' would be deleted.\n\n" +
                            "This feature will be implemented in the next version.");
        }
    }

    private void handleQuit() {
        dispose(); // Close the window
    }

    private boolean validateFields() {
        // Contest name validation
        String contestName = contestNameField.getText().trim();
        if (contestName.isEmpty()) {
            showErrorMessage("Contest name is required.");
            contestNameField.requestFocus();
            return false;
        }
        if (contestName.length() > 100) {
            showErrorMessage("Contest name must be 100 characters or less.");
            contestNameField.requestFocus();
            return false;
        }

        // Length validation
        String lengthText = lengthField.getText().trim();
        if (lengthText.isEmpty()) {
            showErrorMessage("Contest length is required.");
            lengthField.requestFocus();
            return false;
        }
        try {
            int length = Integer.parseInt(lengthText);
            if (length <= 0) {
                showErrorMessage("Contest length must be a positive number.");
                lengthField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter a valid number for contest length.");
            lengthField.requestFocus();
            return false;
        }

        // Start time validation
        String startTimeText = startedAtField.getText().trim();
        if (startTimeText.isEmpty()) {
            showErrorMessage("Start time is required.");
            startedAtField.requestFocus();
            return false;
        }

        try {
            parseStartTime(startTimeText);
        } catch (Exception e) {
            showErrorMessage("Invalid start time format. Use Unix timestamp or YYYY-MM-DD HH:MM:SS format.");
            startedAtField.requestFocus();
            return false;
        }

        return true;
    }

    private long parseStartTime(String timeText) throws Exception {
        // Try to parse as Unix timestamp first
        try {
            return Long.parseLong(timeText.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            // Try to parse as date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(timeText);
            return date.getTime() / 1000L; // Convert to Unix timestamp
        }
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
    public Contest getContest() {
        return contest;
    }

    // Main method for testing the UpdateContestScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Create a sample contest for testing
                Contest sampleContest = new Contest(
                        1,
                        "Sample Programming Contest",
                        180,
                        System.currentTimeMillis() / 1000L,
                        "1,2,3,4,5",
                        "100,200,300,400,500");

                new UpdateCreateContestScreen(sampleContest).setVisible(true);
            }
        });
    }
}