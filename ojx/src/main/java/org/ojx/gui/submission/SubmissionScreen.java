package org.ojx.gui.submission;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.ojx.dto.SubmissionResDTO;
import org.ojx.repository.SubmissionRepository;
import org.ojx.service.SubmissionService;
import org.ojx.service.impl.SubmissionServiceImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SubmissionScreen extends JFrame {
    private JTable submissionTable;
    private DefaultTableModel tableModel;
    private JButton viewSubmissionButton;
    private JButton quitButton;
    private JButton previousPageButton;
    private JButton nextPageButton;
    private JLabel pageInfoLabel;
    private JLabel totalSubmissionsLabel;
    private SubmissionService submissionService;
    private int user_id;
    // Pagination
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;
    private int totalSubmissions = 0;
    private int totalPages = 0;

    // Table column names
    private final String[] columnNames = {"Submission ID", "Problem ID", "Problem Name", "Programming Language", "Judge Status", "Submission Time"};

    public SubmissionScreen(int user_id) {
        this.user_id = user_id;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        submissionService = new SubmissionServiceImpl(new SubmissionRepository());
        loadSubmissions();
    }

    private void initializeComponents() {
        // Create table model and table
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        submissionTable = new JTable(tableModel);
        submissionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        submissionTable.setRowHeight(25);
        submissionTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        submissionTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Submission ID
        submissionTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Problem ID
        submissionTable.getColumnModel().getColumn(2).setPreferredWidth(250); // Problem Name
        submissionTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Programming Language
        submissionTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Judge Status
        submissionTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Submission Time

        // Create buttons
        viewSubmissionButton = new JButton("View Submission");
        quitButton = new JButton("Quit");
        previousPageButton = new JButton("Previous");
        nextPageButton = new JButton("Next");

        // Set button sizes
        Dimension buttonSize = new Dimension(130, 35);
        viewSubmissionButton.setPreferredSize(buttonSize);
        quitButton.setPreferredSize(new Dimension(80, 35));
        
        Dimension navButtonSize = new Dimension(100, 30);
        previousPageButton.setPreferredSize(navButtonSize);
        nextPageButton.setPreferredSize(navButtonSize);

        // Create labels
        pageInfoLabel = new JLabel("Page 1 of 1");
        pageInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        pageInfoLabel.setHorizontalAlignment(JLabel.CENTER);
        
        totalSubmissionsLabel = new JLabel("Total: 0 submissions");
        totalSubmissionsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Add tooltips
        viewSubmissionButton.setToolTipText("View details of selected submission");
        quitButton.setToolTipText("Close submission screen");
        previousPageButton.setToolTipText("Go to previous page");
        nextPageButton.setToolTipText("Go to next page");
        
        // Initially disable view button and navigation buttons
        viewSubmissionButton.setEnabled(false);
        previousPageButton.setEnabled(false);
        nextPageButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Submission History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Info panel (total submissions)
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(totalSubmissionsLabel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));

        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(submissionTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 400));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Submissions"));

        // Pagination panel
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.add(previousPageButton);
        paginationPanel.add(Box.createHorizontalStrut(20));
        paginationPanel.add(pageInfoLabel);
        paginationPanel.add(Box.createHorizontalStrut(20));
        paginationPanel.add(nextPageButton);
        paginationPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(viewSubmissionButton);
        buttonPanel.add(quitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Combine title and info panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(paginationPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        // Table selection listener
        submissionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewSubmissionButton.setEnabled(submissionTable.getSelectedRow() != -1);
            }
        });

        // Button listeners
        viewSubmissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewSubmission();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });

        previousPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePreviousPage();
            }
        });

        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNextPage();
            }
        });

        // Double-click on table row to view submission
        submissionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && submissionTable.getSelectedRow() != -1) {
                    handleViewSubmission();
                }
            }
        });
    }

    private void setupFrame() {
        setTitle("OJX - Submission History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void loadSubmissions() {
        try {
            tableModel.setRowCount(0);
            
            // Load submissions from service (for now, use getAllSubmissions since pagination methods may not be implemented)
            totalSubmissions = submissionService.count();
            totalPages = (int) Math.ceil((double) totalSubmissions / PAGE_SIZE);
            List<SubmissionResDTO> allSubmissions = submissionService.getAllSubmissions(currentPage, PAGE_SIZE);
            
            // Add submissions to table for current page
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            for(int i = 0; i < allSubmissions.size(); i++) {
                SubmissionResDTO submission = allSubmissions.get(i);
                Object[] rowData = {
                    submission.getSubmissionId(),
                    submission.getProblemId(),
                    submission.getProblemName() != null ? submission.getProblemName() : "Problem " + submission.getProblemId(),
                    submission.getLanguage(),
                    getFormattedJudgeStatus(submission.getJudgeStatus()),
                    dateFormat.format(new Date(submission.getCreatedAt()))
                };
                tableModel.addRow(rowData);
            }
            
            // Update UI elements
            updatePaginationControls();
            updateTitle();
            
        } catch (Exception e) {
            showErrorMessage("Error loading submissions: " + e.getMessage());
        }
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

    private void updatePaginationControls() {
        // Update page info label
        if (totalPages > 0) {
            pageInfoLabel.setText("Page " + currentPage + " of " + totalPages);
        } else {
            pageInfoLabel.setText("Page 0 of 0");
        }
        
        // Update navigation buttons
        previousPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < totalPages);
        
        // Update total submissions label
        totalSubmissionsLabel.setText("Total: " + totalSubmissions + " submissions");
    }

    private void updateTitle() {
        int startItem = totalSubmissions > 0 ? (currentPage - 1) * PAGE_SIZE + 1 : 0;
        int endItem = Math.min(currentPage * PAGE_SIZE, totalSubmissions);
        setTitle("OJX - Submission History (" + startItem + "-" + endItem + " of " + totalSubmissions + ")");
    }

    private void handlePreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            loadSubmissions();
        }
    }

    private void handleNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            loadSubmissions();
        }
    }

    private void handleViewSubmission() {
        int selectedRow = submissionTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Please select a submission to view.");
            return;
        }
        
        Integer submissionId = (Integer) tableModel.getValueAt(selectedRow, 0);
        // Create modal dialog with this frame as parent
        ViewSubmissionScreen viewScreen = new ViewSubmissionScreen(submissionId);
        viewScreen.setVisible(true);
    }

    private void handleQuit() {
        dispose(); // Close this window
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Getters for external access
    public JTable getSubmissionTable() {
        return submissionTable;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalSubmissions() {
        return totalSubmissions;
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
                
                new SubmissionScreen(1).setVisible(true);
            }
        });
    }
}
