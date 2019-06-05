package com.sonudoo.AccountKeeper;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Account implements Parcelable {
    public int accountNumber;
    public String accountName;
    public String accountDesc;
    private double accountBalance;

    Account(int accountNumber, String accountName, String accountDesc) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountDesc = accountDesc;
        this.accountBalance = 0;
    }

    Account(int accountNumber, String accountName, String accountDesc, double accountBalance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountDesc = accountDesc;
        this.accountBalance = accountBalance;
    }

    public double getBalance(){
        return accountBalance;
    }
    public boolean deposit(double amount){
        accountBalance += amount;
        return true;
    }
    public boolean withdraw(double amount) {
        if (amount > accountBalance) {
            return false;
        } else {
            accountBalance -= amount;
            return true;
        }
    }
    public String toString(){
        return accountName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(accountNumber);
    }

    public static final Parcelable.Creator<Account> CREATOR =
        new Parcelable.Creator<Account>(){

            @Override
            public Account createFromParcel(Parcel source) {
                return AccountList.getInstance().getAccount(source.readInt());
            }

            @Override
            public Account[] newArray(int size) {
                return new Account[size];
            }
        };
}
