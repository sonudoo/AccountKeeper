package com.sonudoo.AccountKeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AccountListAdapter accountListAdapter;
    private AccountList accountListInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        accountListInstance = AccountList.getInstance();

        RecyclerView accountList = (RecyclerView) findViewById(R.id.account_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        accountList.setLayoutManager(gridLayoutManager);
        accountListAdapter = new AccountListAdapter(this, accountListInstance.getAccounts());
        accountList.setAdapter(accountListAdapter);

        FloatingActionButton newAccountButton = (FloatingActionButton) findViewById(R.id.add_account_button);
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddAccountActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton interAccountTransferButton = (FloatingActionButton) findViewById(R.id.inter_account_transfer_button);
        interAccountTransferButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InterAccountTransferActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton transactionListButton = (FloatingActionButton) findViewById(R.id.transaction_list_button);
        transactionListButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListTransactionsActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton addTransactionButton = (FloatingActionButton) findViewById(R.id.add_transaction_button_1);
        addTransactionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "Download AccountKeeper today.");
            startActivity(i);
            return true;
        } else if (id == R.id.exit) finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        accountListAdapter.notifyDataSetChanged();
        TextView balanceText = (TextView) findViewById(R.id.balance_text);
        balanceText.setText("Balance: Rs. " + Double.toString(accountListInstance.getTotalBalance()));
        TextView noAccountsText = (TextView) findViewById(R.id.no_account_text);
        if (accountListInstance.getAccounts().size() == 0) {
            noAccountsText.setVisibility(View.VISIBLE);
        } else {
            noAccountsText.setVisibility(View.INVISIBLE);
        }
    }
}
