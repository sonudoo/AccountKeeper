package com.sonudoo.AccountKeeper;

import android.database.Cursor;

import java.util.ArrayList;

public class AccountList {
    private static AccountList singleton_instance = null;
    private ArrayList <Account> accountList;
    private DatabaseHandler dh;
    private AccountList(){
        accountList = new ArrayList<Account>();
    }

    private AccountList(DatabaseHandler dh) {
        this();
        Cursor cursor = dh.getAccounts();
        while (cursor.moveToNext()) {
            Account account = new Account(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getDouble(3));
            accountList.add(account);
        }
        this.dh = dh;
    }
    public static AccountList getInstance(){
        if(AccountList.singleton_instance == null) {
            singleton_instance = new AccountList();
        }
        return singleton_instance;
    }

    public static AccountList getInstance(DatabaseHandler dh) {
        if (AccountList.singleton_instance == null) {
            singleton_instance = new AccountList(dh);
        }
        return singleton_instance;
    }

    public Account addAccount(String name, String desc) {
        Account newAccount = new Account(accountList.size() + 1, name, desc);
        accountList.add(newAccount);
        dh.addAccount(accountList.size(), name, desc);
        return newAccount;
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

    public void updateAccount(int accountNumber, String newName, String newDesc) {
        accountList.get(accountNumber - 1).accountName = newName;
        accountList.get(accountNumber - 1).accountDesc = newDesc;
        dh.updateAccount(accountNumber, newName, newDesc);
    }
}
