package com.bank.bms.service;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

import java.util.Map;

public class RoleService {
	
	private static Map<String, List<String>> rolePermissions = new HashMap<>();
	private static Map<String, String> rolePasswords = new HashMap<>();
	private static Map<String, String> userRoles = new HashMap<>();
	
	public static void loadRoles() {
	    try {
	        Properties props = new Properties();
	        props.load(new FileInputStream("roles.properties"));

	        for (String role : props.stringPropertyNames()) {

	            String roleKey = role; // clarity

	            String value = props.getProperty(role);

	            String[] parts = value.split("\\|");

	            String permissionsPart = parts[0];
	            String passwordPart = parts[1];

	            List<String> permissions = Arrays.asList(permissionsPart.split(","));

	            rolePermissions.put(roleKey, permissions);

	            // store encrypted password
	            rolePasswords.put(roleKey, com.bank.bms.util.PasswordUtil.encrypt(passwordPart));

	            // ✅ THIS IS THE IMPORTANT LINE (ADD HERE)
	            userRoles.put(roleKey, roleKey.equals("SUPER_ADMIN") ? "SUPER_ADMIN" : "SUPER_USER");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static boolean hasPermission(String role, String permission) {

		String actualRole = userRoles.get(role);

		List<String> perms = rolePermissions.get(actualRole);
	    if (perms == null) return false;

	    if (perms.contains("ALL")) return true;

	    return perms.contains(permission);
	}
	
	
	public static boolean authenticate(String username, String inputPassword) {
		username = username.toUpperCase(); 

	    String storedPass = rolePasswords.get(username);

	    if (storedPass == null) return false;

	    return storedPass.equals(
	        com.bank.bms.util.PasswordUtil.encrypt(inputPassword)
	    );
	}
	
	
	public static String getRole(String username) {

	    if (username.equals("SUPER_ADMIN")) return "SUPER_ADMIN";

	    return "SUPER_USER"; // all others are super users
	}
	
	
	public static void addSuperUser(String name, String password) {
	    try {
	        FileWriter fw = new FileWriter("roles.properties", true);

	        String roleKey = name.toUpperCase().replace(" ", "_");

	        String entry = roleKey + "=CREATE_ACCOUNT,VIEW_ACCOUNTS|" + password;

	        fw.write("\n" + entry);
	        fw.close();

	        System.out.println("Saved in roles file!");

	        loadRoles(); // reload roles

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
