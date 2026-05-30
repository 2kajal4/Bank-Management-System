package bankManagementSystem.service;

import java.util.List;

import bankManagementSystem.model.Account;
import bankManagementSystem.model.CurrentAccount;
import bankManagementSystem.model.SavingAccount;
import bankManagementSystem.model.Transaction;
import bankManagementSystem.util.FileUtil;

public class BankService {
	
    private List<Account> accountList;

    public BankService() throws Exception {
        accountList = FileUtil.loadAccounts();
    }

    public List<Account> getAllAccounts() {
        return accountList;
    }

    public Account findAccount(int accNo) {
        for (Account acc : accountList) {
            if (acc.getAccNumber() == accNo) return acc;
        }
        return null;
    }

    public String createAccount(int accNo, String name, String type, double bal) throws Exception {

        if (findAccount(accNo) != null) {
            return "Account already exists!";
        }

        Account acc = type.equalsIgnoreCase("Savings")
                ? new SavingAccount(accNo, name, bal)
                : new CurrentAccount(accNo, name, bal);

        accountList.add(acc);
        FileUtil.saveAccount(acc);

        return "Account Created Successfully!";
    }

    public String deposit(Account acc, double amt) throws Exception {

        if (amt <= 0) return "Invalid Amount!";

        acc.deposit(amt);
        FileUtil.updateAllAccounts(accountList);
        FileUtil.saveTransaction(new Transaction(acc.getAccNumber(), "Deposit", amt));

        return "Deposit Successful! Updated Balance: " + acc.getBalance();
    }

    public String withdraw(Account acc, double amt) throws Exception {

        String res = acc.withdraw(amt);

        if (res.contains("Successful")) {
            FileUtil.updateAllAccounts(accountList);
            FileUtil.saveTransaction(new Transaction(acc.getAccNumber(), "Withdraw", amt));

            return "Withdrawal Successful! Remaining Balance: " + acc.getBalance();
        }

        return res;
    }

    public String transfer(Account sender, int toAcc, double amt) throws Exception {

        Account receiver = findAccount(toAcc);

        if (receiver == null) return "Invalid Receiver Account!";
        if (amt <= 0) return "Invalid Amount!";

        String res = sender.withdraw(amt);

        if (res.contains("Successful")) {

            receiver.deposit(amt);
            FileUtil.updateAllAccounts(accountList);

            FileUtil.saveTransaction(new Transaction(sender.getAccNumber(), "Transfer Sent", amt));
            FileUtil.saveTransaction(new Transaction(toAcc, "Transfer Received", amt));

            return "Transfer Successful! Your Balance: " + sender.getBalance();
        }

        return res;
    }

}
