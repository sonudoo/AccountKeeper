package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashSet;

public class AccountList {
    /**
     * This is a singleton class representing a list of accounts.
     */
    private static AccountList singleton_instance = null;
    private final ArrayList<Account> accountList;
    private final HashSet<String> accountNames;
    private final DatabaseHandler databaseHandler;

    private AccountList(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        accountList = new ArrayList<>();
        accountNames = new HashSet<>();
        Cursor cursor = databaseHandler.getAccounts();
        while (cursor.moveToNext()) {
            accountNames.add(cursor.getString(1));
            Account account = new Account(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getDouble(3));
            accountList.add(account);
        }
        this.databaseHandler = databaseHandler;
    }

    public static AccountList getInstance(Context context) {
        /*
          This method returns the singleton instance of the class.
         */
        if (AccountList.singleton_instance == null) {
            singleton_instance = new AccountList(context);
        }
        return singleton_instance;
    }

    private boolean exists(String name) {
        /*
            This method checks if the account name exists in the account list.
         */
        return this.accountNames.contains(name);
    }

    public Account addAccount(String accountName, String accountDesc) {
        /*
          This method adds a new account and ensures non-duplicate names.
         */
        if (exists(accountName)) {
            return null;
        } else {
            Account newAccount = new Account(accountList.size() + 1,
                    accountName, accountDesc);
            accountNames.add(accountName);
            accountList.add(newAccount);
            databaseHandler.addAccount(accountList.size(), accountName,
                    accountDesc);
            return newAccount;
        }
    }

    public double getTotalBalance() {
        /*
            This method returns the total balance from all the accounts.
         */
        double total = 0;
        for (int i = 0; i < accountList.size(); i++) {
            total += accountList.get(i).getBalance();
        }
        return total;
    }

    public ArrayList<Account> getAccounts() {
        /*
            This method returns the account list.
         */
        return accountList;
    }

    public Account getAccount(int accountNumber) {
        /*
            This method returns a single account by account number.
         */
        return accountList.get(accountNumber - 1);
    }

    public boolean updateAccount(int accountNumber, String newName,
                                 String newDesc) {
        /*
          This method updates the account name and description of an account.
         */
        if (newName.compareTo(accountList.get(accountNumber - 1).accountName) != 0) {
            if (exists(newName)) {
                return false;
            } else {
                accountNames.remove(accountList.get(accountNumber - 1).accountName);
                accountNames.add(newName);
            }
        }
        accountList.get(accountNumber - 1).accountName = newName;
        accountList.get(accountNumber - 1).accountDesc = newDesc;
        databaseHandler.updateAccount(accountNumber, newName, newDesc);
        return true;
    }

    public void restoreDatabase(Account[] accountsList) {
        /*
            This method restores the database and account list.
         */
        databaseHandler.cleanAccountDatabase();
        this.accountList.clear();
        this.accountNames.clear();
        for (Account account : accountsList) {
            addAccount(account.accountName, account.accountDesc);
        }
    }
}
