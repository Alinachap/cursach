package com.testingsystem.client.model;

import com.testingsystem.common.dto.UserDTO;

/**
 * Session manager using Singleton pattern.
 * Stores the currently authenticated user's information.
 */
public class Session {
    private static volatile Session instance;
    
    private UserDTO currentUser;
    private boolean isAdmin;

    /**
     * Private constructor for Singleton pattern.
     */
    private Session() {
        this.currentUser = null;
        this.isAdmin = false;
    }

    /**
     * Gets the singleton instance of Session.
     *
     * @return the Session instance
     */
    public static Session getInstance() {
        if (instance == null) {
            synchronized (Session.class) {
                if (instance == null) {
                    instance = new Session();
                }
            }
        }
        return instance;
    }

    /**
     * Sets the current user.
     *
     * @param user the authenticated user
     */
    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
        this.isAdmin = user != null && user.getRole().getValue().equals("admin");
    }

    /**
     * Gets the current user.
     *
     * @return the current user, or null if not authenticated
     */
    public UserDTO getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if the current user is an admin.
     *
     * @return true if current user is admin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Gets the current user's ID.
     *
     * @return the user ID, or null if not authenticated
     */
    public Long getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    /**
     * Gets the current user's full name.
     *
     * @return the full name, or empty string if not authenticated
     */
    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getFullName() : "";
    }

    /**
     * Clears the session (logout).
     */
    public void clear() {
        this.currentUser = null;
        this.isAdmin = false;
    }

    /**
     * Checks if a user is currently authenticated.
     *
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    /**
     * Resets the singleton instance.
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.clear();
        }
        instance = null;
    }
}
