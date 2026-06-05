package com.bank.bms.model;

public class Account {

    private int accNumber;
    private String name;
    private String type;
    private double balance;
    private String password;

    public Account(int accNumber, String name, String type, double balance, String password) {
        this.accNumber = accNumber;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.password = password;
    }

    public int getAccNumber() {
        return accNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getPassword() {
        return password;
    }
    
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("Invalid amount!");
        }
    }

    public String withdraw(double amount) {
        if (amount <= 0) return "Invalid Amount!";

        if (balance >= amount) {
            balance -= amount;
            return "Successful Withdrawal!";
        } else {
            return "Insufficient Balance!";
        }
    }

    
    protected void updateBalance(double amount) {
        balance += amount;
    }

    public String toFileString() {
        return accNumber + "," + name + "," + type + "," + balance+ "," + password;
    }

    public static Account fromFileString(String line) {

        String[] parts = line.split(",");

        int accNo = Integer.parseInt(parts[0].trim());
        String accName = parts[1].trim();
        String accType = parts[2].trim();
        double accBalance = Double.parseDouble(parts[3].trim());
        String password = parts[4].trim();

        if (accType.equalsIgnoreCase("Savings")) {
            return new SavingAccount(accNo, accName, accBalance, password);
        } else {
            return new CurrentAccount(accNo, accName, accBalance, password);
        }
    }

    public void display() {
        System.out.println("Account Number: " + accNumber);
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Balance: " + balance);
    }
}