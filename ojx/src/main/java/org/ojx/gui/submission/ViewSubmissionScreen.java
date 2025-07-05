package org.ojx.gui.submission;

import javax.swing.*;

import org.ojx.dto.SubmissionDetailDTO;
import org.ojx.dto.SubmissionResDTO;
import org.ojx.model.Submission;
import org.ojx.repository.SubmissionRepository;
import org.ojx.service.SubmissionService;
import org.ojx.service.impl.SubmissionServiceImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class ViewSubmissionScreen extends JFrame {
    private JTextField usernameField;
    private JTextField programmingLanguageField;
    private JTextField problemIdField;
    private JTextField problemNameField;
    private JTextArea sourceCodeArea;
    private JTextField judgeStatusField;
    private JTextField submissionTimeField;
    private JButton quitButton;

    private SubmissionService submissionService;
    private int submissionId;

    public ViewSubmissionScreen(int submissionId) {
        this.submissionId = submissionId;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        submissionService = new SubmissionServiceImpl(new SubmissionRepository());
        loadSubmissionData();
    }

    private void initializeComponents() {
        // Create read-only text fields
        usernameField = new JTextField(30);
        usernameField.setEditable(false);
        usernameField.setBackground(Color.WHITE);
        
        programmingLanguageField = new JTextField(30);
        programmingLanguageField.setEditable(false);
        programmingLanguageField.setBackground(Color.WHITE);
        
        problemIdField = new JTextField(30);
        problemIdField.setEditable(false);
        problemIdField.setBackground(Color.WHITE);
        
        problemNameField = new JTextField(30);
        problemNameField.setEditable(false);
        problemNameField.setBackground(Color.WHITE);
        
        judgeStatusField = new JTextField(30);
        judgeStatusField.setEditable(false);
        judgeStatusField.setBackground(Color.WHITE);
        
        submissionTimeField = new JTextField(30);
        submissionTimeField.setEditable(false);
        submissionTimeField.setBackground(Color.WHITE);

        // Create large text area for source code
        sourceCodeArea = new JTextArea(15, 50);
        sourceCodeArea.setEditable(false);
        sourceCodeArea.setBackground(Color.WHITE);
        sourceCodeArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        sourceCodeArea.setLineWrap(true);
        sourceCodeArea.setWrapStyleWord(false); // Don't wrap words for code
        sourceCodeArea.setBorder(BorderFactory.createLoweredBevelBorder());

        // Create quit button
        quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(100, 35));

        // Set preferred sizes for fields
        Dimension fieldSize = new Dimension(400, 30);
        usernameField.setPreferredSize(fieldSize);
        programmingLanguageField.setPreferredSize(fieldSize);
        problemIdField.setPreferredSize(fieldSize);
        problemNameField.setPreferredSize(fieldSize);
        judgeStatusField.setPreferredSize(fieldSize);
        submissionTimeField.setPreferredSize(fieldSize);

        // Add tooltips
        usernameField.setToolTipText("User who submitted this solution");
        programmingLanguageField.setToolTipText("Programming language used");
        problemIdField.setToolTipText("Problem identifier");
        problemNameField.setToolTipText("Problem title");
        sourceCodeArea.setToolTipText("Submitted source code");
        judgeStatusField.setToolTipText("Judge evaluation result");
        submissionTimeField.setToolTipText("When this submission was made");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Submission Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Submission Information"));

        // Username field
        addDetailField(detailsPanel, gbc, "Username:", usernameField, 0);

        // Programming Language field
        addDetailField(detailsPanel, gbc, "Programming Language:", programmingLanguageField, 1);

        // Problem ID field
        addDetailField(detailsPanel, gbc, "Problem ID:", problemIdField, 2);

        // Problem Name field
        addDetailField(detailsPanel, gbc, "Problem Name:", problemNameField, 3);

        // Judge Status field
        addDetailField(detailsPanel, gbc, "Judge Status:", judgeStatusField, 4);

        // Submission Time field
        addDetailField(detailsPanel, gbc, "Submission Time:", submissionTimeField, 5);

        // Source Code panel
        JPanel sourceCodePanel = new JPanel(new BorderLayout());
        sourceCodePanel.setBorder(BorderFactory.createTitledBorder("Source Code"));
        
        JScrollPane sourceCodeScrollPane = new JScrollPane(sourceCodeArea);
        sourceCodeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sourceCodeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sourceCodeScrollPane.setPreferredSize(new Dimension(700, 300));
        
        sourceCodePanel.add(sourceCodeScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(quitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(detailsPanel, BorderLayout.NORTH);
        centerPanel.add(sourceCodePanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addDetailField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, gbc);

        // Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void setupEventHandlers() {
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });

        // Allow Escape key to close the window
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });
    }

    private void setupFrame() {
        setTitle("OJX - View Submission");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set minimum size to prevent UI elements from being too cramped
        setMinimumSize(new Dimension(600, 500));
    }

    private void loadSubmissionData() {
        try {
            Optional<SubmissionDetailDTO> submissionOpt = submissionService.getSubmissionDetail(submissionId);
            if (submissionOpt.isPresent()) {
                SubmissionDetailDTO submission = submissionOpt.get();

                // Create SubmissionResDTO-like object for display
                // Note: We'll need to get problem name separately if it's not available in Submission
                populateFieldsFromSubmission(submission);
            } else {
                showErrorMessage("Submission not found with ID: " + submissionId);
            }
            
        } catch (Exception e) {
            showErrorMessage("Error loading submission: " + e.getMessage());
        }
    }

    private void populateFieldsFromSubmission(SubmissionDetailDTO submission) {
        usernameField.setText(submission.user_name()); // You might want to fetch actual username
        programmingLanguageField.setText(submission.language());
        problemIdField.setText(String.valueOf(submission.problemId())); // Problem ID not available in current Submission model
        problemNameField.setText(submission.problem_name()); // Problem name not available
        sourceCodeArea.setText(submission.sourceCode());
        judgeStatusField.setText(getFormattedJudgeStatus(submission.judgeStatus()));

        // Format submission time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        submissionTimeField.setText(dateFormat.format(new Date(submission.createdAt())));
    }

    private String getFormattedJudgeStatus(String status) {
        if (status == null) return "Unknown";
        
        switch (status) {
            case "AC":
                return "Accepted";
            case "WA":
                return "Wrong Answer";
            case "TLE":
                return "Time Limit Exceeded";
            case "MLE":
                return "Memory Limit Exceeded";
            case "RE":
                return "Runtime Error";
            case "CE":
                return "Compilation Error";
            default:
                return status;
        }
    }

    private void handleQuit() {
        dispose(); // Close this window
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Getters for external access
    public int getSubmissionId() {
        return submissionId;
    }

    public String getDisplayedSourceCode() {
        return sourceCodeArea.getText();
    }

    public String getDisplayedJudgeStatus() {
        return judgeStatusField.getText();
    }

    // Static factory method for easy creation from SubmissionScreen
    public static void showSubmission(int submissionId) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ViewSubmissionScreen(submissionId).setVisible(true);
            }
        });
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new ViewSubmissionScreen(1).setVisible(true);
            }
        });
    }
}
