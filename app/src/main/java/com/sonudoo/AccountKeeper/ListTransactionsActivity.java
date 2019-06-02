package com.sonudoo.AccountKeeper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListTransactionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView transactionList = (RecyclerView) findViewById(R.id.transaction_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        transactionList.setLayoutManager(layoutManager);
        TransactionListAdapter transactionListAdapter = new TransactionListAdapter(this, TransactionList.getInstance().getTransactions());
        transactionList.setAdapter(transactionListAdapter);
    }

}
