// view/TaskView.java
package view;

import model.*;
import controller.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.*;
import java.util.*;
import java.util.List;

/**
 * Main task management interface
 */
public class TaskView extends JFrame {
    private JTable taskTable;
    private TaskTableModel tableModel;
    private JComboBox<String> filterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton addTaskButton;
    private JButton editTaskButton;
    private JButton deleteTaskButton;
    private JButton exportButton;
    private JButton importButton;
    private JButton logoutButton;
    private JButton themeButton;

    private TaskController controller;

    // In TaskView.java
    private TaskManager taskManager;

    public TaskView(TaskManager taskManager) throws Exception {
        this.taskManager = taskManager;
        controller = new TaskController(this, taskManager);
        initializeUI(); // This initializes tableModel
        controller.loadTasks(); // Load tasks after UI is ready
    }

    /**
     * Initializes the user interface
     */
    private void initializeUI() {
        setTitle("Todo App - Tasks");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");

        // File menu items
        JMenuItem exportItem = new JMenuItem("Export Tasks");
        exportItem.addActionListener(e -> controller.handleExport());
        fileMenu.add(exportItem);

        JMenuItem importItem = new JMenuItem("Import Tasks");
        importItem.addActionListener(e -> controller.handleImport());
        fileMenu.add(importItem);

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> controller.handleLogout());
        fileMenu.add(logoutItem);

        // Edit menu items
        JMenuItem addTaskItem = new JMenuItem("Add Task");
        addTaskItem.addActionListener(e -> controller.handleAddTask());
        editMenu.add(addTaskItem);

        // View menu items
        JMenuItem themeItem = new JMenuItem("Toggle Theme");
        themeItem.addActionListener(e -> controller.handleToggleTheme());
        viewMenu.add(themeItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with filters and buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // Filter section
        JLabel filterLabel = new JLabel("Filter:");
        filterComboBox = new JComboBox<>(new String[]{"All", "Complete", "Incomplete", "High Priority"});
        filterComboBox.addActionListener(e -> controller.handleFilterChange());
        topPanel.add(filterLabel);
        topPanel.add(filterComboBox);

        // Sort section
        JLabel sortLabel = new JLabel("Sort:");
        sortComboBox = new JComboBox<>(new String[]{"Default", "Due Date", "Priority"});
        sortComboBox.addActionListener(e -> controller.handleSortChange());
        topPanel.add(sortLabel);
        topPanel.add(sortComboBox);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new TaskTableModel();
        taskTable = new JTable(tableModel);

        // Customize table appearance
        taskTable.setFillsViewportHeight(true);
        taskTable.getTableHeader().setReorderingAllowed(false);
        taskTable.setRowHeight(25);

        // Make status column show checkboxes
        TableColumn statusColumn = taskTable.getColumnModel().getColumn(0); // Status column
        statusColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        statusColumn.setCellRenderer(renderer);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> controller.handleAddTask());
        bottomPanel.add(addTaskButton);

        editTaskButton = new JButton("Edit Task");
        editTaskButton.addActionListener(e -> controller.handleEditTask());
        bottomPanel.add(editTaskButton);

        deleteTaskButton = new JButton("Delete Task");
        deleteTaskButton.addActionListener(e -> controller.handleDeleteTask());
        bottomPanel.add(deleteTaskButton);

        exportButton = new JButton("Export");
        exportButton.addActionListener(e -> controller.handleExport());
        bottomPanel.add(exportButton);

        importButton = new JButton("Import");
        importButton.addActionListener(e -> controller.handleImport());
        bottomPanel.add(importButton);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        bottomPanel.add(logoutButton);

        themeButton = new JButton("Toggle Theme");
        themeButton.addActionListener(e -> controller.handleToggleTheme());
        bottomPanel.add(themeButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Sets the task data in the table
     */
    public void setTasks(List<Task> tasks) {
        tableModel.setTasks(tasks);
    }

    /**
     * Gets the currently selected task
     */
    public Task getSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getTaskAt(selectedRow);
        }
        return null;
    }

    /**
     * Shows an error message
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success message
     */
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Export Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Shows a confirmation dialog
     */
    public int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION);
    }

    /**
     * Gets the current filter selection
     */
    public String getFilterSelection() {
        return (String) filterComboBox.getSelectedItem();
    }

    /**
     * Gets the current sort selection
     */
    public String getSortSelection() {
        return (String) sortComboBox.getSelectedItem();
    }

    /**
     * Shows the add/edit task dialog
     */
    public void showTaskDialog(Task task, boolean isNew) {
        TaskDialog dialog = new TaskDialog(this, isNew ? "New Task" : "Edit Task", task);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            if (isNew) {
                controller.handleAddTask(dialog.getTask());
            } else {
                controller.handleUpdateTask(dialog.getTask());
            }
        }
    }

    /**
     * Shows the export options dialog
     */
    public void showExportDialog() {
        ExportDialog dialog = new ExportDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            controller.handleExportTasks(dialog.getFilename(),
                    dialog.includeDescription(),
                    dialog.includeDueDate(),
                    dialog.includePriority());
        }
    }

    /**
     * Shows the import dialog
     */
    public void showImportDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            controller.handleImportTasks(fileChooser.getSelectedFile().getPath());
        }
    }
}

/**
 * Custom table model for tasks
 */
class TaskTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Status", "Title", "Due Date", "Priority", "Created", "Description"};
    private List<Task> tasks = new ArrayList<>();

    public TaskTableModel() {
        // Initialize with empty list
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        fireTableDataChanged();
    }

    public Task getTaskAt(int row) {
        if (row >= 0 && row < tasks.size()) {
            return tasks.get(row);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Task task = tasks.get(row);

        switch (column) {
            case 0: // Status
                return task.getStatus() == TaskStatus.COMPLETE;
            case 1: // Title
                return task.getTitle();
            case 2: // Due Date
                return task.getDueDate() != null ? task.getDueDate().toString() : "";
            case 3: // Priority
                return task.getPriority();
            case 4: // Created
                return task.getCreatedAt().toString();
            case 5: // Description
                return task.getDescription();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 0; // Only status column is editable
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (column == 0) {
            Task task = tasks.get(row);
            task.setStatus((Boolean) value ? TaskStatus.COMPLETE : TaskStatus.INCOMPLETE);
            fireTableCellUpdated(row, column);
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0) {
            return Boolean.class;
        }
        return super.getColumnClass(column);
    }
}

/**
 * Dialog for adding/editing tasks
 */
class TaskDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JSpinner dueDateSpinner;
    private JComboBox<PriorityLevel> priorityCombo;
    private JCheckBox completeCheckBox;

    private boolean confirmed = false;
    private Task task;

    public TaskDialog(JFrame parent, String title, Task task) {
        super(parent, title, true);
        this.task = task;
        setSize(500, 400);
        setLocationRelativeTo(parent);
        initializeUI();
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Title:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        titleField = new JTextField(20);
        titleField.setText(task.getTitle());
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        // Priority
        JLabel priorityLabel = new JLabel("Priority:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(priorityLabel, gbc);

        priorityCombo = new JComboBox<>(PriorityLevel.values());
        priorityCombo.setSelectedItem(task.getPriority());
        gbc.gridx = 1;
        panel.add(priorityCombo, gbc);

        // Due date
        JLabel dueDateLabel = new JLabel("Due Date:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(dueDateLabel, gbc);

        dueDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dueDateSpinner, "yyyy-MM-dd HH:mm");
        dueDateSpinner.setEditor(editor);

        if (task.getDueDate() != null) {
            dueDateSpinner.setValue(Date.from(task.getDueDate().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }

        gbc.gridx = 1;
        panel.add(dueDateSpinner, gbc);

        // Complete checkbox
        completeCheckBox = new JCheckBox("Complete");
        completeCheckBox.setSelected(task.getStatus() == TaskStatus.COMPLETE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(completeCheckBox, gbc);

        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(descriptionLabel, gbc);

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setText(task.getDescription());
        gbc.gridx = 1;
        panel.add(new JScrollPane(descriptionArea), gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        JButton confirmButton = new JButton("OK");
        confirmButton.addActionListener(e -> {
            confirmed = true;
            saveData();
            dispose();
        });
        buttonPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void saveData() {
        task.setTitle(titleField.getText().trim());
        task.setDescription(descriptionArea.getText().trim());

        Date selectedDate = (Date) dueDateSpinner.getValue();
        task.setDueDate(selectedDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());

        task.setPriority((PriorityLevel) priorityCombo.getSelectedItem());
        task.setStatus(completeCheckBox.isSelected() ? TaskStatus.COMPLETE : TaskStatus.INCOMPLETE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Task getTask() {
        return task;
    }
}

/**
 * Dialog for export options
 */
class ExportDialog extends JDialog {
    private JTextField filenameField;
    private JCheckBox descriptionBox;
    private JCheckBox dueDateBox;
    private JCheckBox priorityBox;
    private boolean confirmed = false;
    private JButton browseButton;

    public ExportDialog(JFrame parent) {
        super(parent, "Export Tasks", true);
        setSize(500, 220);
        setLocationRelativeTo(parent);
        initializeUI();
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // File name selection
        JLabel filenameLabel = new JLabel("Save to:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(filenameLabel, gbc);

        filenameField = new JTextField("tasks_export.csv");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(filenameField, gbc);

        browseButton = new JButton("Browse...");
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(browseButton, gbc);

        // Add action listener for browse button
        browseButton.addActionListener(e -> showFileChooser());

        // Fields to export
        JLabel fieldsLabel = new JLabel("Fields to export:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(fieldsLabel, gbc);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));

        descriptionBox = new JCheckBox("Description");
        descriptionBox.setSelected(true);
        fieldsPanel.add(descriptionBox);

        dueDateBox = new JCheckBox("Due Date");
        dueDateBox.setSelected(true);
        fieldsPanel.add(dueDateBox);

        priorityBox = new JCheckBox("Priority");
        priorityBox.setSelected(true);
        fieldsPanel.add(priorityBox);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(fieldsPanel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        JButton confirmButton = new JButton("Export");
        confirmButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        buttonPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(buttonPanel, gbc);

        // Add main panel to dialog
        add(panel);
    }

    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Export Location");
        fileChooser.setSelectedFile(new File(filenameField.getText()));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // Ensure .csv extension
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }

            filenameField.setText(filePath);
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getFilename() {
        String filename = filenameField.getText().trim();
        if (!filename.isEmpty() && !filename.toLowerCase().endsWith(".csv")) {
            filename += ".csv";
        }
        return filename;
    }

    public boolean includeDescription() {
        return descriptionBox.isSelected();
    }

    public boolean includeDueDate() {
        return dueDateBox.isSelected();
    }

    public boolean includePriority() {
        return priorityBox.isSelected();
    }
}