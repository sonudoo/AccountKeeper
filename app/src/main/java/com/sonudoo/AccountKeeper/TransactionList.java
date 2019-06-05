package com.sonudoo.AccountKeeper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

public class TransactionList {
    private static TransactionList singleton_instance = null;
    private ArrayList <Transaction> transactionList;
    private DatabaseHandler dh;

    private TransactionList(){
        transactionList = new ArrayList<Transaction>();
    }

    private TransactionList(DatabaseHandler dh) {
        this();
        Cursor cursor = dh.getTransactions();
        while (cursor.moveToNext()) {
            Transaction transaction = new Transaction(cursor.getInt(0),
                    cursor.getInt(1), cursor.getDouble(2),
                    cursor.getInt(3), cursor.getString(4),
                    cursor.getLong(5));
            transactionList.add(transaction);
        }
        this.dh = dh;
    }
    public static TransactionList getInstance(){
        if(TransactionList.singleton_instance == null) {
            singleton_instance = new TransactionList();
        }
        return singleton_instance;
    }

    public static TransactionList getInstance(DatabaseHandler dh) {
        if (TransactionList.singleton_instance == null) {
            singleton_instance = new TransactionList(dh);
        }
        return singleton_instance;
    }

    public boolean addTransaction(int accountNumber, double transactionAmount,
                                  int transactionType, String transactionJournalEntry) {
        if (transactionType == 0) {
            if (AccountList.getInstance().getAccount(accountNumber).withdraw(transactionAmount) == true) {
                long currentTimestamp = new Date().getTime();
                dh.addTransaction(transactionList.size(), accountNumber, transactionAmount,
                        transactionType, transactionJournalEntry, currentTimestamp);
                transactionList.add(new Transaction(transactionList.size() + 1,
                        accountNumber, transactionAmount, transactionType, transactionJournalEntry,
                        currentTimestamp));
                dh.updateAccount(accountNumber, -transactionAmount);
                return true;
            } else {
                return false;
            }
        } else {
            AccountList.getInstance().getAccount(accountNumber).deposit(transactionAmount);
            long currentTimestamp = new Date().getTime();
            dh.addTransaction(transactionList.size(), accountNumber, transactionAmount,
                    transactionType, transactionJournalEntry, currentTimestamp);
            transactionList.add(new Transaction(transactionList.size() + 1,
                    accountNumber, transactionAmount, transactionType, transactionJournalEntry,
                    currentTimestamp));
            dh.updateAccount(accountNumber, transactionAmount);
            return true;
        }
    }

    public ArrayList<Transaction> getTransactions() {
        return transactionList;
    }
}
