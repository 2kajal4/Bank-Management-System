package com.bank.bms.service;

import java.util.List;

import com.bank.bms.model.Account;
import com.bank.bms.model.CurrentAccount;
import com.bank.bms.model.SavingAccount;
import com.bank.bms.model.Transaction;
import com.bank.bms.util.FileUtil;

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

    public String createAccount(String name, String type, double bal,String password) throws Exception {
    	
    	 int accNo = generateAccountNumber();   // 👈 AUTO GENERATE

        if (findAccount(accNo) != null) {
            return "Account already exists!";
        }

        Account acc = type.equalsIgnoreCase("Savings")
                ? new SavingAccount(accNo, name, bal,password)
                : new CurrentAccount(accNo, name, bal,password);

        accountList.add(acc);
        FileUtil.saveAccount(acc);

        return "Account Created Successfully!" + accNo;
    }
    
    private int generateAccountNumber() {

        if (accountList.isEmpty()) {
            return 1001; // starting account number
        }

        int lastAccNo = accountList.get(accountList.size() - 1).getAccNumber();
        return lastAccNo + 1;
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
