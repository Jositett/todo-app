// controller/LoginController.java
package controller;

import model.*;
import view.*;

/**
 * Controller for the login view
 */
public class LoginController {
    private LoginView view;
    private TaskManager taskManager;

    public LoginController(LoginView view) {
        this.view = view;
        this.taskManager = new TaskManager();
    }

    /**
     * Handles login button click
     */
    // In LoginController.java
    public void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        try {
            if (taskManager.login(username, password)) {
                // Pass the authenticated TaskManager to TaskView
                TaskView taskView = new TaskView(taskManager); // Pass taskManager
                taskView.setVisible(true);
                view.dispose();
            } else {
                view.showErrorMessage("Invalid username or password.");
                view.clearPassword();
            }
        } catch (Exception e) {
            view.showErrorMessage("Error during login: " + e.getMessage());
        }
    }

    /**
     * Handles register button click
     */
    public void handleRegister() {
        RegistrationView registrationView = new RegistrationView();
        registrationView.setVisible(true);
        view.dispose();
    }
}