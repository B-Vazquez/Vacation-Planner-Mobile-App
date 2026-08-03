package com.example.vacationplanner.utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Utility class for validating user input and credentials.
 */
public class ValidationUtil {

    /**
     * Represents the result of a validation operation.
     */
    public static class ValidationResult {
        public final boolean isValid;
        @Nullable public final String errorMessage;

        public ValidationResult(boolean isValid, @Nullable String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }
    }

    private static boolean validateUsername(String username){
        // Username must contain at least 1 capital, be 6-15 characters, no spaces
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,15}$";
        return username.matches(pattern);
    }

    private static boolean validatePassword(String password){
        // Password must contain at least 1 number, 1 capital, 1 lowercase, 8-20 characters, no spaces
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
        return password.matches(pattern);
    }

    /**
     * Validates user credentials for login.
     * @param username The entered username.
     * @param password The entered password.
     * @return ValidationResult indicating success or specific failure message.
     */
    public static ValidationResult validateLogin(@NonNull String username, @NonNull String password){
        if (username.isEmpty() || password.isEmpty()) {
            return new ValidationResult(false, "Please enter a username and password.");
        }
        if (!validateUsername(username)) {
            return new ValidationResult(false, "The username must contain 1 capital and be 6-15 characters long.");
        }
        if (!validatePassword(password)) {
            return new ValidationResult(false, "The password must be 8-20 characters long and contain at least 1 capital and 1 number.");
        }
        return new ValidationResult(true, null);
    }

    /**
     * Validates user credentials for sign up.
     * @param username The entered username.
     * @param password The entered password.
     * @param confirmPassword The confirmed password.
     * @return ValidationResult indicating success or specific failure message.
     */
    public static ValidationResult validateSignUp(@NonNull String username, @NonNull String password, @NonNull String confirmPassword){
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return new ValidationResult(false, "Please fill in all fields.");
        }
        if (!validateUsername(username)) {
            return new ValidationResult(false, "The username must contain 1 capital and be 6-15 characters long.");
        }
        if (!password.equals(confirmPassword)) {
            return new ValidationResult(false, "The passwords entered do not match.");
        }
        if (!validatePassword(password)) {
            return new ValidationResult(false, "The password must be 8-20 characters long and contain at least 1 capital and 1 number.");
        }
        return new ValidationResult(true, null);
    }
}
