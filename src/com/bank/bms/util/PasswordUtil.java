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
}
