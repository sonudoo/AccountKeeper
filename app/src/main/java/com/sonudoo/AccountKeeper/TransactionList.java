package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

class Points {
    final int x;
    final int y;

    Points(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class TransactionList {
    /**
     * This is a singleton class representing a list of transactions.
     */
    private static TransactionList singleton_instance = null;
    private final long DAY_DURATION = 24 * 60 * 60 * 1000;
    private final ArrayList<Transaction> transactionList;
    private final DatabaseHandler databaseHandler;
    private final Context context;

    private TransactionList(Context context) {
        this.context = context;
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        transactionList = new ArrayList<>();
        Cursor cursor = databaseHandler.getTransactions();
        while (cursor.moveToNext()) {
            Transaction transaction = new Transaction(cursor.getInt(0), cursor.getInt(1), cursor.getDouble(2), cursor.getInt(3), cursor.getString(4), cursor.getLong(5));
            transactionList.add(transaction);
        }
        this.databaseHandler = databaseHandler;
    }

    static TransactionList getInstance(Context context) {
        /*
          This method returns a singleton instance of the class
         */
        if (TransactionList.singleton_instance == null) {
            singleton_instance = new TransactionList(context);
        }
        return singleton_instance;
    }

    boolean addTransaction(int accountNumber, double transactionAmount,
                           int transactionType,
                           String transactionJournalEntry) {
        /*
          This method adds a transaction. If the transaction involves a
          withdrawal, then the method checks if the withdrawal is possible.
         */
        if (transactionType == 0 || transactionType == 2) {
            if (AccountList.getInstance(context).getAccount(accountNumber).withdraw(transactionAmount)) {
                long currentTimestamp = new Date().getTime();
                databaseHandler.addTransaction(transactionList.size(),
                        accountNumber, transactionAmount, transactionType,
                        transactionJournalEntry, currentTimestamp);
                transactionList.add(new Transaction(transactionList.size() + 1, accountNumber, transactionAmount, transactionType, transactionJournalEntry, currentTimestamp));
                databaseHandler.updateAccount(accountNumber,
                        -transactionAmount);
                return true;
            } else {
                return false;
            }
        } else {
            AccountList.getInstance(context).getAccount(accountNumber).deposit(transactionAmount);
            long currentTimestamp = new Date().getTime();
            databaseHandler.addTransaction(transactionList.size(),
                    accountNumber, transactionAmount, transactionType,
                    transactionJournalEntry, currentTimestamp);
            transactionList.add(new Transaction(transactionList.size() + 1, accountNumber, transactionAmount, transactionType, transactionJournalEntry, currentTimestamp));
            databaseHandler.updateAccount(accountNumber, transactionAmount);
            return true;
        }
    }

    ArrayList<Transaction> getTransactions() {
        return transactionList;
    }

    private Points getTransactions(long startTimestamp, long endTimestamp) {
        /*
          This method returns two pointers pointing to the first and last
          transaction in the time range specified by startTimestamp and
          endTimestamp. The method performs a binary search since the
          transactionList is already sorted by time
         */
        if (startTimestamp > endTimestamp) {
            long tmp = startTimestamp;
            startTimestamp = endTimestamp;
            endTimestamp = tmp;
        }
        int low = 0;
        int high = transactionList.size() - 1;
        int x = -1;
        int y = -1;
        while (low <= high) {
            int mid = (high + low) >> 1;
            if (transactionList.get(mid).transactionTimestamp >= startTimestamp) {
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
            if (transactionList.get(mid).transactionTimestamp <= endTimestamp) {
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

    private double getExpenditureAmount(long startTimestamp, long endTimestamp) {
        /*
          This methods returns total expenditure between specified time range.
         */
        Points p = getTransactions(startTimestamp, endTimestamp);
        if (p.x == -1 && p.y == -1)
            return 0;
        double total = 0;
        for (int i = p.x; i <= p.y; i++) {
            if (transactionList.get(i).transactionType == 0)
                total += transactionList.get(i).transactionAmount;
        }
        return total;
    }

    double getTodaysExpenditureAmount() {
        /*
          This method returns the total expenditure amount for the day.
         */
        Long time = new Date().getTime();
        Date date = new Date(time - time % (DAY_DURATION));
        return getExpenditureAmount(date.getTime(), date.getTime() + (DAY_DURATION));
    }

    double getYesterdaysExpenditureAmount() {
        /*
          This method returns the total expenditure amount for a day before.
         */
        Long time = new Date().getTime();
        Date date = new Date(time - time % (DAY_DURATION));
        return getExpenditureAmount(date.getTime() - DAY_DURATION, date.getTime());
    }

    private double getIncomeAmount(long startTimestamp, long endTimestamp) {
        /*
          This methods returns total income between specified time range.
         */
        Points p = getTransactions(startTimestamp, endTimestamp);
        if (p.x == -1 && p.y == -1)
            return 0;
        double total = 0;
        for (int i = p.x; i <= p.y; i++) {
            if (transactionList.get(i).transactionType == 1)
                total += transactionList.get(i).transactionAmount;
        }
        return total;
    }

    double getTodaysIncomeAmount() {
        /*
          This method returns the total income amount for a day.
         */
        Long time = new Date().getTime();
        Date date = new Date(time - time % (DAY_DURATION));
        return getIncomeAmount(date.getTime(), date.getTime() + DAY_DURATION);
    }

    ArrayList<Transaction> getFilteredTransactions(long startTimestamp,
                                                   long endTimestamp, long accountNumber, boolean showExpense, boolean showIncome) {
        /*
          This method filters the transaction list on the basis of given
          parameters.
         */
        ArrayList<Transaction> filteredTransactionList = new ArrayList<>();
        int low = 0;
        int high = transactionList.size() - 1;
        if (startTimestamp != -1 && endTimestamp != -1) {
            /*
              If start and end time are set, then get the range of
              transaction corresponding to the time range.
             */
            Points p = getTransactions(startTimestamp, endTimestamp);
            if (p.x == -1 && p.y == -1)
                return filteredTransactionList;
            low = p.x;
            high = p.y;
        }
        for (int i = low; i <= high; i++) {
            if (accountNumber != -1 && transactionList.get(i).transactionAccountNumber != accountNumber)
                continue;
            if (!showExpense && (transactionList.get(i).transactionType == 0 || transactionList.get(i).transactionType == 2))
                continue;
            if (!showIncome && (transactionList.get(i).transactionType == 1 || transactionList.get(i).transactionType == 3))
                continue;
            filteredTransactionList.add(transactionList.get(i));
        }
        return filteredTransactionList;
    }
}
