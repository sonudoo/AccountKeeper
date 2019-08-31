package com.sonudoo.AccountKeeper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest {
    public Account account;

    @Before
    public void setUp() {
        account = new Account(0, "Test", "Sample");
    }

    @Test
    public void getBalance() {
        assertEquals(0.00, account.getBalance(), 1e-6);
        account.deposit(10.00);
        assertEquals(10.00, account.getBalance(), 1e-6);
        account.withdraw(4);
        assertEquals(6.00, account.getBalance(), 1e-6);
    }

    @Test
    public void deposit() {
        account.deposit(20.00);
        assertEquals(20.00, account.getBalance(), 1e-6);
        account.deposit(10.00);
        assertEquals(30.00, account.getBalance(), 1e-6);
        account.deposit(40.00);
        assertEquals(70.00, account.getBalance(), 1e-6);
    }

    @Test
    public void withdraw() {
        account.deposit(100.00);
        assertEquals(true, account.withdraw(20.00));
        assertEquals(80.00, account.getBalance(), 1e-6);
        assertEquals(false, account.withdraw(100.00));
        assertEquals(80.00, account.getBalance(), 1e-6);
    }
}