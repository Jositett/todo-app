// controller/RegistrationController.java
package controller;

import model.*;
import view.*;

/**
 * Controller for the registration view
 */
public class RegistrationController {
    private RegistrationView view;
    private TaskManager taskManager;

    public RegistrationController(RegistrationView view) {
        this.view = view;
        this.taskManager = new TaskManager();
    }

    /**
     * Handles registration button click
     */
    public void handleRegistration() {
        String username = view.getUsername();
        String password = view.getPassword();
        String confirmPassword = view.getConfirmPassword();

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            view.showErrorMessage("Username and password cannot be empty.");
            return;
        }

        if (password.length() < 6) {
            view.showErrorMessage("Password must be at least 6 characters long.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            view.showErrorMessage("Passwords do not match.");
            return;
        }

        try {
            if (taskManager.register(username, password)) {
                view.showSuccessMessage("Registration successful!");
                view.dispose();

                // Show login view again
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            } else {
                view.showErrorMessage("Username already exists.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Error during registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles back button click
     */
    public void handleBack() {
        view.dispose();
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
    }
}