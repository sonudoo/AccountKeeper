package com.sonudoo.AccountKeeper;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class AddTransactionActivity extends AppCompatActivity {
    /**
     * This activity is used for adding a new transaction
     */
    private Spinner addTransactionAccount;
    private EditText addTransactionAmount;
    private RadioGroup addTransactionType;
    private EditText addTransactionJournalEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Toolbar toolbar =
                findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                AccountList.getInstance(this).getAccounts());
        addTransactionAccount =
                findViewById(R.id.add_transaction_activity_transaction_account);
        addTransactionAmount =
                findViewById(R.id.add_transaction_activity_transaction_amount);
        addTransactionType =
                findViewById(R.id.add_transaction_activity_transaction_type);
        addTransactionJournalEntry =
                findViewById(R.id.add_transaction_activity_transaction_journal_entry);
        addTransactionAccount.setAdapter(arrayAdapter);

        Button addTransactionButton =
                findViewById(R.id.add_transaction_activity_transaction_button);
        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  Transaction amount must be at least 0.01.
                  Journal entry must not be empty.
                  One of expense or income must be selected.
                 */
                double addTransactionAmount;
                try {
                    addTransactionAmount =
                            Double.parseDouble(AddTransactionActivity.this.addTransactionAmount.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(AddTransactionActivity.this, "Invalid " +
                            "amount entered", Toast.LENGTH_LONG).show();
                    return;
                }
                String journalEntry = addTransactionJournalEntry.getText().toString();
                int id = addTransactionType.getCheckedRadioButtonId();
                if (journalEntry.compareTo("") == 0) {
                    Toast.makeText(AddTransactionActivity.this, "Journal " +
                            "entry must not be empty", Toast.LENGTH_LONG).show();
                } else if (id == -1) {
                    Toast.makeText(AddTransactionActivity.this, "Please " +
                            "select if the transaction is an Expense or " +
                            "Income", Toast.LENGTH_LONG).show();
                } else if (addTransactionAmount < 0.01) {
                    Toast.makeText(AddTransactionActivity.this, "Invalid " +
                            "amount entered.  Minimum transaction amount is " + "0" + ".01", Toast.LENGTH_LONG).show();
                } else {
                    RadioButton radioButton = findViewById(id);
                    int type = 0;
                    if (radioButton.getText().toString().equals("Income"))
                        type = 1;
                    if (TransactionList.getInstance(AddTransactionActivity.this).addTransaction((int) addTransactionAccount.getSelectedItemId() + 1, addTransactionAmount, type, "Being " + journalEntry)) {
                        Toast.makeText(AddTransactionActivity.this,
                                "Transaction successful", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(AddTransactionActivity.this,
                                "Transaction unsuccessful. Ensure that the " + "transaction amount is less than " + "balance.", Toast.LENGTH_LONG).show();
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

}
