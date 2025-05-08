package view;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import utils.ThemePreference;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    public static final int LIGHT_THEME = 0;
    public static final int DARK_THEME = 1;

    private static int currentTheme = LIGHT_THEME;

    /**
     * Applies the selected theme
     */
    public static void applyTheme(int theme) {
        currentTheme = theme;

        try {
            if (theme == LIGHT_THEME) {
                // Use FlatLaf Light Theme
                FlatLightLaf.setup();
            } else {
                // Use FlatLaf Dark Theme (Darcula)
                FlatDarculaLaf.setup();
            }

            // Refresh UI
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
                window.pack(); // Ensure layout is updated
            }
            // Save Current Theme
            ThemePreference.saveTheme(theme);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getCurrentTheme() {
        return currentTheme;
    }
}