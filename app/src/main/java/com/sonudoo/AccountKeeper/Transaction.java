package com.sonudoo.AccountKeeper;

import java.util.Date;

public class Transaction {
    private static int transactionCount = 0;
    public int transactionId;
    public Account fromAccount;
    public double amount;
    public boolean toAccount; // 0 = Expense Account, 1 = Income Account
    public String journalEntry;
    public long timestamp;
    private Transaction(){
        Transaction.transactionCount += 1;
        this.transactionId = Transaction.transactionCount;
        this.timestamp = (new Date()).getTime();
    }

    Transaction(Account account, double amount, boolean toAccount, String journalEntry) {
        this();
        this.fromAccount = account;
        this.amount = amount;
        this.toAccount = toAccount;
        this.journalEntry = journalEntry;
    }

    public String toString() {
        return transactionId + " " + amount + " " + fromAccount.accountName + " " + toAccount + " " + journalEntry;
    }
}
