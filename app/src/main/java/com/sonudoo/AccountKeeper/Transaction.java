package com.sonudoo.AccountKeeper;

public class Transaction {
    public int transactionNumber;
    public int transactionAccountNumber;
    public double transactionAmount;
    public int transactionType; // 0 = Expense Account, 1 = Income Account
    public String transactionJournalEntry;
    public long transactionTimestamp;

    Transaction(int transactionNumber, int transactionAccountNumber,
                double transactionAmount, int transactionType, String transactionJournalEntry,
                long transactionTimestamp) {
        this.transactionNumber = transactionNumber;
        this.transactionAccountNumber = transactionAccountNumber;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.transactionJournalEntry = transactionJournalEntry;
        this.transactionTimestamp = transactionTimestamp;
    }

    public String toString() {
        return transactionNumber + " " + transactionAmount + " " + transactionAccountNumber + " " + transactionJournalEntry;
    }
}
