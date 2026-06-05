package com.bank.bms.model;

public class CurrentAccount extends Account {

    private double overdraftLimit = 5000;

    public CurrentAccount(int accNumber, String name, double balance,String password) {
        super(accNumber, name, "Current", balance,password);
    }

    @Override
    public String withdraw(double amount) {

        if (amount <= 0) return "Invalid Amount!";

        if (getBalance() + overdraftLimit >= amount) {

            updateBalance(-amount);

            return "Successful Withdrawal!";
        } else {
            return "Overdraft limit exceeded!";
        }
    }
}