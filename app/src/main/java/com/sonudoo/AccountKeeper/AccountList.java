package com.sonudoo.AccountKeeper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashSet;

public class AccountList {
    private static AccountList singleton_instance = null;
    private ArrayList <Account> accountList;
    private HashSet<String> accountNames;
    private DatabaseHandler dh;
    private AccountList(){
        accountList = new ArrayList<Account>();
    }

    private AccountList(DatabaseHandler dh) {
        this();
        accountNames = new HashSet<String>();
        Cursor cursor = dh.getAccounts();
        while (cursor.moveToNext()) {
            accountNames.add(cursor.getString(1));
            Account account = new Account(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getDouble(3));
            accountList.add(account);
        }
        this.dh = dh;
    }
    public static AccountList getInstance(){
        return singleton_instance;
    }

    public static AccountList getInstance(DatabaseHandler dh) {
        if (AccountList.singleton_instance == null) {
            singleton_instance = new AccountList(dh);
        }
        return singleton_instance;
    }

    public boolean exists(String name) {
        return this.accountNames.contains(name);
    }

    public Account addAccount(String accountName, String accountDesc) {
        if (exists(accountName) == true) {
            return null;
        } else {
            Account newAccount = new Account(accountList.size() + 1, accountName, accountDesc);
            accountNames.add(accountName);
            accountList.add(newAccount);
            dh.addAccount(accountList.size(), accountName, accountDesc);
            return newAccount;
        }
    }

    public double getTotalBalance() {
        double total = 0;
        for (int i = 0; i < accountList.size(); i++) {
            total += accountList.get(i).getBalance();
        }
        return total;
    }
    public ArrayList <Account> getAccounts(){
        return accountList;
    }
    public Account getAccount(int accountNumber){
        return accountList.get(accountNumber-1);
    }

    public boolean updateAccount(int accountNumber, String newName, String newDesc) {
        if (newName.compareTo(accountList.get(accountNumber - 1).accountName) != 0) {
            if (exists(newName) == true) {
                return false;
            } else {
                accountNames.remove(accountList.get(accountNumber - 1).accountName);
                accountNames.add(newName);
            }
        }
        accountList.get(accountNumber - 1).accountName = newName;
        accountList.get(accountNumber - 1).accountDesc = newDesc;
        dh.updateAccount(accountNumber, newName, newDesc);
        return true;
    }
}
