package com.sonudoo.AccountKeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHandler extends SQLiteOpenHelper {
    /**
     * This class handles all the database related operations.
     */

    private final static double VERSION_NUMBER = 1.0;
    private final static String DATABASE_NAME = "accountkeeper";
    private final static String TABLE_ACCOUNTS = "accounts";
    private final static String TABLE_TRANSACTIONS = "transactions";

    private final static String ACCOUNT_ID = "id";
    private final static String ACCOUNT_NAME = "name";
    private final static String ACCOUNT_DESC = "desc";
    private final static String ACCOUNT_BALANCE = "balance";

    private final static String TRANSACTION_ID = "id";
    private final static String TRANSACTION_ACCOUNT_ID = "account_id";
    private final static String TRANSACTION_AMOUNT = "amount";
    private final static String TRANSACTION_TYPE = "type";
    private final static String TRANSACTION_JOURNAL_ENTRY = "journal_entry";
    private final static String TRANSACTION_TIMESTAMP = "timestamp";
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, (int) VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + " (" +
                ACCOUNT_ID + " INTEGER PRIMARY KEY," +
                ACCOUNT_NAME + " TEXT, " +
                ACCOUNT_DESC + " TEXT, " +
                ACCOUNT_BALANCE + " REAL" +
                ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                TRANSACTION_ID + " INTEGER PRIMARY KEY," +
                TRANSACTION_ACCOUNT_ID + " INTEGER REFERENCES " + TABLE_ACCOUNTS + "(" + ACCOUNT_ID + ")," +
                TRANSACTION_AMOUNT + " REAL," +
                TRANSACTION_TYPE + " INTEGER," +
                TRANSACTION_JOURNAL_ENTRY + " TEXT," +
                TRANSACTION_TIMESTAMP + " INTEGER" +
                ")";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAccounts() {
        readableDatabase = getReadableDatabase();
        return readableDatabase.query(TABLE_ACCOUNTS, new String[]{ACCOUNT_ID
                        , ACCOUNT_NAME, ACCOUNT_DESC, ACCOUNT_BALANCE},
                "", new String[]{}, null, null, null, null);
    }

    public void updateAccount(int accountNumber, String newName, String newDesc) {
        writableDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_NAME, newName);
        cv.put(ACCOUNT_DESC, newDesc);
        writableDatabase.update(TABLE_ACCOUNTS, cv, ACCOUNT_ID + " = ?", new String[]{Integer.toString(accountNumber)});
    }

    public void updateAccount(int accountNumber, double deltaAmount) {
        writableDatabase = getWritableDatabase();
        String strSQL = "UPDATE " + TABLE_ACCOUNTS + " SET " + ACCOUNT_BALANCE + " = " +
                ACCOUNT_BALANCE + " + " + deltaAmount + " WHERE " + ACCOUNT_ID + " = " + accountNumber;
        writableDatabase.execSQL(strSQL);
    }

    public Cursor getTransactions() {
        readableDatabase = getReadableDatabase();
        return readableDatabase.query(TABLE_TRANSACTIONS,
                new String[]{TRANSACTION_ID,
                        TRANSACTION_ACCOUNT_ID, TRANSACTION_AMOUNT, TRANSACTION_TYPE,
                        TRANSACTION_JOURNAL_ENTRY, TRANSACTION_TIMESTAMP},
                "", new String[]{}, null, null, null, null);
    }

    public void addAccount(int accountNumber, String accountName, String accountDesc) {
        writableDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_ID, accountNumber);
        cv.put(ACCOUNT_NAME, accountName);
        cv.put(ACCOUNT_DESC, accountDesc);
        cv.put(ACCOUNT_BALANCE, 0.0);
        writableDatabase.insert(TABLE_ACCOUNTS, null, cv);
    }

    public void addTransaction(int transactionNumber, int transactionAccountNumber, double transactionAmount,
                               int transactionType, String transactionJournalEntry, long transactionTimestamp) {
        writableDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRANSACTION_ID, transactionNumber);
        cv.put(TRANSACTION_ACCOUNT_ID, transactionAccountNumber);
        cv.put(TRANSACTION_AMOUNT, transactionAmount);
        cv.put(TRANSACTION_TYPE, transactionType);
        cv.put(TRANSACTION_JOURNAL_ENTRY, transactionJournalEntry);
        cv.put(TRANSACTION_TIMESTAMP, transactionTimestamp);
        writableDatabase.insert(TABLE_TRANSACTIONS, null, cv);
    }

    public void cleanAccountDatabase() {
        writableDatabase = getWritableDatabase();
        String TRUNCATE_ACCOUNTS_TABLE = "DELETE FROM " + TABLE_ACCOUNTS;
        writableDatabase.execSQL(TRUNCATE_ACCOUNTS_TABLE);
    }

    public void cleanTransactionDatabase() {
        writableDatabase = getWritableDatabase();
        String TRUNCATE_TRANSACTIONS_TABLE =
                "DELETE FROM " + TABLE_TRANSACTIONS;
        writableDatabase.execSQL(TRUNCATE_TRANSACTIONS_TABLE);
    }
}
