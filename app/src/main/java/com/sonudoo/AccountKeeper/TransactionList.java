package com.sonudoo.AccountKeeper;

import java.util.ArrayList;

public class TransactionList {
    private static TransactionList singleton_instance = null;
    private ArrayList <Transaction> transactionList;
    private TransactionList(){
        transactionList = new ArrayList<Transaction>();
    }
    public static TransactionList getInstance(){
        if(TransactionList.singleton_instance == null) {
            singleton_instance = new TransactionList();
        }
        return singleton_instance;
    }
}
