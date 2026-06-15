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

            int choice = getValidInt();

            if (choice == 1) adminMenu();
            else if (choice == 2) userMenu();
            else if (choice == 3) break;
            else System.out.println("❌ Invalid choice!");
        }
    }

    // ================= ADMIN =================
    static void adminMenu() throws Exception {

        String username;

        while (true) {
            System.out.print("Enter Username: ");
            username = sc.next();

            if (username.matches("[A-Za-z_]+")) break;
            else System.out.println("❌ Invalid Username!");
        }

        System.out.print("Enter Password: ");
        String inputPass = sc.next();

        if (!RoleService.authenticate(username, inputPass)) {
            System.out.println("Wrong Username or Password!");
            return;
        }

        showWelcome(username);
        String role = username;

        while (true) {

            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Create Account");
            System.out.println("2. View All Accounts");
            System.out.println("3. Create Super User");
            System.out.println("4. Delete User");
            System.out.println("5. Edit User");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            int ch = getValidInt();

            // ================= CREATE ACCOUNT =================
            if (ch == 1) {

                sc.nextLine();

                // NAME
                String name;
                while (true) {
                    System.out.print("Name: ");
                    name = sc.nextLine();

                    if (PasswordUtil.isValidName(name)) break;
                    else System.out.println("❌ Only letters allowed!");
                }

                // TYPE
                String type;
                while (true) {
                    System.out.print("Type (saving/current): ");
                    type = sc.nextLine();

                    if (type.equalsIgnoreCase("saving") || type.equalsIgnoreCase("current"))
                        break;
                    else System.out.println("❌ Invalid type!");
                }

                // BALANCE
                double bal = getValidAmount();

                // PASSWORD
                String pass;
                while (true) {
                    System.out.print("Set Password: ");
                    pass = sc.next();

                    if (!PasswordUtil.isStrongPassword(pass)) {
                        System.out.println("❌ Weak Password!");
                    } else break;
                }

                String encryptedPass = PasswordUtil.encrypt(pass);

                // ✅ PHONE (ADD HERE)
                String phone;
                while (true) {
                    System.out.print("Phone: ");
                    phone = sc.next();

                    if (phone.matches("\\d{10}")) break;
                    else System.out.println("❌ Enter valid 10-digit phone number!");
                }

                // ✅ EMAIL (ADD HERE)
                String email;
                while (true) {
                    System.out.print("Email: ");
                    email = sc.next();

                    if (email.contains("@") && email.contains(".")) break;
                    else System.out.println("❌ Invalid email!");
                }

                // SERVICE CALL
                if (RoleService.hasPermission(role, "CREATE_ACCOUNT")) {
                    System.out.println(
                        service.createAccount(name, type, bal, encryptedPass, phone, email)
                    );
                } else {
                    System.out.println("Access Denied!");
                }
            }

            // ================= VIEW =================
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

            // ================= CREATE SUPER USER =================
            else if (ch == 3) {

                if (role.equals("SUPER_ADMIN")) {

                    sc.nextLine();

                    String name;
                    while (true) {
                        System.out.print("Enter Super User Name: ");
                        name = sc.nextLine();

                        if (PasswordUtil.isValidName(name)) break;
                        else System.out.println("❌ Invalid Name!");
                    }

                    String pass;
                    while (true) {
                        System.out.print("Set Password: ");
                        pass = sc.next();

                        if (!PasswordUtil.isStrongPassword(pass)) {
                            System.out.println("❌ Weak Password!");
                        } else break;
                    }

                    RoleService.addSuperUser(name, pass);
                    System.out.println("✅ Super User Created!");

                } else {
                    System.out.println("❌ Only SUPER_ADMIN allowed!");
                }
            }

            // ================= DELETE =================
            else if (ch == 4) {

                int accNo = getValidNumber("Enter Account No: ");

                if (RoleService.hasPermission(role, "DELETE_USER")) {
                    System.out.println(service.deleteAccount(accNo));
                } else {
                    System.out.println("Access Denied!");
                }
            }

            // ================= EDIT =================
            else if (ch == 5) {

                int accNo = getValidNumber("Enter Account No: ");
                sc.nextLine();

                String newName;
                while (true) {
                    System.out.print("Enter New Name: ");
                    newName = sc.nextLine();

                    if (PasswordUtil.isValidName(newName)) break;
                    else System.out.println("❌ Invalid Name!");
                }

                if (RoleService.hasPermission(role, "EDIT_USER")) {
                    System.out.println(service.updateAccount(accNo, newName));
                } else {
                    System.out.println("Access Denied!");
                }
            }

            else if (ch == 6) break;
            else System.out.println("❌ Invalid choice!");
        }
    }

    // ================= USER =================
    static void userMenu() throws Exception {

        int accNo = getValidNumber("Enter Account Number: ");

        Account acc = service.findAccount(accNo);

        if (acc == null) {
            System.out.println("Account not found!");
            return;
        }

        System.out.print("Enter Password: ");
        String inputPass = sc.next();

        if (!PasswordUtil.encrypt(inputPass).equals(acc.getPassword())) {
            System.out.println("Wrong Password!");
            return;
        }
        showWelcome(acc.getName());

        while (true) {

            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Balance");
            System.out.println("4. Transfer");
            System.out.println("5. History");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            int ch = getValidInt();

            if (ch == 1) {
                double amt = getValidAmount();
                System.out.println(service.deposit(acc, amt));
            }

            else if (ch == 2) {
                double amt = getValidAmount();
                System.out.println(service.withdraw(acc, amt));
            }

            else if (ch == 3) {
                acc.display();
            }

            else if (ch == 4) {
                int toAcc = getValidNumber("Receiver Acc: ");
                double amt = getValidAmount();

                System.out.println(service.transfer(acc, toAcc, amt));
            }

            else if (ch == 5) {
                FileUtil.showTransactions(acc.getAccNumber());
            }

            else if (ch == 6) break;
            else System.out.println("❌ Invalid choice!");
        }
    }

    // ================= VALIDATIONS =================

    public static int getValidInt() {
        while (true) {
            String input = sc.next();

            if (!input.matches("\\d+")) {
                System.out.println("❌ Enter valid number!");
                continue;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Number too large!");
            }
        }
    }

    public static int getValidNumber(String msg) {
        while (true) {
            System.out.print(msg);
            String input = sc.next();

            if (!input.matches("\\d+")) {
                System.out.println("❌ Invalid number!");
                continue;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Number too large!");
            }
        }
    }

    public static double getValidAmount() {
        while (true) {
            System.out.print("Amount: ");
            String input = sc.next();

            if (!input.matches("\\d+(\\.\\d+)?")) {
                System.out.println("❌ Enter valid amount!");
                continue;
            }

            double amt = Double.parseDouble(input);

            if (amt <= 0) {
                System.out.println("❌ Amount must be greater than 0!");
            } else {
                return amt;
            }
        }
    }
    
 // ================= WELCOME MESSAGE =================
    public static void showWelcome(String name) {

        String greeting;

        int hour = java.time.LocalTime.now().getHour();

        if (hour < 12) greeting = "Good Morning";
        else if (hour < 17) greeting = "Good Afternoon";
        else if (hour < 21) greeting = "Good Evening";
        else greeting = "Good Night";

        System.out.println("\n=======================================");
        System.out.println("🏦       SECURE BANK SYSTEM       🏦");
        System.out.println("=======================================");
        System.out.println("👋 " + greeting + ", " + name + "!");
        System.out.println("🔐 Login Successful");
        System.out.println("=======================================\n");
    }
}