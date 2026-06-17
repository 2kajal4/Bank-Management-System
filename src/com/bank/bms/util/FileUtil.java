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

            // ✅ NEW FORMAT (5 parts)
            if (parts.length >= 5) {
                System.out.println(
                    "Type: " + parts[1] +
                    " | Amount: " + parts[2] +
                    " | Time(ms): " + parts[3] +
                    " | Date: " + parts[4]
                );
            }
            // ✅ OLD FORMAT (4 parts)
            else if (parts.length == 4) {
                System.out.println(
                    "Type: " + parts[1] +
                    " | Amount: " + parts[2] +
                    " | Date: " + parts[3]
                );
            }
            else {
                System.out.println("⚠ Invalid transaction record skipped");
            }
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

            // ✅ ensure correct format (now should be at least 5 parts)
            if (parts.length < 5) continue;

            double amount = Double.parseDouble(parts[2]);

            try {
                // ✅ FIX: read milliseconds instead of parsing string date
                long millis = Long.parseLong(parts[3]);

                Date date = new Date(millis);

                if (currentTime - date.getTime() <= timeLimit) {
                    total += amount;
                }

            } catch (Exception e) {
                System.out.println("⚠ Invalid transaction entry skipped");
            }
        }

        reader.close();
        return total;
    }
}