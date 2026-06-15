package com.bank.bms.util;
import java.security.MessageDigest;

public class PasswordUtil {

	 public static String encrypt(String password) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            byte[] hash = md.digest(password.getBytes());

	            StringBuilder hex = new StringBuilder();
	            for (byte b : hash) {
	                String s = Integer.toHexString(0xff & b);
	                if (s.length() == 1) hex.append('0');
	                hex.append(s);
	            }

	            return hex.toString();

	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	 
	 public static boolean isValidName(String name) {
		    return name.matches("[a-zA-Z ]+");
		}

		public static boolean isValidNumber(String input) {
		    return input.matches("\\d+");
		}

		public static boolean isValidAmount(double amt) {
		    return amt > 0;
		}
		
		
		public static boolean isStrongPassword(String password) {

		    if (password.length() < 6) return false;

		    boolean hasUpper = false;
		    boolean hasLower = false;
		    boolean hasDigit = false;

		    for (char ch : password.toCharArray()) {
		        if (Character.isUpperCase(ch)) hasUpper = true;
		        else if (Character.isLowerCase(ch)) hasLower = true;
		        else if (Character.isDigit(ch)) hasDigit = true;
		    }

		    return hasUpper && hasLower && hasDigit;
		}
}
