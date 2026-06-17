package com.bank.bms.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    private int accNumber;
    private String type;
    private double amount;
    private String date;

    public Transaction(int accNumber, String type, double amount) {

        this.accNumber = accNumber;
        this.type = type;
        this.amount = amount;

        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.date = format.format(currentDate);
    }

    public int getAccNumber() {
        return accNumber;
    }
    public String toFileString() {
    	long millis = System.currentTimeMillis();

        java.text.SimpleDateFormat format =
            new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

        String formattedDate = format.format(new java.util.Date(millis));

        return accNumber + "," + type + "," + amount + "," + millis + "," + formattedDate;
    }
}