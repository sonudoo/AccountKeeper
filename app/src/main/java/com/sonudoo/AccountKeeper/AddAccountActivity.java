package com.sonudoo.AccountKeeper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddAccountActivity extends AppCompatActivity {
    private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar = findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);
        final AccountList accountList = AccountList.getInstance();
        final TransactionList transactionList = TransactionList.getInstance();
        Button addButton = (Button) findViewById(R.id.add_account_activity_add_button);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText addAccountName = (EditText) findViewById(R.id.add_acount_activity_account_name);
                EditText addAccountDesc = (EditText) findViewById(R.id.add_account_activity_account_description);
                EditText addAccountInitialBalance = (EditText) findViewById(R.id.add_account_activity_account_initial_balance);
                if (addAccountName.getText().toString().compareTo("") == 0) {
                    Toast.makeText(AddAccountActivity.this, "Account name must not be empty", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        double amount = Double.parseDouble(addAccountInitialBalance.getText().toString());
                        if (amount < 0.01) {
                            Toast.makeText(AddAccountActivity.this, "Invalid amount entered. It must be greater than 0.01", Toast.LENGTH_LONG).show();
                        } else {
                            Account newAccount = accountList.addAccount(addAccountName.getText().toString().toUpperCase(), addAccountDesc.getText().toString());
                            if (newAccount == null) {
                                Toast.makeText(AddAccountActivity.this, "Account name is already taken", Toast.LENGTH_LONG).show();
                            } else {
                                transactionList.addTransaction(newAccount.accountNumber, amount, 1, "Being Initial amount added");
                                success = true;
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(AddAccountActivity.this, "Invalid initial amount entered", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (success == false)
            Toast.makeText(AddAccountActivity.this, "Account not added", Toast.LENGTH_LONG).show();
    }
}
