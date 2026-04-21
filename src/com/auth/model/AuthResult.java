package com.auth.model;

/**
 * AuthResult.java — Simple wrapper for authentication outcomes.
 * 
 * Used to return structured responses from AuthService to the API handler,
 * containing both a success flag and a descriptive message.
 */
public class AuthResult {
    private final boolean success;
    private final String message;

    public AuthResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
