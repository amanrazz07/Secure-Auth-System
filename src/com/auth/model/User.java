package com.auth.model;

import java.sql.Timestamp;

public class User {

    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private int failedAttempts;
    private boolean accountLocked;
    private Timestamp createdAt;

    public User() {
    }

    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.failedAttempts = 0;
        this.accountLocked = false;
    }

    public User(int id, String username, String email, String passwordHash,
                int failedAttempts, boolean accountLocked, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.failedAttempts = failedAttempts;
        this.accountLocked = accountLocked;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }

    public boolean isAccountLocked() { return accountLocked; }
    public void setAccountLocked(boolean accountLocked) { this.accountLocked = accountLocked; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", failedAttempts=" + failedAttempts +
                ", accountLocked=" + accountLocked +
                ", createdAt=" + createdAt +
                '}';
    }
}
