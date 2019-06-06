package com.sonudoo.AccountKeeper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

class Points {
    int x;
    int y;

    Points(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class TransactionList {
    private final long DAY_DURATION = 24 * 60 * 60 * 1000;
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
        if (transactionType == 0 || transactionType == 2) {
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

    public Points getTransactions(long timestamp1, long timestamp2) {
        if (timestamp1 > timestamp2) {
            long tmp = timestamp1;
            timestamp1 = timestamp2;
            timestamp2 = timestamp1;
        }
        int low = 0;
        int high = transactionList.size() - 1;
        int x = -1;
        int y = -1;
        while (low <= high) {
            int mid = (high + low) >> 1;
            if (transactionList.get(mid).transactionTimestamp >= timestamp1) {
                x = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        if (x == -1) {
            return new Points(-1, -1);
        }

        low = 0;
        high = transactionList.size() - 1;
        while (low <= high) {
            int mid = (high + low) >> 1;
            if (transactionList.get(mid).transactionTimestamp <= timestamp2) {
                y = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (y == -1) {
            return new Points(-1, -1);
        }
        return new Points(x, y);
    }

    public double getExpenditureAmount(long timestamp1, long timestamp2) {

        Points p = getTransactions(timestamp1, timestamp2);
        if (p.x == -1 && p.y == -1) return 0;
        double total = 0;
        for (int i = p.x; i <= p.y; i++) {
            if (transactionList.get(i).transactionType == 0)
                total += transactionList.get(i).transactionAmount;
        }
        return total;
    }

    public double getTodaysExpenditureAmount() {
        Long time = new Date().getTime();
        Date date = new Date(time - time % (DAY_DURATION));
        return getExpenditureAmount(date.getTime(), date.getTime() + (long) (DAY_DURATION));
    }

    public double getYesterdaysExpenditureAmount() {
        Long time = new Date().getTime();
        Date date = new Date(time - time % (DAY_DURATION));
        return getExpenditureAmount(date.getTime() - (long) (DAY_DURATION), date.getTime());
    }

    public double getIncomeAmount(long timestamp1, long timestamp2) {

        Points p = getTransactions(timestamp1, timestamp2);
        if (p.x == -1 && p.y == -1) return 0;
        double total = 0;
        for (int i = p.x; i <= p.y; i++) {
            if (transactionList.get(i).transactionType == 1)
                total += transactionList.get(i).transactionAmount;
        }
        return total;
    }

    public double getTodaysIncomeAmount() {
        Long time = new Date().getTime();
        Date date = new Date(time - time % (DAY_DURATION));
        return getIncomeAmount(date.getTime(), date.getTime() + (long) (DAY_DURATION));
    }

    public ArrayList<Transaction> getFilteredTransactions(long startTimeStamp, long endTimeStamp,
                                                          long accountNumber, boolean expense,
                                                          boolean income) {
        ArrayList<Transaction> filteredTransactionList = new ArrayList<Transaction>();
        int low = 0;
        int high = transactionList.size() - 1;
        if (startTimeStamp != -1 && endTimeStamp != -1) {
            Points p = getTransactions(startTimeStamp, endTimeStamp);
            if (p.x == -1 && p.y == -1) return filteredTransactionList;
            low = p.x;
            high = p.y;
        }
        for (int i = low; i <= high; i++) {
            if (accountNumber != -1 && transactionList.get(i).transactionAccountNumber != accountNumber)
                continue;
            if (expense == false && (transactionList.get(i).transactionType == 0 || transactionList.get(i).transactionType == 2))
                continue;
            if (income == false && (transactionList.get(i).transactionType == 1 || transactionList.get(i).transactionType == 3))
                continue;
            filteredTransactionList.add(transactionList.get(i));
        }
        return filteredTransactionList;
    }
}
