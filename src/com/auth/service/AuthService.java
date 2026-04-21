package com.auth.service;

import com.auth.db.DatabaseConnection;
import com.auth.model.User;
import com.auth.model.AuthResult;
import com.auth.util.AuthLogger;
import com.auth.util.InputValidator;
import com.auth.util.InputValidator.ValidationResult;
import com.auth.util.PasswordUtil;

import java.sql.*;

public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private final Connection connection;

    public AuthService() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public AuthResult registerUser(String username, String email, String password) {
        ValidationResult usernameCheck = InputValidator.validateUsername(username);
        if (!usernameCheck.isValid()) {
            AuthLogger.logRegistrationFailed(username, usernameCheck.getMessage());
            return new AuthResult(false, usernameCheck.getMessage());
        }

        ValidationResult emailCheck = InputValidator.validateEmail(email);
        if (!emailCheck.isValid()) {
            AuthLogger.logRegistrationFailed(username, emailCheck.getMessage());
            return new AuthResult(false, emailCheck.getMessage());
        }

        ValidationResult passwordCheck = InputValidator.validatePassword(password);
        if (!passwordCheck.isValid()) {
            AuthLogger.logRegistrationFailed(username, passwordCheck.getMessage());
            return new AuthResult(false, passwordCheck.getMessage());
        }

        if (isUsernameTaken(username)) {
            AuthLogger.logRegistrationFailed(username, "Username already exists");
            return new AuthResult(false, "Username is already taken.");
        }

        if (isEmailTaken(email)) {
            AuthLogger.logRegistrationFailed(username, "Email already exists");
            return new AuthResult(false, "Email is already registered.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                AuthLogger.logRegistration(username);
                return new AuthResult(true, "Registration successful!");
            }
        } catch (SQLException e) {
            AuthLogger.logRegistrationFailed(username, "Database error");
            return new AuthResult(false, "Internal database error.");
        }

        return new AuthResult(false, "Registration failed due to an unknown error.");
    }

    public AuthResult loginUser(String username, String password) {
        User user = getUserByUsername(username);

        if (user == null) {
            AuthLogger.logLoginFailed(username, 0);
            return new AuthResult(false, "Invalid username or password.");
        }

        if (user.isAccountLocked()) {
            AuthLogger.logSecurityWarning("Locked account login attempt: " + username);
            return new AuthResult(false, "Account is locked due to multiple failed attempts.");
        }

        if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            resetFailedAttempts(username);
            AuthLogger.logLoginSuccess(username);
            return new AuthResult(true, "Login successful.");
        } else {
            int newFailedAttempts = user.getFailedAttempts() + 1;
            incrementFailedAttempts(username, newFailedAttempts);

            if (newFailedAttempts >= MAX_FAILED_ATTEMPTS) {
                lockAccount(username);
                AuthLogger.logAccountLocked(username);
                return new AuthResult(false, "Incorrect password. Account has been LOCKED.");
            } else {
                int remaining = MAX_FAILED_ATTEMPTS - newFailedAttempts;
                AuthLogger.logLoginFailed(username, newFailedAttempts);
                return new AuthResult(false, "Incorrect password. " + remaining + " attempt(s) remaining.");
            }
        }
    }

    private User getUserByUsername(String username) {
        String sql = "SELECT id, username, email, password_hash, failed_attempts, account_locked, created_at FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"), rs.getString("username"), rs.getString("email"),
                    rs.getString("password_hash"), rs.getInt("failed_attempts"),
                    rs.getBoolean("account_locked"), rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Database error while fetching user: " + e.getMessage());
        }
        return null;
    }

    private boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {}
        return false;
    }

    private boolean isEmailTaken(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {}
        return false;
    }

    private void resetFailedAttempts(String username) {
        String sql = "UPDATE users SET failed_attempts = 0 WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {}
    }

    private void incrementFailedAttempts(String username, int failedAttempts) {
        String sql = "UPDATE users SET failed_attempts = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, failedAttempts);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {}
    }

    private void lockAccount(String username) {
        String sql = "UPDATE users SET account_locked = TRUE WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {}
    }
}
