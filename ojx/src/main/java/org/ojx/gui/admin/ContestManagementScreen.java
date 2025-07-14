package org.ojx.gui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.ojx.model.Contest;
import org.ojx.service.ContestService;
import org.ojx.service.impl.ContestServiceImpl;
import org.ojx.repository.ContestRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ContestManagementScreen extends JFrame {
    private JTable contestTable;
    private DefaultTableModel tableModel;
    private JButton createContestButton;
    private JButton updateContestButton;
    private JButton quitButton;
    private JLabel totalContestsLabel;
    
    private ContestService contestService;
    
    public ContestManagementScreen() {
        contestService = new ContestServiceImpl(new ContestRepository());
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadContests();
    }
    
    private void initializeComponents() {
        // Create table model with columns
        String[] columnNames = {
            "Contest ID", "Contest Name", "Duration (min)", "Start Time", "Status", "Problems", "Points"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        contestTable = new JTable(tableModel);
        contestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contestTable.setRowHeight(25);
        contestTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        contestTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Contest ID
        contestTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Contest Name
        contestTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Duration
        contestTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Start Time
        contestTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Status
        contestTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Problems
        contestTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Points
        
        // Create buttons
        createContestButton = new JButton("Create Contest");
        updateContestButton = new JButton("Update Contest");
        quitButton = new JButton("Quit");
        
        createContestButton.setPreferredSize(new Dimension(140, 35));
        updateContestButton.setPreferredSize(new Dimension(140, 35));
        quitButton.setPreferredSize(new Dimension(100, 35));
        
        // Create total contests label
        totalContestsLabel = new JLabel("Total: 0 contests");
        totalContestsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add tooltips
        contestTable.setToolTipText("Click on a contest to select, then use 'Update Contest' button or double-click to edit");
        createContestButton.setToolTipText("Create a new contest");
        updateContestButton.setToolTipText("Update the selected contest");
        quitButton.setToolTipText("Close this screen");
        
        // Initially disable update button until a contest is selected
        updateContestButton.setEnabled(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Contest Management");
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
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("System Contests"));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(createContestButton);
        buttonPanel.add(updateContestButton);
        buttonPanel.add(quitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add to frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Create contest button
        createContestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreateContest();
            }
        });
        
        // Update contest button
        updateContestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateContest();
            }
        });
        
        // Quit button
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });
        
        // Table selection listener
        contestTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateContestButton.setEnabled(contestTable.getSelectedRow() != -1);
            }
        });
        
        // Double-click on table row to update contest
        contestTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && contestTable.getSelectedRow() != -1) {
                    handleUpdateContest();
                }
            }
        });
        
        // Allow Enter key to update selected contest
        contestTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "updateContest");
        contestTable.getActionMap().put("updateContest", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contestTable.getSelectedRow() != -1) {
                    handleUpdateContest();
                }
            }
        });
    }
    
    private void setupFrame() {
        setTitle("OJX - Contest Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void loadContests() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load contests from service
            List<Contest> contests = contestService.getAll();
            
            // Sort contests by start time (latest first)
            contests.sort((c1, c2) -> Long.compare(c2.getStartedAt(), c1.getStartedAt()));
            
            // Populate table using Stream API
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            long currentTime = System.currentTimeMillis();
            
            contests.stream()
                .map(contest -> {
                    // Format start time
                    String startTime = dateFormat.format(new Date(contest.getStartedAt() * 1000));
                    
                    // Determine status
                    long contestEndTime = contest.getStartedAt() * 1000 + (contest.getLength() * 60 * 1000L);
                    String status;
                    if (currentTime < contest.getStartedAt()) {
                        status = "Upcoming";
                    } else if (currentTime >= contest.getStartedAt() && currentTime < contestEndTime) {
                        status = "Active";
                    } else {
                        status = "Ended";
                    }
                    
                    // Count problems using Stream API  
                    String problemIds = contest.getProblemIds();
                    int problemCount = (problemIds != null && !problemIds.trim().isEmpty()) 
                        ? problemIds.split(",").length : 0;
                    
                    return new Object[] {
                        contest.getContestId(),
                        contest.getContestName(),
                        contest.getLength(),
                        startTime,
                        status,
                        problemCount + " problems",
                        contest.getPoints() != null ? contest.getPoints() : "Not set"
                    };
                })
                .forEach(tableModel::addRow);
            
            // Update total count
            totalContestsLabel.setText("Total: " + contests.size() + " contests");
            
        } catch (Exception e) {
            showErrorMessage("Error loading contests: " + e.getMessage());
        }
    }
    
    private void handleCreateContest() {
        // Create a new empty contest for creation
        Contest newContest = new Contest(
            0, // ID will be auto-generated
            "", // Empty name to be filled
            120, // Default 2 hours
            System.currentTimeMillis() / 1000L, // Current time as default
            "", // Empty problem IDs
            "" // Empty points
        );
        
        // Show UpdateCreateContestScreen with create mode
        UpdateCreateContestScreen createScreen = new UpdateCreateContestScreen(newContest);
        createScreen.setTitle("OJX - Create New Contest");
        createScreen.setVisible(true);
        
        // Refresh the list when the create screen is closed
        createScreen.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                refreshContestList();
            }
        });
    }
    
    private void handleUpdateContest() {
        int selectedRow = contestTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Please select a contest to update.");
            return;
        }
        
        // Get contest ID from the table
        Object contestIdObj = tableModel.getValueAt(selectedRow, 0);
        
        if (contestIdObj instanceof Integer) {
            int contestId = (Integer) contestIdObj;
            
            // Get the full contest object from the service
            Optional<Contest> contestOpt = contestService.getContestById(contestId);
            
            if (contestOpt.isPresent()) {
                Contest contest = contestOpt.get();
                
                // Show UpdateCreateContestScreen with update mode
                UpdateCreateContestScreen updateScreen = new UpdateCreateContestScreen(contest);
                updateScreen.setTitle("OJX - Update Contest: " + contest.getContestName());
                updateScreen.setVisible(true);
                
                // Refresh the list when the update screen is closed
                updateScreen.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        refreshContestList();
                    }
                });
            } else {
                showErrorMessage("Contest not found in database.");
            }
        } else {
            showErrorMessage("Invalid contest selection.");
        }
    }
    
    private void handleQuit() {
        dispose(); // Close this window
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Getters for external access
    public JTable getContestTable() {
        return contestTable;
    }
    
    public void refreshContestList() {
        loadContests();
    }
    
    // Main method for testing the ContestManagementScreen independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                new ContestManagementScreen().setVisible(true);
            }
        });
    }
}
