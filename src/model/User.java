// model/User.java
package model;

import java.io.Serializable;

/**
 * Represents a user in the system
 */
public class User implements Serializable {
    private final String username;
    private final String hashedPassword;

    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}