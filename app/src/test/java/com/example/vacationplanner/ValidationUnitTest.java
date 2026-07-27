package com.example.vacationplanner;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.vacationplanner.utilities.ValidationUtil;


public class ValidationUnitTest {
    // Tests the username validation when the input username meets the username requirements
    @Test
    public void correctUsername_Validation() {
        assertTrue(ValidationUtil.validateUserCredentials("Tester1", "Tester1!"));
    }

    // Tests the username validation when the input username does not meet the username requirements
    @Test
    public void incorrectUsername_Validation(){
        assertFalse(ValidationUtil.validateUserCredentials("test1", "Tester1!"));
    }

    // Tests the password validation when the input password meets the password requirements
    @Test
    public void correctPassword_Validation(){
        assertTrue(ValidationUtil.validateUserCredentials("Tester1", "Tester1!"));
    }

    // Tests the password validation when the input password does not meet the password requirements
    @Test
    public void incorrectPassword_Validation(){
        assertFalse(ValidationUtil.validateUserCredentials("Tester1", "test1!"));
    }
}