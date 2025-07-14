package org.ojx.gui.admin;

import org.ojx.gui.problem.ProblemsetScreen;
import org.ojx.gui.problem.ViewProblemScreen;
import org.ojx.repository.ProblemRepository;
import org.ojx.repository.TestCaseRepository;
import org.ojx.service.ProblemService;
import org.ojx.service.impl.ProblemServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProblemManagementScreen extends ProblemsetScreen {
    
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private ProblemService problemService;
    private int userId;

    public ProblemManagementScreen(int userId) {
        super(userId);
        this.userId = userId;
        problemService = new ProblemServiceImpl(new ProblemRepository(), new TestCaseRepository());
        initializeAdminButtons();
        addAdminButtonsToLayout();
        updateTitle();
        setTitle("OJX - Problem Management");
    }
    
    private void initializeAdminButtons() {
        // Create admin buttons
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        
        // Set button sizes
        addButton.setPreferredSize(new Dimension(120, 35));
        deleteButton.setPreferredSize(new Dimension(120, 35));
        updateButton.setPreferredSize(new Dimension(120, 35));
        
        // Set button colors and styling
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);
        
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        
        updateButton.setBackground(new Color(255, 193, 7));
        updateButton.setForeground(Color.BLACK);
        updateButton.setFocusPainted(false);
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);
        
        // Add tooltips
        addButton.setToolTipText("Create a new problem");
        deleteButton.setToolTipText("Delete the selected problem");
        updateButton.setToolTipText("Update the selected problem");
        
        // Add event handlers
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddProblem();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteProblem();
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateProblem();
            }
        });
        
        // Initially disable delete and update buttons until a problem is selected
        deleteButton.setEnabled(false);
        updateButton.setEnabled(false);
    }
    
    private void addAdminButtonsToLayout() {
        // Find the existing button panel and add our buttons
        Container contentPane = getContentPane();
        JPanel buttonPanel = findButtonPanel(contentPane);
        
        if (buttonPanel != null) {
            // Add our admin buttons before the existing buttons
            Component[] existingComponents = buttonPanel.getComponents();
            buttonPanel.removeAll();
            
            // Add our admin buttons first
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
            
            // Add a separator
            buttonPanel.add(Box.createHorizontalStrut(20));
            
            // Add back the existing buttons
            for (Component comp : existingComponents) {
                buttonPanel.add(comp);
            }
            
            buttonPanel.revalidate();
            buttonPanel.repaint();
            
            // Add table selection listener through the public table reference
            JTable table = findProblemTable(contentPane);
            if (table != null) {
                table.getSelectionModel().addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        boolean hasSelection = table.getSelectedRow() != -1;
                        deleteButton.setEnabled(hasSelection);
                        updateButton.setEnabled(hasSelection);
                    }
                });
            }
        }
    }
    
    private void updateTitle() {
        // Find and update the title label
        Container contentPane = getContentPane();
        JLabel titleLabel = findTitleLabel(contentPane);
        if (titleLabel != null) {
            titleLabel.setText("Problem Management");
        }
    }
    
    private JLabel findTitleLabel(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getFont() != null && label.getFont().getSize() >= 20) {
                    return label;
                }
            }
            if (comp instanceof Container) {
                JLabel found = findTitleLabel((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    private JPanel findButtonPanel(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                // Check if this panel contains buttons (likely the button panel)
                for (Component child : panel.getComponents()) {
                    if (child instanceof JButton) {
                        return panel;
                    }
                    if (child instanceof Container) {
                        JPanel found = findButtonPanel((Container) child);
                        if (found != null) return found;
                    }
                }
            }
        }
        return null;
    }
    
    private JTable findProblemTable(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    return (JTable) view;
                }
            }
            if (comp instanceof Container) {
                JTable found = findProblemTable((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    private void handleAddProblem() {
        showInfoMessage("Add Problem", 
            "Add Problem functionality will include:\n\n" +
            "• Create new problem statement\n" +
            "• Set problem difficulty and tags\n" +
            "• Add test cases and solutions\n" +
            "• Configure time and memory limits\n" +
            "• Set problem visibility\n\n" +
            "This feature will be implemented in the next version.");
    }
    
    private void handleDeleteProblem() {
        JTable table = findProblemTable(getContentPane());
        if (table == null) {
            showErrorMessage("Problem table not found.");
            return;
        }
        
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Please select a problem to delete.");
            return;
        }
        
        // Get problem details from the table
        Object problemIdObj = table.getValueAt(selectedRow, 0);
        String problemName = (String) table.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete problem '" + problemName + "'?\n\n" +
            "This action cannot be undone and will:\n" +
            "• Remove the problem permanently\n" +
            "• Delete all associated test cases\n" +
            "• Remove all submissions for this problem\n\n" +
            "Do you want to continue?",
            "Confirm Problem Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            problemService.delete((Integer) problemIdObj);
            // showInfoMessage("Delete Problem", 
            //     "Problem deletion functionality for:\n" +
            //     "Problem ID: " + problemIdObj + "\n" +
            //     "Problem Name: " + problemName + "\n\n" +
            //     "Delete features will include:\n" +
            //     "• Remove problem from database\n" +
            //     "• Clean up test cases\n" +
            //     "• Archive submissions\n" +
            //     "• Update statistics\n\n" +
            //     "This feature will be implemented in the next version.");
        }
    }
    
    private void handleUpdateProblem() {
        JTable table = findProblemTable(getContentPane());
        if (table == null) {
            showErrorMessage("Problem table not found.");
            return;
        }
        
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Please select a problem to update.");
            return;
        }
        
        // Get problem details from the table
        Object problemIdObj = table.getValueAt(selectedRow, 0);
        // String problemName = (String) table.getValueAt(selectedRow, 1);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ViewProblemScreen viewScreen = new ViewProblemScreen((Integer) problemIdObj, userId);
                viewScreen.setVisible(true);
            }
        });
        // showInfoMessage("Update Problem", 
        //     "Update Problem functionality for:\n" +
        //     "Problem ID: " + problemIdObj + "\n" +
        //     "Problem Name: " + problemName + "\n\n" +
        //     "Update features will include:\n" +
        //     "• Modify problem statement\n" +
        //     "• Update difficulty and tags\n" +
        //     "• Edit test cases\n" +
        //     "• Adjust time/memory limits\n" +
        //     "• Change visibility settings\n\n" +
        //     "This feature will be implemented in the next version.");
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showInfoMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}