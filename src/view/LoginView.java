// view/LoginView.java
package view;

import controller.LoginController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login screen for the application
 */
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JCheckBox showPasswordCheckBox;

    private LoginController controller;

    public LoginView() {
        controller = new LoginController(this);
        initializeUI();
    }

    /**
     * Initializes the user interface
     */
    private void initializeUI() {
        setTitle("Login - Todo App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Login to Todo App");
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

        // Show password checkbox
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(showPasswordCheckBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> controller.handleLogin());
        buttonPanel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> controller.handleRegister());
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    /**
     * Toggles password field visibility
     */
    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char)0);
        } else {
            passwordField.setEchoChar('*');
        }
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
     * Clears the password field
     */
    public void clearPassword() {
        passwordField.setText("");
    }

    /**
     * Shows an error message
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}