package utils;

import view.ThemeManager;

import java.io.*;

public class ThemePreference {
    private static final String THEME_FILE = "theme.pref";

    public static int loadTheme() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(THEME_FILE));
            int theme = Integer.parseInt(reader.readLine());
            reader.close();
            return theme;
        } catch (Exception e) {
            return ThemeManager.DARK_THEME; // Default to dark if no preference
        }
    }

    public static void saveTheme(int theme) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(THEME_FILE));
            writer.write(Integer.toString(theme));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
