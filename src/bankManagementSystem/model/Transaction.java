package bankManagementSystem.model;

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

    public String toFileString() {
        return accNumber + "," + type + "," + amount + "," + date;
    }
}