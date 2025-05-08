// Main.java
import utils.ThemePreference;
import view.LoginView;
import view.ThemeManager;

import javax.swing.*;

/**
 * Main entry point for the application
 */
public class Main {
    public static void main(String[] args) {
        // Load saved theme or default to dark
        int savedTheme = ThemePreference.loadTheme();
        ThemeManager.applyTheme(savedTheme);

        // Start app
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}