package com.example.vacationplanner.utilities;

public class ValidationUtil {
    private static boolean validateUsername(String username){
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,15}$";
        return username.matches(pattern);
    }
    private static boolean validatePassword(String password){
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
        return password.matches(pattern);
    }


    public static boolean validateUserCredentials(String username, String password){
        return (validateUsername(username) && validatePassword(password));
    }

    public static String displayIssueWithUserCredentials(String username, String password){
        if(!ValidationUtil.validateUsername(username)){
            return "The username must contain 1 capital, be at least 6 characters long, and be at most 15 characters.";
        } else if(!ValidationUtil.validatePassword(password)){
            return "The password must be 8 characters long, be at most 20 characters, contain at least 1 capital," +
                    " 1 number, and have no spaces.";
        } else {
            return "Please enter a username and password.";
        }
    }
    
    public static String displayIssueWithUserCredentials(String username, String password, String confirmPassword){
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Please enter a username, password, and password confirmation.";
        } else if (!ValidationUtil.validateUsername(username)) {
            return "The username must contain 1 capital, be at least 6 characters long, and be at most 15 characters.";
        } else if (!password.equals(confirmPassword)) {
            return "The passwords entered do not match.";
        } else {
            return "The password must be 8 characters long, be at most 20 characters, contain at least 1 capital," +
                    " 1 number, and have no spaces.";
        }
    }
}
