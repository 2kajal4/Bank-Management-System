package bankManagementSystem.model;

public class CurrentAccount extends Account {

    private double overdraftLimit = 5000;

    public CurrentAccount(int accNumber, String name, double balance) {
        super(accNumber, name, "Current", balance);
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