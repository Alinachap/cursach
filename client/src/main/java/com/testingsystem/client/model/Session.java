package com.testingsystem.client.model;

import com.testingsystem.common.dto.UserDTO;

public class Session {
    private static volatile Session instance;

    private UserDTO currentUser;
    private boolean isAdmin;

    private Session() {
        this.currentUser = null;
        this.isAdmin = false;
    }

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

    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
        this.isAdmin = user != null && user.getRole().getValue().equals("admin");
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Long getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getFullName() : "";
    }

    public void clear() {
        this.currentUser = null;
        this.isAdmin = false;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.clear();
        }
        instance = null;
    }
}
