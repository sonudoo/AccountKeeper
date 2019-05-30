package com.sonudoo.AccountKeeper;

public class Transaction {
    private static int transactionCount = 0;
    public int transactionId;
    public Account account;
    public double amount;
    public boolean action;
    private Transaction(){
        Transaction.transactionCount += 1;
        this.transactionId = Transaction.transactionCount;
    }
    Transaction(Account account, double amount, boolean action){
        this();
        this.account = account;
        this.amount = amount;
        this.action = action;
    }
}
