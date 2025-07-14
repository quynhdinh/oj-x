package org.ojx.gui.problem;

import javax.swing.*;
import org.ojx.model.Problem;
import org.ojx.model.TestCase;
import org.ojx.model.User;
import org.ojx.repository.ProblemRepository;
import org.ojx.service.ProblemService;
import org.ojx.service.SubmissionService;
import org.ojx.service.TestCaseService;
import org.ojx.service.impl.ProblemServiceImpl;
import org.ojx.service.impl.SubmissionServiceImpl;
import org.ojx.service.impl.TestCaseServiceImpl;
import org.ojx.service.impl.UserServiceImpl;
import org.ojx.repository.TestCaseRepository;
import org.ojx.repository.UserRepository;
import org.ojx.repository.SubmissionRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class ViewProblemScreen extends JFrame {
    private JTextField problemIdField;
    private JTextField problemNameField;
    private JTextField difficultyField;
    private JTextField tagsField;
    private JTextArea problemStatementArea;
    private JTextArea testCasesArea;
    private JTextArea sourceCodeArea;
    private JComboBox<String> languageComboBox;
    private JButton submitCodeButton;
    private JButton backButton;

    private ProblemService problemService;
    private SubmissionService submissionService;
    private TestCaseService testCaseService;
    private int problemId;
    private int userId;

    public ViewProblemScreen(int problemId, int userId) {
        this.problemId = problemId;
        this.userId = userId;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        problemService = new ProblemServiceImpl(new ProblemRepository(), new TestCaseRepository());
        submissionService = new SubmissionServiceImpl(new SubmissionRepository());
        testCaseService = new TestCaseServiceImpl(new TestCaseRepository());
        loadProblemData();
    }
    private void setFieldsEditable(){
        User user = (new UserServiceImpl(new UserRepository())).getById(userId).orElse(null);
        if (user != null && user.getUserType().toString().equals("admin")) {
            problemIdField.setEditable(true);
            problemNameField.setEditable(true);
            difficultyField.setEditable(true);
            tagsField.setEditable(true);
            problemStatementArea.setEditable(true);
            testCasesArea.setEditable(true);
        } else {
            problemIdField.setEditable(false);
            problemNameField.setEditable(false);
            difficultyField.setEditable(false);
            tagsField.setEditable(false);
            problemStatementArea.setEditable(false);
            testCasesArea.setEditable(false);
        }
    }

    private void initializeComponents() {
        // Create read-only text fields for problem details
        problemIdField = new JTextField(30);
        // problemIdField.setEditable(false);
        problemIdField.setBackground(Color.WHITE);
        
        problemNameField = new JTextField(30);
        // problemNameField.setEditable(false);
        problemNameField.setBackground(Color.WHITE);
        
        difficultyField = new JTextField(30);
        // difficultyField.setEditable(false);
        difficultyField.setBackground(Color.WHITE);
        
        tagsField = new JTextField(30);
        // tagsField.setEditable(false);
        tagsField.setBackground(Color.WHITE);

        // Create problem statement area (read-only)
        problemStatementArea = new JTextArea(5, 60);
        // problemStatementArea.setEditable(false);
        problemStatementArea.setBackground(Color.WHITE);
        problemStatementArea.setFont(new Font("Arial", Font.PLAIN, 14));
        problemStatementArea.setLineWrap(true);
        problemStatementArea.setWrapStyleWord(true);
        problemStatementArea.setBorder(BorderFactory.createLoweredBevelBorder());

        // Create test cases area (read-only)
        testCasesArea = new JTextArea(4, 60);
        // testCasesArea.setEditable(false);
        testCasesArea.setBackground(Color.WHITE);
        testCasesArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        testCasesArea.setLineWrap(false);
        testCasesArea.setBorder(BorderFactory.createLoweredBevelBorder());

        setFieldsEditable();
        // Create large text area for source code submission
        sourceCodeArea = new JTextArea(20, 60);
        sourceCodeArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        sourceCodeArea.setLineWrap(false);
        sourceCodeArea.setBorder(BorderFactory.createLoweredBevelBorder());
        sourceCodeArea.setTabSize(4);

        // Language selection combo box
        String[] languages = {"Java", "C++", "Python", "JavaScript", "C"};
        languageComboBox = new JComboBox<>(languages);
        languageComboBox.setPreferredSize(new Dimension(150, 30));

        // Create buttons
        submitCodeButton = new JButton("Submit");
        submitCodeButton.setPreferredSize(new Dimension(120, 35));
        submitCodeButton.setBackground(new Color(0, 123, 255));
        submitCodeButton.setForeground(Color.WHITE);
        submitCodeButton.setOpaque(true);
        submitCodeButton.setBorderPainted(false);

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 35));

        // Set preferred sizes for fields
        Dimension fieldSize = new Dimension(400, 30);
        problemIdField.setPreferredSize(fieldSize);
        problemNameField.setPreferredSize(fieldSize);
        difficultyField.setPreferredSize(fieldSize);
        tagsField.setPreferredSize(fieldSize);

        // Add tooltips
        problemIdField.setToolTipText("Problem identifier");
        problemNameField.setToolTipText("Problem title");
        difficultyField.setToolTipText("Problem difficulty level");
        tagsField.setToolTipText("Problem categories and topics");
        problemStatementArea.setToolTipText("Problem description and requirements");
        testCasesArea.setToolTipText("Sample test cases with input and expected output");
        sourceCodeArea.setToolTipText("Enter your solution code here");
        languageComboBox.setToolTipText("Select programming language");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Problem Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Problem details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Problem Information"));

        // Problem ID field
        addDetailField(detailsPanel, gbc, "Problem ID:", problemIdField, 0);

        // Problem Name field
        addDetailField(detailsPanel, gbc, "Problem Name:", problemNameField, 1);

        // Difficulty field
        addDetailField(detailsPanel, gbc, "Difficulty:", difficultyField, 2);

        // Tags field
        addDetailField(detailsPanel, gbc, "Tags:", tagsField, 3);

        // Problem Statement panel
        JPanel statementPanel = new JPanel(new BorderLayout());
        statementPanel.setBorder(BorderFactory.createTitledBorder("Problem Statement"));
        
        JScrollPane statementScrollPane = new JScrollPane(problemStatementArea);
        statementScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        statementScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statementScrollPane.setPreferredSize(new Dimension(700, 120));
        
        statementPanel.add(statementScrollPane, BorderLayout.CENTER);

        // Test Cases panel
        JPanel testCasesPanel = new JPanel(new BorderLayout());
        testCasesPanel.setBorder(BorderFactory.createTitledBorder("Sample Test Cases"));
        
        JScrollPane testCasesScrollPane = new JScrollPane(testCasesArea);
        testCasesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        testCasesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        testCasesScrollPane.setPreferredSize(new Dimension(700, 100));
        
        testCasesPanel.add(testCasesScrollPane, BorderLayout.CENTER);

        // Submission panel
        JPanel submissionPanel = new JPanel(new BorderLayout());
        submissionPanel.setBorder(BorderFactory.createTitledBorder("Submit Your Solution"));
        
        // Language selection panel
        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        languagePanel.add(new JLabel("Language:"));
        languagePanel.add(Box.createHorizontalStrut(10));
        languagePanel.add(languageComboBox);
        
        // Source code area
        JScrollPane sourceCodeScrollPane = new JScrollPane(sourceCodeArea);
        sourceCodeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sourceCodeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sourceCodeScrollPane.setPreferredSize(new Dimension(700, 400));
        
        submissionPanel.add(languagePanel, BorderLayout.NORTH);
        submissionPanel.add(sourceCodeScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(submitCodeButton);
        buttonPanel.add(backButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel topCenterPanel = new JPanel(new BorderLayout());
        topCenterPanel.add(detailsPanel, BorderLayout.NORTH);
        
        JPanel statementAndTestCasesPanel = new JPanel(new BorderLayout());
        statementAndTestCasesPanel.add(statementPanel, BorderLayout.NORTH);
        statementAndTestCasesPanel.add(testCasesPanel, BorderLayout.CENTER);
        
        topCenterPanel.add(statementAndTestCasesPanel, BorderLayout.CENTER);
        
        centerPanel.add(topCenterPanel, BorderLayout.NORTH);
        centerPanel.add(submissionPanel, BorderLayout.CENTER);
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
        submitCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmitCode();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBack();
            }
        });

        // Allow Escape key to go back
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBack();
            }
        });
    }

    private void setupFrame() {
        setTitle("OJX - View Problem");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set minimum size to prevent UI elements from being too cramped
        setMinimumSize(new Dimension(700, 700));
    }

    private void loadProblemData() {
        try {
            Optional<Problem> problemOpt = problemService.getProblemById(problemId);
            
            if (problemOpt.isPresent()) {
                Problem problem = problemOpt.get();
                populateFieldsFromProblem(problem);
                loadTestCases();
            } else {
                showErrorMessage("Problem not found with ID: " + problemId);
            }
            
        } catch (Exception e) {
            showErrorMessage("Error loading problem: " + e.getMessage());
        }
    }

    private void populateFieldsFromProblem(Problem problem) {
        problemIdField.setText(String.valueOf(problem.getProblemId()));
        problemNameField.setText(problem.getProblemName());
        difficultyField.setText(problem.getDifficulty());
        
        // Format tags as comma-separated string
        if (problem.getTags() != null && !problem.getTags().isEmpty()) {
            tagsField.setText(String.join(", ", problem.getTags()));
        } else {
            tagsField.setText("No tags");
        }
        
        // Set problem statement
        if (problem.getProblemStatement() != null) {
            problemStatementArea.setText(problem.getProblemStatement());
        } else {
            problemStatementArea.setText("Problem statement not available.");
        }
    }

    private void loadTestCases() {
        try {
            List<TestCase> sampleTestCases = testCaseService.getSampleTestCasesByProblemId(problemId);
            
            if (sampleTestCases != null && !sampleTestCases.isEmpty()) {
                // Build test cases display using Stream API
                String testCasesText = java.util.stream.IntStream.range(0, sampleTestCases.size())
                    .mapToObj(i -> {
                        TestCase testCase = sampleTestCases.get(i);
                        StringBuilder caseText = new StringBuilder();
                        caseText.append("Test Case ").append(i + 1).append(":\n");
                        caseText.append("Input:\n").append(testCase.getInput()).append("\n\n");
                        caseText.append("Expected Output:\n").append(testCase.getOutput()).append("\n");
                        
                        if (i < sampleTestCases.size() - 1) {
                            caseText.append("\n" + "=".repeat(50) + "\n\n");
                        }
                        return caseText.toString();
                    })
                    .collect(java.util.stream.Collectors.joining(""));
                
                testCasesArea.setText(testCasesText);
            } else {
                testCasesArea.setText("No sample test cases available for this problem.");
            }
            
        } catch (Exception e) {
            testCasesArea.setText("Error loading test cases: " + e.getMessage());
        }
    }

    private void handleSubmitCode() {
        String sourceCode = sourceCodeArea.getText().trim();
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        
        // Validate input
        if (sourceCode.isEmpty()) {
            showErrorMessage("Please enter your source code.");
            sourceCodeArea.requestFocus();
            return;
        }
        
        if (selectedLanguage == null) {
            showErrorMessage("Please select a programming language.");
            return;
        }
        
        try {
            // Submit the solution
            boolean success = submissionService.submitSolution(userId, problemId, selectedLanguage, sourceCode);
            
            if (success) {
                showSuccessMessage("Solution submitted successfully!");
                // Clear the source code area after successful submission
                sourceCodeArea.setText("");
            } else {
                showErrorMessage("Failed to submit solution. Please try again.");
            }
            
        } catch (Exception e) {
            showErrorMessage("Error submitting solution: " + e.getMessage());
        }
    }

    private void handleBack() {
        dispose(); // Close this window
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Getters for external access
    public int getProblemId() {
        return problemId;
    }

    public String getDisplayedProblemName() {
        return problemNameField.getText();
    }

    public String getSourceCode() {
        return sourceCodeArea.getText();
    }

    public String getSelectedLanguage() {
        return (String) languageComboBox.getSelectedItem();
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
                
                // Test with a sample problem ID and user ID
                new ViewProblemScreen(1, 1).setVisible(true);
            }
        });
    }
}
