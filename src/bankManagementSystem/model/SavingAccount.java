package bankManagementSystem.model;

public class SavingAccount extends Account {

    private double interestRate = 4.0;

    public SavingAccount(int accNumber, String name, double balance) {
        super(accNumber, name, "Savings", balance);
    }

    public void addInterest() {
        double interest = getBalance() * interestRate / 100;
        deposit(interest);
        System.out.println("Interest added: " + interest);
    }
}