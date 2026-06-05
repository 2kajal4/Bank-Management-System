package com.bank.bms.service;
import java.io.FileInputStream;
import java.util.*;

import java.util.Map;

public class RoleService {
	
	private static Map<String, List<String>> rolePermissions = new HashMap<>();
	private static Map<String, String> rolePasswords = new HashMap<>();
	
	public static void loadRoles() {
	    try {
	        Properties props = new Properties();
	        props.load(new FileInputStream("roles.properties"));

	        for (String role : props.stringPropertyNames()) {

	            String value = props.getProperty(role);

	            String[] parts = value.split("\\|");

	            String permissionsPart = parts[0];
	            String passwordPart = parts[1];

	            List<String> permissions = Arrays.asList(permissionsPart.split(","));

	            rolePermissions.put(role, permissions);

	            // store encrypted password
	            rolePasswords.put(role, com.bank.bms.util.PasswordUtil.encrypt(passwordPart));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static boolean hasPermission(String role, String permission) {

	    List<String> perms = rolePermissions.get(role);

	    if (perms == null) return false;

	    if (perms.contains("ALL")) return true;

	    return perms.contains(permission);
	}
	
	
	public static boolean authenticate(String role, String inputPassword) {

	    String storedPass = rolePasswords.get(role);

	    if (storedPass == null) return false;

	    return storedPass.equals(
	        com.bank.bms.util.PasswordUtil.encrypt(inputPassword)
	    );
	}
}
