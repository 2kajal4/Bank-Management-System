package com.bank.bms.app;

import com.bank.bms.service.RoleService;
import com.bank.bms.util.PasswordUtil;

import java.util.*;

import com.bank.bms.model.Account;
import com.bank.bms.service.BankService;
import com.bank.bms.util.FileUtil;

public class BankApp {

    static Scanner sc = new Scanner(System.in);
    static BankService service;

    public static void main(String[] args) throws Exception {

        service = new BankService();
        RoleService.loadRoles();

        while (true) {

            System.out.println("\n===== BANK SYSTEM =====");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            if (choice == 1) adminMenu();
            else if (choice == 2) userMenu();
            else if (choice == 3) break;
            else System.out.println("❌ Invalid choice!");
        }
    }

    // ADMIN
    static void adminMenu() throws Exception {

        System.out.print("Enter Role (SUPER_ADMIN / SUPER_USER): ");
        String role = sc.next();

        System.out.print("Enter Password: ");
        String inputPass = sc.next();

        if (!RoleService.authenticate(role, inputPass)) {
            System.out.println("Wrong Password!");
            return;
        }

        while (true) {

            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Create Account");
            System.out.println("2. View All Accounts");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            if (ch == 1) {
                
            	 sc.nextLine();

                System.out.print("Name: ");
                String name = sc.nextLine();

                System.out.print("Type: ");
                String type = sc.nextLine();

                System.out.print("Balance: ");
                double bal = sc.nextDouble();
                
                System.out.print("Set Password: ");
                String pass = sc.next();

                String encryptedPass = PasswordUtil.encrypt(pass);

                if (RoleService.hasPermission(role, "CREATE_ACCOUNT")) {
                    System.out.println(service.createAccount(name, type, bal,encryptedPass));
                } else {
                    System.out.println("Access Denied!");
                }
            }

            else if (ch == 2) {
                if (RoleService.hasPermission(role, "VIEW_ACCOUNTS")) {
                    for (Account acc : service.getAllAccounts()) {
                        acc.display();
                        System.out.println("-----------");
                    }
                } else {
                    System.out.println("Access Denied!");
                }
            }

            else if (ch == 3) break;
            else System.out.println("❌ Invalid choice!");
        }
    }

    // USER
    static void userMenu() throws Exception {

        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        Account acc = service.findAccount(accNo);

        if (acc == null) {
            System.out.println("Account not found!");
            return;
        }
        
     // ✅ ADD PASSWORD LOGIN HERE
        System.out.print("Enter Password: ");
        String inputPass = sc.next();

        if (!PasswordUtil.encrypt(inputPass).equals(acc.getPassword())) {
            System.out.println("Wrong Password!");
            return;
        }

        String role = "USER";   

        while (true) {

            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Balance");
            System.out.println("4. Transfer");
            System.out.println("5. History");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            if (ch == 1) {
                System.out.print("Amount: ");
                double amt = sc.nextDouble();

                if (RoleService.hasPermission(role, "DEPOSIT")) {
                    System.out.println(service.deposit(acc, amt));
                } else {
                    System.out.println("Access Denied!");
                }
            }

            else if (ch == 2) {
                System.out.print("Amount: ");
                double amt = sc.nextDouble();

                if (RoleService.hasPermission(role, "WITHDRAW")) {
                    System.out.println(service.withdraw(acc, amt));
                } else {
                    System.out.println("Access Denied!");
                }
            }

            else if (ch == 3) {
                if (RoleService.hasPermission(role, "BALANCE")) {
                    acc.display();
                } else {
                    System.out.println("Access Denied!");
                }
            }

            else if (ch == 4) {
                System.out.print("Receiver Acc: ");
                int toAcc = sc.nextInt();

                System.out.print("Amount: ");
                double amt = sc.nextDouble();

                if (RoleService.hasPermission(role, "TRANSFER")) {
                    System.out.println(service.transfer(acc, toAcc, amt));
                } else {
                    System.out.println("Access Denied!");
                }
            }

            else if (ch == 5) {
                if (RoleService.hasPermission(role, "HISTORY")) {
                    FileUtil.showTransactions(acc.getAccNumber());
                } else {
                    System.out.println("Access Denied!");
                }
            }

            else if (ch == 6) break;
            else System.out.println("❌ Invalid choice!");
        }
    }
}