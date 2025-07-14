package org.ojx.gui.contest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.ojx.model.Contest;
import org.ojx.repository.ContestRepository;
import org.ojx.service.ContestService;
import org.ojx.service.impl.ContestServiceImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ContestScreen extends JFrame {
    private JTable contestTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JLabel totalContestsLabel;

    private ContestService contestService;
    private List<Contest> contests; // Store loaded contests

    // Table column names
    private final String[] columnNames = {"Contest ID", "Contest Name", "Duration (minutes)", "Problems Count", "Start Time", "Status"};

    public ContestScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        contestService = new ContestServiceImpl(new ContestRepository());
        loadContests();
    }

    private void initializeComponents() {
        // Create table model and table
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        contestTable = new JTable(tableModel);
        contestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contestTable.setRowHeight(25);
        contestTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        contestTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Contest ID
        contestTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Contest Name
        contestTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Duration
        contestTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Problems Count
        contestTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Start Time
        contestTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Status
        
        // Create back button
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 35));
        
        // Create total contests label
        totalContestsLabel = new JLabel("Total: 0 contests");
        totalContestsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add tooltips
        contestTable.setToolTipText("Click on a contest to view details");
        backButton.setToolTipText("Go back to previous screen");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Contest List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Info panel (total contests)
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(totalContestsLabel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));

        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(contestTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 400));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Contests (Ordered by Start Time - Latest First)"));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(backButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Combine title and info panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBack();
            }
        });

        // Double-click and single-click on table row to view contest details
        contestTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1 && contestTable.getSelectedRow() != -1) {
                    handleViewContest();
                }
            }
        });

        // Allow Enter key to view selected contest
        contestTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "viewContest");
        contestTable.getActionMap().put("viewContest", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contestTable.getSelectedRow() != -1) {
                    handleViewContest();
                }
            }
        });
    }

    private void setupFrame() {
        setTitle("OJX - Contest List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set minimum size to prevent UI elements from being too cramped
        setMinimumSize(new Dimension(800, 500));
    }

    private void loadContests() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load contests from service
            contests = Optional.ofNullable(contestService.getAll()).orElseGet(List::of);
            
            if (!contests.isEmpty()) {
                // Note: Since Contest model doesn't have start_time field, 
                // we'll create mock data for demonstration
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                
                // Using Stream API to populate contest table
                contests.stream().map(contest -> {
                    // Mock start time (in real implementation, this would come from contest object)
                    Date start_Date = new Date(contest.getStartedAt() * 1000L);
                    String formattedStartTime = dateFormat.format(start_Date);

                    // Determine status based on mock data
                    String status = start_Date.before(new Date()) ? "Ongoing" :
                                    (start_Date.after(new Date()) ? "Upcoming" : "Finished");
                    
                    return new Object[] {
                        getContestId(contest),
                        getContestName(contest),
                        getDuration(contest) + " min",
                        getProblemCount(contest),
                        formattedStartTime,
                        status
                    };
                }).forEach(tableModel::addRow);
            } else {
                // Add a message row if no contests found
                Object[] noDataRow = {"No contests", "available", "", "", "", ""};
                tableModel.addRow(noDataRow);
            }
            
            // Update total contests label
            int totalContests = contests != null ? contests.size() : 0;
            totalContestsLabel.setText("Total: " + totalContests + " contests");
            
        } catch (Exception e) {
            showErrorMessage("Error loading contests: " + e.getMessage());
            // Add error row
            Object[] errorRow = {"Error loading", "contests", "", "", "", ""};
            tableModel.addRow(errorRow);
        }
    }
    
    // Helper methods to safely access Contest properties
    private int getContestId(Contest contest) {
        try {
            return contest.getContestId();
        } catch (Exception e) {
            return 0; // Default value
        }
    }
    
    private int getDuration(Contest contest) {
        try {
            return contest.getLength();
        } catch (Exception e) {
            return 60; // Default duration
        }
    }
    
    private int getProblemCount(Contest contest) {
        try {
            return contest.getProblemIds() != null ? contest.getProblemIds().split(",").length : 0;
        } catch (Exception e) {
            return 0; // Default count
        }
    }

    private String getContestName(Contest contest) {
        try {
            return contest.getContestName() != null ? contest.getContestName() : "Unnamed Contest";
        } catch (Exception e) {
            return "Unnamed Contest"; // Default name
        }
    }
    
    private void handleViewContest() {
        int selectedRow = contestTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Please select a contest to view.");
            return;
        }
        
        // Validate that we have contests loaded and the row is valid
        if (contests == null || selectedRow >= contests.size()) {
            showErrorMessage("Invalid contest selection.");
            return;
        }
        
        try {
            // Get the contest object from the stored list
            Contest selectedContest = contests.get(selectedRow);
            
            // Open the ContestDetailScreen as a modal dialog
            ContestDetailScreen.showContestDetail(this, selectedContest);
            
        } catch (Exception e) {
            showErrorMessage("Error opening contest details: " + e.getMessage());
        }
    }

    private void handleBack() {
        dispose(); // Close this window
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Getters for external access
    public JTable getContestTable() {
        return contestTable;
    }

    public int getTotalContests() {
        return tableModel.getRowCount();
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
                
                new ContestScreen().setVisible(true);
            }
        });
    }
}