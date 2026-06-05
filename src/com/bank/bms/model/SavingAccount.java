package com.bank.bms.model;

public class SavingAccount extends Account {

    private double interestRate = 4.0;

    public SavingAccount(int accNumber, String name, double balance,String password) {
        super(accNumber, name, "Savings", balance,password);
    }

    public void addInterest() {
        double interest = getBalance() * interestRate / 100;
        deposit(interest);
        System.out.println("Interest added: " + interest);
    }
}