package com.auth.util;

import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[A-Za-z0-9_]{3,50}$"
    );

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern HAS_UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern HAS_LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern HAS_DIGIT = Pattern.compile("[0-9]");
    private static final Pattern HAS_SPECIAL = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]");

    private InputValidator() {
        // utility class
    }

    public static ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Email cannot be empty.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return new ValidationResult(false, "Invalid email format. Example: user@example.com");
        }
        return new ValidationResult(true, "Email is valid.");
    }

    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty.");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return new ValidationResult(false, "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }
        if (!HAS_UPPERCASE.matcher(password).find()) {
            return new ValidationResult(false, "Password must contain at least 1 uppercase letter.");
        }
        if (!HAS_LOWERCASE.matcher(password).find()) {
            return new ValidationResult(false, "Password must contain at least 1 lowercase letter.");
        }
        if (!HAS_DIGIT.matcher(password).find()) {
            return new ValidationResult(false, "Password must contain at least 1 digit.");
        }
        if (!HAS_SPECIAL.matcher(password).find()) {
            return new ValidationResult(false, "Password must contain at least 1 special character.");
        }
        return new ValidationResult(true, "Password meets strength requirements.");
    }

    public static ValidationResult validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ValidationResult(false, "Username cannot be empty.");
        }
        if (!USERNAME_PATTERN.matcher(username.trim()).matches()) {
            return new ValidationResult(false, "Username must be 3-50 characters, using only letters, numbers, and underscores.");
        }
        return new ValidationResult(true, "Username is valid.");
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }
}
