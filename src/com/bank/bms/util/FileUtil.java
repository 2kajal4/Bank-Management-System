package com.bank.bms.util;

import java.io.*;
import java.util.*;

import com.bank.bms.model.Account;
import com.bank.bms.model.Transaction;

public class FileUtil {

    // ================= SAVE ACCOUNT =================
    public static void saveAccount(Account acc) throws IOException {

        FileWriter writer = new FileWriter("accounts.txt", true);
        writer.write(acc.toFileString());
        writer.write("\n");
        writer.close();
    }

    // ================= LOAD ACCOUNTS =================
    public static List<Account> loadAccounts() throws IOException {

        List<Account> list = new ArrayList<>();
        File file = new File("accounts.txt");

        if (!file.exists()) return list;

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        while ((line = reader.readLine()) != null) {

            
            if (line.trim().isEmpty()) continue;

            list.add(Account.fromFileString(line));
        }

        reader.close();
        return list;
    }

    // ================= UPDATE ALL =================
    public static void updateAllAccounts(List<Account> accounts) throws IOException {

        FileWriter writer = new FileWriter("accounts.txt");

        for (Account acc : accounts) {
            writer.write(acc.toFileString());
            writer.write("\n");
        }

        writer.close();
    }

    // ================= SAVE TRANSACTION =================
    public static void saveTransaction(Transaction t) throws IOException {

        FileWriter writer = new FileWriter(
                "transactions_" + t.getAccNumber() + ".txt", true);

        writer.write(t.toFileString());
        writer.write("\n");

        writer.close();
    }

    // ================= SHOW TRANSACTIONS =================
    public static void showTransactions(int accNo) throws IOException {

        File file = new File("transactions_" + accNo + ".txt");

        if (!file.exists()) {
            System.out.println("No transactions found.");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        while ((line = reader.readLine()) != null) {

            String[] parts = line.split(",");

            
            if (parts.length < 4) continue;

            System.out.println(
                    "Type: " + parts[1] +
                    " | Amount: " + parts[2] +
                    " | Date: " + parts[3]
            );
        }

        reader.close();
    }

    // ================= LIMIT CALCULATION =================
    public static double getTotalAmount(int accNo, int days) throws IOException {

        File file = new File("transactions_" + accNo + ".txt");

        if (!file.exists()) return 0;

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        double total = 0;

        long currentTime = System.currentTimeMillis();
        long timeLimit = days * 24L * 60 * 60 * 1000;

        while ((line = reader.readLine()) != null) {

            String[] parts = line.split(",");

           
            if (parts.length < 4) continue;

            double amount = Double.parseDouble(parts[2]);
            String dateStr = parts[3];

            java.text.SimpleDateFormat format =
                    new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            try {
                Date date = format.parse(dateStr);

                if (currentTime - date.getTime() <= timeLimit) {
                    total += amount;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        reader.close();
        return total;
    }
}