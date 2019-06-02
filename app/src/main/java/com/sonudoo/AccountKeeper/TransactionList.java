package com.sonudoo.AccountKeeper;

import java.util.ArrayList;

public class TransactionList {
    private static TransactionList singleton_instance = null;
    private ArrayList <Transaction> transactionList;
    private TransactionList(){
        transactionList = new ArrayList<Transaction>();
    }
    public static TransactionList getInstance(){
        if(TransactionList.singleton_instance == null) {
            singleton_instance = new TransactionList();
        }
        return singleton_instance;
    }

    public boolean addTransaction(Account fromAccount, double amount, boolean toAccount, String journalEntry) {
        if (toAccount == false) {
            if (fromAccount.withdraw(amount) == true) {
                transactionList.add(new Transaction(fromAccount, amount, false, journalEntry));
                return true;
            } else {
                return false;
            }
        } else {
            fromAccount.deposit(amount);
            transactionList.add(new Transaction(fromAccount, amount, true, journalEntry));
            return true;
        }
    }

    public ArrayList<Transaction> getTransactions() {
        return transactionList;
    }
}
