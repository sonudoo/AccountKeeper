package com.sonudoo.AccountKeeper;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class AddAccountActivity extends AppCompatActivity {
    /**
     * This activity is used for adding an account.
     */
    private boolean addAccountSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar =
                findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);
        final AccountList accountList = AccountList.getInstance(this);
        final TransactionList transactionList =
                TransactionList.getInstance(this);
        Button addButton = findViewById(R.id.add_account_activity_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  Account name must not be empty.
                  Account name must not clash.
                  Initial balance must be at least 0.01.
                 */
                EditText addAccountName =
                        findViewById(R.id.add_acount_activity_account_name);
                EditText addAccountDesc =
                        findViewById(R.id.add_account_activity_account_description);
                EditText addAccountInitialBalance =
                        findViewById(R.id.add_account_activity_account_initial_balance);
                if (addAccountName.getText().toString().compareTo("") == 0) {
                    Toast.makeText(AddAccountActivity.this,
                            "Account name " + "must not be empty",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        double amount =
                                Double.parseDouble(addAccountInitialBalance.getText().toString());
                        if (amount < 0.01) {
                            Toast.makeText(AddAccountActivity.this, "Invalid "
                                    + "amount entered. It must be greater " + "than " + "0.01", Toast.LENGTH_LONG).show();
                        } else {
                            Account newAccount =
                                    accountList.addAccount(addAccountName.getText().toString().toUpperCase(), addAccountDesc.getText().toString());
                            if (newAccount == null) {
                                Toast.makeText(AddAccountActivity.this,
                                        "Account name is already taken",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                transactionList.addTransaction(newAccount.accountNumber, amount, 1, "Being Initial amount added");
                                addAccountSuccess = true;
                                finish();
                            }
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(AddAccountActivity.this, "Invalid " +
                                "initial amount entered", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(AddAccountActivity.this,
                                "An unknown " + "error occurred",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!addAccountSuccess)
            Toast.makeText(AddAccountActivity.this, "Account not added",
                    Toast.LENGTH_LONG).show();
    }
}
