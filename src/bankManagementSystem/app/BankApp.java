package bankManagementSystem.app;

import java.util.*;

import bankManagementSystem.model.Account;
import bankManagementSystem.service.BankService;
import bankManagementSystem.util.FileUtil;

public class BankApp {

    static Scanner sc = new Scanner(System.in);
    static BankService service;

    public static void main(String[] args) throws Exception {

        service = new BankService();

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

        System.out.print("Enter Password: ");
        if (!sc.next().equals("admin123")) {
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
                System.out.print("Acc No: ");
                int accNo = sc.nextInt(); sc.nextLine();

                System.out.print("Name: ");
                String name = sc.nextLine();

                System.out.print("Type: ");
                String type = sc.nextLine();

                System.out.print("Balance: ");
                double bal = sc.nextDouble();

                System.out.println(service.createAccount(accNo, name, type, bal));
            }

            else if (ch == 2) {
                for (Account acc : service.getAllAccounts()) {
                    acc.display();
                    System.out.println("-----------");
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
                System.out.println(service.deposit(acc, amt));
            }

            else if (ch == 2) {
                System.out.print("Amount: ");
                double amt = sc.nextDouble();
                System.out.println(service.withdraw(acc, amt));
            }

            else if (ch == 3) acc.display();

            else if (ch == 4) {
                System.out.print("Receiver Acc: ");
                int toAcc = sc.nextInt();

                System.out.print("Amount: ");
                double amt = sc.nextDouble();

                System.out.println(service.transfer(acc, toAcc, amt));
            }

            else if (ch == 5) {
                FileUtil.showTransactions(acc.getAccNumber());
            }

            else if (ch == 6) break;
            else System.out.println("❌ Invalid choice!");
        }
    }
}