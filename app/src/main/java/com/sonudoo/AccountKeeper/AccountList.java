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
    public boolean addAccount(String name, String desc, double initialBalance){

        /* TODO: Optimize the search */
        for(int i=0;i<accountList.size();i++){
            if(accountList.get(i).accountName.compareTo(name) == 0){
                return false;
            }
        }
        accountList.add(new Account(name, desc, initialBalance));
        return true;
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
