package com.sonudoo.AccountKeeper;

import java.util.ArrayList;

public class AccountList {
    private static AccountList singleton_instance = null;
    private ArrayList <Account> accountList;
    private AccountList(){
        accountList = new ArrayList<Account>();
    }
    public static AccountList getInstance(){
        if(AccountList.singleton_instance == null) {
            singleton_instance = new AccountList();
        }
        return singleton_instance;
    }

    public Account addAccount(String name, String desc, double initialBalance) {
        Account newAccount = new Account(name, desc, initialBalance);
        accountList.add(newAccount);
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
}
