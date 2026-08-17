package com.example.vacationplanner;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.vacationplanner.utilities.ValidationUtil;

/**
 * Unit tests for {@link ValidationUtil}
 */
public class ValidationUnitTest {
    // Tests the log in validation when input credentials meet the requirements
    @Test
    public void valid_Credentials_Login_Validation() {
        assertTrue(ValidationUtil.validateLogin("Tester1", "Tester1!").isValid);
    }

    // Tests the sign-up validation when input credentials meet the requirements
    @Test
    public void valid_Credentials_Signup_Validation() {
        assertTrue(ValidationUtil.validateSignUp("Tester1", "Tester1!", "Tester1!").isValid);
    }

    // Tests the log in validation when input credentials do not meet the requirements
    @Test
    public void invalid_Credentials_Login_Validation() {
        assertFalse("Invalid username should return false",
                ValidationUtil.validateLogin("test1", "Tester1!").isValid);
        assertFalse("Invalid password should return false",
                ValidationUtil.validateLogin("Tester1", "test1!").isValid);
    }

    // Tests the sign-up validation when input credentials do not meet the requirements
    @Test
    public void invalid_Credentials_Signup_Validation() {
        assertFalse("Invalid username should return false",
                ValidationUtil.validateSignUp("test1", "Tester1!", "Tester1!").isValid);
        assertFalse("Invalid password should return false",
                ValidationUtil.validateSignUp("Tester1", "test1!", "Tester1!").isValid);
        assertFalse("Invalid password confirmation should return false",
                ValidationUtil.validateSignUp("Tester1", "Tester1!", "test1!").isValid);

    }
}