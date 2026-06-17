package com.bank.bms.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.*;

public class RoleService {

    private static Map<String, List<String>> rolePermissions = new HashMap<>();
    private static Map<String, String> rolePasswords = new HashMap<>();

    // ================= LOAD ROLES =================
    public static void loadRoles() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("roles.properties"));

            rolePermissions.clear();
            rolePasswords.clear();

            for (String role : props.stringPropertyNames()) {

                String value = props.getProperty(role);

                if (!value.contains("|")) {
                    System.out.println("Invalid entry in roles file for: " + role);
                    continue;
                }

                String[] parts = value.split("\\|");

                String permissionsPart = parts[0];
                String passwordPart = parts[1];

                List<String> permissions = Arrays.asList(permissionsPart.split(","));

                rolePermissions.put(role, permissions);

                // ✅ store password AS IT IS (no encryption)
                rolePasswords.put(role, passwordPart);
            }

        } catch (Exception e) {
            System.out.println("Error loading roles!");
            e.printStackTrace();
        }
    }

    // ================= PERMISSION =================
    public static boolean hasPermission(String role, String permission) {

        List<String> perms = rolePermissions.get(role);

        if (perms == null) return false;
        if (perms.contains("ALL")) return true;

        return perms.contains(permission);
    }

    // ================= AUTH =================
    public static boolean authenticate(String username, String inputPassword) {

        String storedPass = rolePasswords.get(username);

        if (storedPass == null) return false;

        // ✅ DIRECT COMPARISON (NO ENCRYPTION)
        return storedPass.equals(inputPassword);
    }

    // ================= ADD SUPER USER =================
    public static void addSuperUser(String name, String password) {
        try {
            FileWriter fw = new FileWriter("roles.properties", true);

            String roleKey = name.toUpperCase().replace(" ", "_");

            
            String entry = roleKey +
                    "=CREATE_ACCOUNT,VIEW_ACCOUNTS,EDIT_USER,DELETE_USER|" +
                    password;

            fw.write("\n" + entry);
            fw.close();

            System.out.println("Saved in roles file!");

            loadRoles();

        } catch (Exception e) {
            System.out.println("Error adding super user!");
            e.printStackTrace();
        }
    }
    
    public static boolean roleExists(String role) {
        return rolePasswords.containsKey(role);
    }
    public static void updatePassword(String username, String newPass) {

        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("roles.properties");
            props.load(fis);
            fis.close();

            String value = props.getProperty(username);
            if (value == null) return;

            String[] parts = value.split("\\|");

            String permissions = parts[0];

            // ✅ encrypt here
            String encrypted = com.bank.bms.util.PasswordUtil.encrypt(newPass);

            props.setProperty(username, permissions + "|" + encrypted);

            FileOutputStream fos = new FileOutputStream("roles.properties");
            props.store(fos, null);
            fos.close();

            // ✅ reload
            loadRoles();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updateRolePassword(String username, String newPassword) {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("roles.properties");
            props.load(fis);
            fis.close();

            String value = props.getProperty(username);

            if (value == null) {
                System.out.println("User not found in roles!");
                return;
            }

            String[] parts = value.split("\\|");

            // update password
            String updatedValue = parts[0] + "|" + newPassword;

            props.setProperty(username, updatedValue);

            FileWriter fw = new FileWriter("roles.properties");
            props.store(fw, null);
            fw.close();

            loadRoles(); // reload updated roles

            System.out.println("✅ Role password updated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}