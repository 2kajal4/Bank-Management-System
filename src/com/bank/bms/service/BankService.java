package com.bank.bms.service;

import java.util.List;

import com.bank.bms.model.Account;
import com.bank.bms.model.CurrentAccount;
import com.bank.bms.model.SavingAccount;
import com.bank.bms.model.Transaction;
import com.bank.bms.util.FileUtil;

public class BankService {
	
	private static final double DAILY_LIMIT = 10000;
	private static final double WEEKLY_LIMIT = 50000;
	private static final double MONTHLY_LIMIT = 200000;
	
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

    public String createAccount(String name, String type, double bal,String password,String phone, String email) throws Exception {
    	
    	 int accNo = generateAccountNumber();   

        if (findAccount(accNo) != null) {
            return "Account already exists!";
        }

        Account acc = type.equalsIgnoreCase("Savings")
                ? new SavingAccount(accNo, name, bal,password, phone, email)
                : new CurrentAccount(accNo, name, bal,password, phone, email);

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

        double daily = FileUtil.getTotalAmount(acc.getAccNumber(), 1);
        double weekly = FileUtil.getTotalAmount(acc.getAccNumber(), 7);
        double monthly = FileUtil.getTotalAmount(acc.getAccNumber(), 30);

        if (daily + amt > DAILY_LIMIT)
            return "❌ Daily limit exceeded!";

        if (weekly + amt > WEEKLY_LIMIT)
            return "❌ Weekly limit exceeded!";

        if (monthly + amt > MONTHLY_LIMIT)
            return "❌ Monthly limit exceeded!";

        acc.deposit(amt);

        FileUtil.updateAllAccounts(accountList);
        FileUtil.saveTransaction(new Transaction(acc.getAccNumber(), "Deposit", amt));

        return "Deposit Successful! Balance: " + acc.getBalance();
    }

    public String withdraw(Account acc, double amt) throws Exception {

        double daily = FileUtil.getTotalAmount(acc.getAccNumber(), 1);
        double weekly = FileUtil.getTotalAmount(acc.getAccNumber(), 7);
        double monthly = FileUtil.getTotalAmount(acc.getAccNumber(), 30);

        if (daily + amt > DAILY_LIMIT)
            return "❌ Daily limit exceeded!";

        if (weekly + amt > WEEKLY_LIMIT)
            return "❌ Weekly limit exceeded!";

        if (monthly + amt > MONTHLY_LIMIT)
            return "❌ Monthly limit exceeded!";

        String res = acc.withdraw(amt);

        if (res.contains("Successful")) {

            FileUtil.updateAllAccounts(accountList);
            FileUtil.saveTransaction(new Transaction(acc.getAccNumber(), "Withdraw", amt));

            return "Withdrawal Successful! Balance: " + acc.getBalance();
        }

        return res;
    }

    public String transfer(Account sender, int toAcc, double amt) throws Exception {

        Account receiver = findAccount(toAcc);

        if (receiver == null) return "Invalid Receiver!";
        if (amt <= 0) return "Invalid Amount!";

        double daily = FileUtil.getTotalAmount(sender.getAccNumber(), 1);
        double weekly = FileUtil.getTotalAmount(sender.getAccNumber(), 7);
        double monthly = FileUtil.getTotalAmount(sender.getAccNumber(), 30);

        if (daily + amt > DAILY_LIMIT)
            return "❌ Daily limit exceeded!";

        if (weekly + amt > WEEKLY_LIMIT)
            return "❌ Weekly limit exceeded!";

        if (monthly + amt > MONTHLY_LIMIT)
            return "❌ Monthly limit exceeded!";

        String res = sender.withdraw(amt);

        if (res.contains("Successful")) {

            receiver.deposit(amt);

            FileUtil.updateAllAccounts(accountList);

            FileUtil.saveTransaction(new Transaction(sender.getAccNumber(), "Transfer Sent", amt));
            FileUtil.saveTransaction(new Transaction(toAcc, "Transfer Received", amt));

            return "Transfer Successful! Balance: " + sender.getBalance();
        }

        return res;
    }
    
    public String deleteAccount(int accNo) throws Exception {

        Account acc = findAccount(accNo);

        if (acc == null) return "Account not found!";

        accountList.remove(acc);
        FileUtil.updateAllAccounts(accountList);

        return "Account Deleted Successfully!";
    }
    
    public String updateAccount(int accNo, String newName) throws Exception {

        Account acc = findAccount(accNo);

        if (acc == null) return "Account not found!";

       
        java.lang.reflect.Field field = acc.getClass().getSuperclass().getDeclaredField("name");
        field.setAccessible(true);
        field.set(acc, newName);

        FileUtil.updateAllAccounts(accountList);

        return "Account Updated Successfully!";
    }
    
    

}
