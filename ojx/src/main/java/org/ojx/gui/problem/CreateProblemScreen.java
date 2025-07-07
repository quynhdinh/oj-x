package org.ojx.gui.problem;

import javax.swing.*;
import org.ojx.dto.CreateProblemDTO;
import org.ojx.model.Problem;
import org.ojx.model.TestCase;
import org.ojx.repository.ProblemRepository;
import org.ojx.repository.TestCaseRepository;
import org.ojx.service.ProblemService;
import org.ojx.service.impl.ProblemServiceImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CreateProblemScreen extends JFrame {
    private JTextField problemIdField;
    private JTextField problemNameField;
    private JTextArea problemStatementArea;
    private JComboBox<String> difficultyComboBox;
    private JTextField tagsField;
    private JCheckBox contestCheckBox;
    private JPanel testCasesPanel;
    private JScrollPane testCasesScrollPane;
    private JButton addTestCaseButton;
    private JButton submitButton;
    private JButton editButton;
    private JButton backButton;
    private List<TestCaseInputPanel> testCasePanels;

    // Inner class for test case input panels
    private class TestCaseInputPanel extends JPanel {
        private JTextArea inputArea;
        private JTextArea outputArea;
        private JButton removeButton;
        private int testCaseNumber;

        public TestCaseInputPanel(int number) {
            this.testCaseNumber = number;
            initializeTestCaseComponents();
            setupTestCaseLayout();
        }

        private void initializeTestCaseComponents() {
            inputArea = new JTextArea(3, 30);
            inputArea.setLineWrap(true);
            inputArea.setWrapStyleWord(true);
            inputArea.setBorder(BorderFactory.createTitledBorder("Input"));

            outputArea = new JTextArea(3, 30);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setBorder(BorderFactory.createTitledBorder("Expected Output"));

            removeButton = new JButton("Remove");
            removeButton.setPreferredSize(new Dimension(80, 30));
            removeButton.addActionListener(e -> CreateProblemScreen.this.removeTestCase(this));
        }

        private void setupTestCaseLayout() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createTitledBorder("Test Case " + testCaseNumber));

            JPanel inputOutputPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            inputOutputPanel.add(new JScrollPane(inputArea));
            inputOutputPanel.add(new JScrollPane(outputArea));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(removeButton);

            add(inputOutputPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        public String getInput() {
            return inputArea.getText().trim();
        }

        public String getExpectedOutput() {
            return outputArea.getText().trim();
        }

        public void setInput(String input) {
            inputArea.setText(input);
        }

        public void setExpectedOutput(String output) {
            outputArea.setText(output);
        }

        public void setEditable(boolean editable) {
            inputArea.setEditable(editable);
            outputArea.setEditable(editable);
            removeButton.setEnabled(editable);
        }

        public boolean validateTestCase() {
            if (getInput().isEmpty()) {
                CreateProblemScreen.this.showErrorMessage("Test case " + testCaseNumber + " input cannot be empty");
                inputArea.requestFocus();
                return false;
            }
            if (getExpectedOutput().isEmpty()) {
                CreateProblemScreen.this.showErrorMessage("Test case " + testCaseNumber + " expected output cannot be empty");
                outputArea.requestFocus();
                return false;
            }
            return true;
        }
    }

    private ProblemService problemService;
    private Problem currentProblem;
    private boolean isEditMode = false;

    public CreateProblemScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        problemService = new ProblemServiceImpl(new ProblemRepository(), new TestCaseRepository());
    }

    public CreateProblemScreen(Problem problem) {
        this();
        this.currentProblem = problem;
        populateFields(problem);
        this.isEditMode = true;
        editButton.setText("Cancel Edit");
    }

    private void initializeComponents() {
        // Create text fields with larger column counts
        problemIdField = new JTextField(20);
        problemNameField = new JTextField(20);
        problemStatementArea = new JTextArea(5, 40);
        problemStatementArea.setLineWrap(true);
        problemStatementArea.setWrapStyleWord(true);
        
        // Create combo box for difficulty
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficulties);
        
        // Create tags field with larger column count
        tagsField = new JTextField(20);
        
        // Create contest checkbox
        contestCheckBox = new JCheckBox();
        contestCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Initialize test cases panel and list
        testCasePanels = new ArrayList<>();
        testCasesPanel = new JPanel();
        testCasesPanel.setLayout(new BoxLayout(testCasesPanel, BoxLayout.Y_AXIS));
        testCasesScrollPane = new JScrollPane(testCasesPanel);
        testCasesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        testCasesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        testCasesScrollPane.setPreferredSize(new Dimension(600, 300));
        
        // Create add test case button
        addTestCaseButton = new JButton("Add Test Case");
        addTestCaseButton.setPreferredSize(new Dimension(120, 35));
        addTestCaseButton.addActionListener(e -> addTestCase());
        
        // Create buttons
        submitButton = new JButton("Submit");
        editButton = new JButton("Edit");
        backButton = new JButton("Back");

        // Set preferred sizes for buttons only
        Dimension buttonSize = new Dimension(100, 35);
        submitButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        // Add tooltips
        problemIdField.setToolTipText("Problem ID (auto-generated if empty)");
        problemNameField.setToolTipText("Enter the problem name");
        problemStatementArea.setToolTipText("Enter the problem description and requirements");
        difficultyComboBox.setToolTipText("Select problem difficulty");
        tagsField.setToolTipText("Enter comma-separated tags (e.g., array, sorting, dynamic-programming)");
        contestCheckBox.setToolTipText("Check if this problem will be used in an upcoming contest");

        // Initially disable problem ID field for new problems
        if (currentProblem == null) {
            problemIdField.setEditable(false);
            problemIdField.setText("Auto-generated");
        }
        // some data for testing
        problemNameField.setText("Add two numbers");
        problemStatementArea.setText("Given two integers, return their sum.");
        tagsField.setText("math, addition");
        // Add initial test case
        addTestCase();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title
        JLabel titleLabel = new JLabel(isEditMode ? "Edit Problem" : "Create New Problem");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        mainPanel.add(titleLabel, gbc);

        // Reset grid width for form fields
        gbc.gridwidth = 1;

        // Problem ID field
        addFormField(mainPanel, gbc, "Problem ID:", problemIdField, 1);

        // Problem Name field
        addFormField(mainPanel, gbc, "Problem Name:", problemNameField, 2);

        // Problem Statement area (special handling for text area)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 10, 10, 5);
        JLabel statementLabel = new JLabel("Problem Statement:");
        statementLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(statementLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(problemStatementArea);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        mainPanel.add(scrollPane, gbc);

        // Reset fill and weight for other components
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // Difficulty field
        addFormField(mainPanel, gbc, "Difficulty:", difficultyComboBox, 4);

        // Tags field
        addFormField(mainPanel, gbc, "Tags:", tagsField, 5);

        // Contest checkbox (aligned with other form fields)
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel contestLabel = new JLabel("Visible");
        contestLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(contestLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(contestCheckBox, gbc);

        // Reset for next component
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.gridwidth = 1;

        // Test Cases section
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(20, 10, 10, 5);
        JLabel testCasesLabel = new JLabel("Test Cases:");
        testCasesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(testCasesLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 10);
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        addButtonPanel.add(addTestCaseButton);
        mainPanel.add(addButtonPanel, gbc);

        // Test cases scroll pane
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 10, 10, 10);
        mainPanel.add(testCasesScrollPane, gbc);

        // Reset fill and weight for button panel
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(submitButton);
        buttonPanel.add(editButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
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
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, gbc);

        // Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
        
        // Reset for next component
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
    }

    private void setupEventHandlers() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEdit();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBack();
            }
        });

        // Allow Enter key to trigger submit
        getRootPane().setDefaultButton(submitButton);
    }

    private void setupFrame() {
        setTitle("OJX - Setting Problem");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 850);
        setLocationRelativeTo(null); // Center the window
        setResizable(true);
    }

    private void handleSubmit() {
        if (!validateFields()) {
            return;
        }

        try {
            // Get field values
            String problemName = problemNameField.getText().trim();
            String problemStatement = problemStatementArea.getText().trim();
            String difficulty = (String) difficultyComboBox.getSelectedItem();
            String tagsString = tagsField.getText().trim();
            boolean isForContest = !contestCheckBox.isSelected();

            if (isEditMode && currentProblem != null) {
                // For edit mode, we'll show a message that editing isn't implemented yet
                // since the service doesn't have an update method
                showInfoMessage("Edit functionality will be implemented in a future version.\nFor now, you can view the problem details.");
            } else {
                // Validate test cases
                if (!validateTestCases()) {
                    return;
                }

                // Create test cases list
                List<TestCase> testCases = new ArrayList<>();
                for (TestCaseInputPanel panel : testCasePanels) {
                    TestCase testCase = new TestCase.Builder()
                            .input(panel.getInput())
                            .output(panel.getExpectedOutput())
                            .build();
                    testCases.add(testCase);
                }

                // Create new problem using CreateProblemDTO
                CreateProblemDTO problemDTO = new CreateProblemDTO(
                    problemName, 
                    problemStatement, 
                    difficulty, 
                    tagsString, 
                    testCases,
                    isForContest
                );
                
                int result = problemService.create(problemDTO);
                if (result > 0) {
                    showSuccessMessage("Problem created successfully!");
                    clearFields();
                } else {
                    showErrorMessage("Failed to create problem. Please try again.");
                }
            }
        } catch (Exception e) {
            showErrorMessage("Error saving problem: " + e.getMessage());
        }
    }

    private void handleEdit() {
        if (isEditMode) {
            // Cancel edit mode
            if (currentProblem != null) {
                populateFields(currentProblem);
            }
            setFieldsEditable(false);
            editButton.setText("Edit");
            submitButton.setEnabled(false);
        } else {
            // Enter edit mode
            setFieldsEditable(true);
            editButton.setText("Cancel Edit");
            submitButton.setEnabled(true);
        }
        isEditMode = !isEditMode;
    }

    private void handleBack() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to go back? Any unsaved changes will be lost.",
            "Confirm Back",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            dispose(); // Close this window
            // You can add navigation logic here to return to previous screen
        }
    }

    private boolean validateFields() {
        String problemName = problemNameField.getText().trim();
        String problemStatement = problemStatementArea.getText().trim();
        String tags = tagsField.getText().trim();

        // Problem name validation
        if (problemName.isEmpty()) {
            showErrorMessage("Problem name is required");
            problemNameField.requestFocus();
            return false;
        }
        if (problemName.length() > 200) {
            showErrorMessage("Problem name must be 200 characters or less");
            problemNameField.requestFocus();
            return false;
        }

        // Problem statement validation
        if (problemStatement.isEmpty()) {
            showErrorMessage("Problem statement is required");
            problemStatementArea.requestFocus();
            return false;
        }
        if (problemStatement.length() > 5000) {
            showErrorMessage("Problem statement must be 5000 characters or less");
            problemStatementArea.requestFocus();
            return false;
        }

        // Tags validation (optional but validate format if provided)
        if (!tags.isEmpty() && tags.length() > 500) {
            showErrorMessage("Tags must be 500 characters or less");
            tagsField.requestFocus();
            return false;
        }

        return true;
    }

    private void clearFields() {
        problemIdField.setText("Auto-generated");
        problemNameField.setText("");
        problemStatementArea.setText("");
        difficultyComboBox.setSelectedIndex(0);
        tagsField.setText("");
        contestCheckBox.setSelected(false);
        clearTestCases();
        problemNameField.requestFocus();
    }

    private void populateFields(Problem problem) {
        if (problem != null) {
            problemIdField.setText(String.valueOf(problem.getProblemId()));
            problemNameField.setText(problem.getProblemName());
            problemStatementArea.setText(problem.getProblemStatement());
            difficultyComboBox.setSelectedItem(problem.getDifficulty());
            tagsField.setText(formatTagsToString(problem.getTags()));
            
            // Note: We can't populate test cases from Problem object 
            // since it doesn't contain test cases. This would need 
            // to be fetched separately from the service.
        }
    }

    private void setFieldsEditable(boolean editable) {
        problemNameField.setEditable(editable);
        problemStatementArea.setEditable(editable);
        difficultyComboBox.setEnabled(editable);
        tagsField.setEditable(editable);
        contestCheckBox.setEnabled(editable);
        setTestCasesEditable(editable);
    }

    // Test case management methods
    private void addTestCase() {
        int testCaseNumber = testCasePanels.size() + 1;
        TestCaseInputPanel newPanel = new TestCaseInputPanel(testCaseNumber);
        testCasePanels.add(newPanel);
        testCasesPanel.add(newPanel);
        
        // Add some spacing between test cases
        if (testCasePanels.size() > 1) {
            testCasesPanel.add(Box.createVerticalStrut(10));
        }
        
        testCasesPanel.revalidate();
        testCasesPanel.repaint();
        
        // Scroll to the bottom to show the newly added test case
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = testCasesScrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMinimum());
        });
    }
    
    private void removeTestCase(TestCaseInputPanel panel) {
        if (testCasePanels.size() <= 1) {
            showErrorMessage("At least one test case is required");
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove this test case?",
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            testCasePanels.remove(panel);
            testCasesPanel.remove(panel);
            
            // Renumber remaining test cases
            for (int i = 0; i < testCasePanels.size(); i++) {
                testCasePanels.get(i).testCaseNumber = i + 1;
                testCasePanels.get(i).setBorder(BorderFactory.createTitledBorder("Test Case " + (i + 1)));
            }
            
            testCasesPanel.revalidate();
            testCasesPanel.repaint();
        }
    }
    
    private boolean validateTestCases() {
        if (testCasePanels.isEmpty()) {
            showErrorMessage("At least one test case is required");
            return false;
        }
        
        for (TestCaseInputPanel panel : testCasePanels) {
            if (!panel.validateTestCase()) {
                return false;
            }
        }
        
        return true;
    }
    
    private void clearTestCases() {
        testCasePanels.clear();
        testCasesPanel.removeAll();
        addTestCase(); // Add one initial test case
    }
    
    private void populateTestCases(List<TestCase> testCases) {
        clearTestCases();
        
        if (testCases != null && !testCases.isEmpty()) {
            // Remove the default test case first
            testCasePanels.clear();
            testCasesPanel.removeAll();
            
            for (TestCase testCase : testCases) {
                int testCaseNumber = testCasePanels.size() + 1;
                TestCaseInputPanel panel = new TestCaseInputPanel(testCaseNumber);
                panel.setInput(testCase.getInput());
                panel.setExpectedOutput(testCase.getOutput());
                
                testCasePanels.add(panel);
                testCasesPanel.add(panel);
                
                if (testCasePanels.size() > 1) {
                    testCasesPanel.add(Box.createVerticalStrut(10));
                }
            }
            
            testCasesPanel.revalidate();
            testCasesPanel.repaint();
        }
    }
    
    private void setTestCasesEditable(boolean editable) {
        addTestCaseButton.setEnabled(editable);
        for (TestCaseInputPanel panel : testCasePanels) {
            panel.setEditable(editable);
        }
    }

    private List<String> parseTagsFromString(String tagsString) {
        if (tagsString.isEmpty()) {
            return Arrays.asList();
        }
        // Split by comma and trim whitespace
        String[] tagArray = tagsString.split(",");
        for (int i = 0; i < tagArray.length; i++) {
            tagArray[i] = tagArray[i].trim();
        }
        return Arrays.asList(tagArray);
    }

    private String formatTagsToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return String.join(", ", tags);
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

    // Getters for external access
    public Problem getCurrentProblem() {
        return currentProblem;
    }

    public String getProblemName() {
        return problemNameField.getText().trim();
    }

    public String getProblemStatement() {
        return problemStatementArea.getText().trim();
    }

    public String getDifficulty() {
        return (String) difficultyComboBox.getSelectedItem();
    }

    public List<String> getTags() {
        return parseTagsFromString(tagsField.getText().trim());
    }

    public List<TestCase> getTestCases() {
        List<TestCase> testCases = new ArrayList<>();
        for (TestCaseInputPanel panel : testCasePanels) {
            TestCase testCase = new TestCase.Builder()
                    .input(panel.getInput())
                    .output(panel.getExpectedOutput())
                    .build();
            testCases.add(testCase);
        }
        return testCases;
    }

    public boolean isForContest() {
        return contestCheckBox.isSelected();
    }

    // Main method for testing the ProblemScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new CreateProblemScreen().setVisible(true);
            }
        });
    }
}
