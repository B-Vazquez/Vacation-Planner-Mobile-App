package com.example.vacationplanner.utilities;

public class ValidationUtil {
    public static boolean validateUsername(String username){
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";
        return username.matches(pattern);
    }
    public static boolean validatePassword(String password){
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }
}
