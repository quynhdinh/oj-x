package org.ojx.gui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import org.ojx.gui.contest.ContestScreen;
import org.ojx.model.Contest;

public class ContestManagementScreen extends ContestScreen {
    private JButton createContestButton;
    private JButton updateContestButton;
    // private JButton quitButton;
    
    public ContestManagementScreen() {
        super();
        setTitle("OJX - Contest Management"); // Override title
        this.titleLabel.setText("Contest Management"); // Set custom title label
        initializeManagementComponents();
        addManagementButtons();
    }
    
    private void initializeManagementComponents() {
        // Initialize management-specific buttons
        createContestButton = new JButton("Create Contest");
        updateContestButton = new JButton("Update Contest");
        // quitButton = new JButton("Quit");
        
        // Set button sizes
        createContestButton.setPreferredSize(new Dimension(140, 35));
        updateContestButton.setPreferredSize(new Dimension(140, 35));
        // quitButton.setPreferredSize(new Dimension(100, 35));
        
        // Add action listeners
        createContestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreateContest();
            }
        });
        
        updateContestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateContest();
            }
        });
        
        // quitButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         handleQuit();
        //     }
        // });
    }
    
    private void addManagementButtons() {
        // Find the existing button panel and add our buttons to it
        Container contentPane = getContentPane();
        addButtonsToPanel(contentPane);
    }
    
    private void addButtonsToPanel(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                // Check if this panel contains buttons (has FlowLayout and contains JButton)
                if (panel.getLayout() instanceof FlowLayout && containsButtons(panel)) {
                    // Add our management buttons to this panel
                    panel.add(createContestButton);
                    panel.add(updateContestButton);
                    // panel.add(quitButton);
                    panel.revalidate();
                    return;
                } else {
                    // Recursively search in nested panels
                    addButtonsToPanel(panel);
                }
            }
        }
    }
    
    private boolean containsButtons(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton) {
                return true;
            }
        }
        return false;
    }
    
    private void handleCreateContest() {
        // JOptionPane.showMessageDialog(this, 
        //     "Create Contest functionality will be implemented here.\n\n" +
        //     "This will include:\n" +
        //     "• Contest name and description\n" +
        //     "• Start time and duration\n" +
        //     "• Problem selection\n" +
        //     "• Point allocation", 
        //     "Create Contest", 
        //     JOptionPane.INFORMATION_MESSAGE);

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
                // refreshContestList();
            }
        });
    }
    
    private void handleUpdateContest() {
        int selectedRow = getContestTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a contest to update.", 
                "No Contest Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // System.out.println("updating contest");
        // JOptionPane.showMessageDialog(this, 
        //     "Update Contest functionality will be implemented here.\n\n" +
        //     "This will allow editing:\n" +
        //     "• Contest details\n" +
        //     "• Start time and duration\n" +
        //     "• Problem list\n" +
        //     "• Point allocation", 
        //     "Update Contest", 
        //     JOptionPane.INFORMATION_MESSAGE);

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
                        // refreshContestList();
                    }
                });
            } else {
                showErrorMessage("Contest not found in database.");
            }
        } else {
            showErrorMessage("Invalid contest selection.");
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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
