package org.ojx.gui.problem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.ojx.dto.ProblemResDTO;
import org.ojx.model.User;
import org.ojx.repository.ProblemRepository;
import org.ojx.repository.TestCaseRepository;
import org.ojx.repository.UserRepository;
import org.ojx.service.ProblemService;
import org.ojx.service.impl.ProblemServiceImpl;
import org.ojx.service.impl.UserServiceImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.RowFilter;

public class ProblemsetScreen extends JFrame {
    private JTable problemTable;
    private DefaultTableModel tableModel;
    private JTextField filterTextField;
    private JComboBox<String> filterComboBox;
    private JButton refreshButton;
    private JButton createProblemButton;
    private JButton viewProblemButton;
    private JButton backButton;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private User user;
    private ProblemService problemService;

    // Table column names
    private final String[] columnNames = { "Problem ID", "Problem Name", "Tags", "Difficulty" };

    public ProblemsetScreen(int userId) {
        user = (new UserServiceImpl(new UserRepository())).getById(userId).get();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        problemService = new ProblemServiceImpl(new ProblemRepository(), new TestCaseRepository());
        loadProblems();
    }

    private void initializeComponents() {
        // Create table model and table
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        problemTable = new JTable(tableModel);
        problemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        problemTable.setRowHeight(25);
        problemTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        problemTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Problem ID
        problemTable.getColumnModel().getColumn(1).setPreferredWidth(300); // Problem Name
        problemTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Tags
        problemTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Difficulty

        // Create row sorter for filtering
        rowSorter = new TableRowSorter<>(tableModel);
        problemTable.setRowSorter(rowSorter);

        // Create filter components
        filterTextField = new JTextField(25);
        filterTextField.setPreferredSize(new Dimension(250, 30));

        String[] filterOptions = { "All", "Problem ID", "Problem Name", "Tags", "Difficulty" };
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setPreferredSize(new Dimension(120, 30));

        // Create buttons
        refreshButton = new JButton("Refresh");
        createProblemButton = new JButton("Create Problem");
        viewProblemButton = new JButton("View Problem");
        backButton = new JButton("Back");

        // Set button sizes
        Dimension buttonSize = new Dimension(120, 35);
        refreshButton.setPreferredSize(buttonSize);
        createProblemButton.setPreferredSize(buttonSize);
        viewProblemButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(new Dimension(80, 35));

        // Add tooltips
        filterTextField.setToolTipText("Enter text to filter problems");
        filterComboBox.setToolTipText("Select filter criteria");
        refreshButton.setToolTipText("Refresh the problem list");
        createProblemButton.setToolTipText("Create a new problem");
        viewProblemButton.setToolTipText("View selected problem details");

        // Initially disable view button
        viewProblemButton.setEnabled(false);
        createProblemButton.setVisible(user.getUserType().equals("problem_setter"));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Problem Set");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));

        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        filterPanel.add(filterLabel);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(filterComboBox);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(filterTextField);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(refreshButton);

        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(problemTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 400));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Problems"));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(createProblemButton);
        buttonPanel.add(viewProblemButton);
        buttonPanel.add(backButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        // Filter text field listener
        filterTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });

        // Filter combo box listener
        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });

        // Table selection listener
        problemTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewProblemButton.setEnabled(problemTable.getSelectedRow() != -1);
            }
        });

        // Button listeners
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProblems();
            }
        });

        createProblemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreateProblem();
            }
        });

        viewProblemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewProblem();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBack();
            }
        });

        // Double-click on table row to view problem
        problemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && problemTable.getSelectedRow() != -1) {
                    handleViewProblem();
                }
            }
        });
    }

    private void setupFrame() {
        setTitle("OJX - Problem Set");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void loadProblems() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);

            String selectedFilter = filterComboBox.getSelectedItem().toString();

            List<ProblemResDTO> problems = new ArrayList<>();
            if (selectedFilter.equals("All")) {
                problems = problemService.getAllProblems();
            } else if (selectedFilter.equals("Problem ID")) {
                var problemOptional = problemService.getProblemById(Integer.parseInt(filterTextField.getText().trim()));
                problems = problemOptional.isPresent() ? List.of(new ProblemResDTO(problemOptional.get()))
                        : new ArrayList<>();
            } else if (selectedFilter.equals("Problem Name")) {
                problems = problemService.getProblemsByName(filterTextField.getText().trim());
            } else if (selectedFilter.equals("Tags")) {
                System.out.println("filtering by tags: " + filterTextField.getText().trim());
                problems = problemService.getProblemsByTags(filterTextField.getText().trim());
            } else if (selectedFilter.equals("Difficulty")) {
                problems = problemService.getProblemsByDifficulty(filterTextField.getText().trim());
            } else {
                problems = new ArrayList<>(); // Default to empty list if no valid filter
            }
            // Add problems to table
            for (ProblemResDTO problem : problems) {
                Object[] rowData = {
                        problem.problemId(),
                        problem.problemName(),
                        problem.tags() != null ? problem.tags() : "",
                        problem.difficulty() != null ? problem.difficulty() : ""
                };
                tableModel.addRow(rowData);
            }

            // Update status
            setTitle("OJX - Problem Set (" + problems.size() + " problems)");

        } catch (Exception e) {
            showErrorMessage("Error loading problems: " + e.getMessage());
        }
    }

    private void applyFilter() {
        String filterText = filterTextField.getText().trim();
        String filterType = (String) filterComboBox.getSelectedItem();
        if (filterText.isEmpty() || "All".equals(filterType)) {
            rowSorter.setRowFilter(null);
        } else {
            try {
                RowFilter<DefaultTableModel, Object> filter = null;

                switch (filterType) {
                    case "Problem ID":
                        // Filter by Problem ID (exact match or starts with)
                        filter = RowFilter.regexFilter("(?i)^" + filterText, 0);
                        break;
                    case "Problem Name":
                        // Filter by Problem Name (contains, case insensitive)
                        filter = RowFilter.regexFilter("(?i)" + filterText, 1);
                        break;
                    case "Tags":
                        // Filter by Tags (contains, case insensitive)
                        filter = RowFilter.regexFilter("(?i)" + filterText, 2);
                        break;
                    case "Difficulty":
                        // Filter by Difficulty (contains, case insensitive)
                        filter = RowFilter.regexFilter("(?i)" + filterText, 3);
                        break;
                    default:
                        // Search all columns
                        filter = RowFilter.regexFilter("(?i)" + filterText);
                        break;
                }
                rowSorter.setRowFilter(filter);
            } catch (java.util.regex.PatternSyntaxException e) {
                // If regex is invalid, show all rows
                rowSorter.setRowFilter(null);
            }
        }
    }

    private void handleCreateProblem() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CreateProblemScreen createScreen = new CreateProblemScreen();
                createScreen.setVisible(true);
                // You might want to refresh the list when the create screen is closed
            }
        });
    }

    private void handleViewProblem() {
        int selectedRow = problemTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Please select a problem to view.");
            return;
        }
        // Convert view row index to model row index (important for filtered tables)
        int modelRow = problemTable.convertRowIndexToModel(selectedRow);

        // Get problem details
        Object problemId = tableModel.getValueAt(modelRow, 0);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ViewProblemScreen viewScreen = new ViewProblemScreen((Integer) problemId, user.getUserId());
                viewScreen.setVisible(true);
            }
        });
    }

    private void handleBack() {
        dispose(); // Close this window
        // You can add navigation logic here to return to the main screen
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Getters for external access
    public JTable getProblemTable() {
        return problemTable;
    }

    public int getSelectedProblemId() {
        int selectedRow = problemTable.getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }
        int modelRow = problemTable.convertRowIndexToModel(selectedRow);
        return (Integer) tableModel.getValueAt(modelRow, 0);
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
                new ProblemsetScreen(1).setVisible(true);
            }
        });
    }
}
