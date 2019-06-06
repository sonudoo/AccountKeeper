package com.sonudoo.AccountKeeper;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    /**
     * This is a parcelable class representing a single account.
     */
    public static final Parcelable.Creator<Integer> CREATOR =
            new Parcelable.Creator<Integer>() {

        @Override
        public Integer createFromParcel(Parcel source) {
            return source.readInt();
        }

        @Override
        public Integer[] newArray(int size) {
            return new Integer[size];
        }
    };
    public final int accountNumber;
    public String accountName;
    public String accountDesc;
    private double accountBalance;

    Account(int accountNumber, String accountName, String accountDesc) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountDesc = accountDesc;
        this.accountBalance = 0;
    }

    Account(int accountNumber, String accountName, String accountDesc,
            double accountBalance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountDesc = accountDesc;
        this.accountBalance = accountBalance;
    }

    public double getBalance() {
        return accountBalance;
    }

    public void deposit(double amount) {
        accountBalance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > accountBalance) {
            return false;
        } else {
            accountBalance -= amount;
            return true;
        }
    }

    public String toString() {
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
}
