package com.bank.bms.util;

import java.io.*;
import java.util.*;

import com.bank.bms.model.Account;
import com.bank.bms.model.Transaction;

public class FileUtil {
 
    public static void saveAccount(Account acc) throws IOException {

        FileWriter writer = new FileWriter("accounts.txt", true);
        writer.write(acc.toFileString() + "\n");
        writer.close();
    }

    public static List<Account> loadAccounts() throws IOException {

        List<Account> list = new ArrayList<>();
        File file = new File("accounts.txt");

        if (!file.exists()) return list;

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        while ((line = reader.readLine()) != null) {
            list.add(Account.fromFileString(line));
        }

        reader.close();
        return list;
    }

    public static void updateAllAccounts(List<Account> accounts) throws IOException {

        FileWriter writer = new FileWriter("accounts.txt");

        for (Account acc : accounts) {
            writer.write(acc.toFileString() + "\n");
        }

        writer.close();
    }

    public static void saveTransaction(Transaction t) throws IOException {

    	FileWriter writer = new FileWriter("transactions_" + t.getAccNumber() + ".txt", true);
        writer.write(t.toFileString() + "\n");
        writer.close();
    }

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

            System.out.println(
                "Type: " + parts[1] +
                " | Amount: " + parts[2] +
                " | Date: " + parts[3]
            );

        }

        reader.close();
    }
}