// view/RegistrationView.java
package view;

import controller.RegistrationController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Registration form for new users
 */
public class RegistrationView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;

    private RegistrationController controller;

    public RegistrationView() {
        controller = new RegistrationController(this);
        initializeUI();
    }

    /**
     * Initializes the user interface
     */
    private void initializeUI() {
        setTitle("Register - Todo App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Create a New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Confirm password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(confirmPasswordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> controller.handleRegistration());
        buttonPanel.add(registerButton);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> controller.handleBack());
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    /**
     * Gets the entered username
     */
    public String getUsername() {
        return usernameField.getText().trim();
    }

    /**
     * Gets the entered password
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    /**
     * Gets the entered confirm password
     */
    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    /**
     * Clears all fields
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    /**
     * Shows an error message
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success message
     */
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
    }
}