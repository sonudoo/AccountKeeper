package com.sonudoo.AccountKeeper;

public class Transaction {
    final int transactionAccountNumber;
    final double transactionAmount;
    final int transactionType;
    final String transactionJournalEntry;
    final long transactionTimestamp;
    /**
     * This is the class for a single transaction
     * Transaction type
     * 0 = Expense
     * 1 = Income
     * 2 = Transfer to
     * 3 = Transfer from
     */
    private final int transactionNumber;

    Transaction(int transactionNumber, int transactionAccountNumber, double transactionAmount, int transactionType, String transactionJournalEntry, long transactionTimestamp) {
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
