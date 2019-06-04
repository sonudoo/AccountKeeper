package com.sonudoo.AccountKeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddTransactionActivity extends AppCompatActivity {

    private Spinner addAccount;
    private EditText addTransactionAmount;
    private RadioGroup addTransactionType;
    private EditText addTransactionJournalEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Toolbar toolbar = findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_dropdown_item, AccountList.getInstance().getAccounts());
        addAccount = (Spinner) findViewById(R.id.add_transaction_activity_transaction_account);
        addTransactionAmount = (EditText) findViewById(R.id.add_transaction_activity_transaction_amount);
        addTransactionType = (RadioGroup) findViewById(R.id.add_transaction_activity_transaction_type);
        addTransactionJournalEntry = (EditText) findViewById(R.id.add_transaction_activity_transaction_journal_entry);
        addAccount.setAdapter(arrayAdapter);

        Button addTransactionButton = (Button) findViewById(R.id.add_transaction_activity_transaction_button);
        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long addAccountIndex = addAccount.getSelectedItemId();
                Account account = AccountList.getInstance().getAccounts().get((int) addAccountIndex);
                double amount = Double.parseDouble(addTransactionAmount.getText().toString());
                String journalEntry = addTransactionJournalEntry.getText().toString();
                int id = addTransactionType.getCheckedRadioButtonId();
                if (id == -1) {
                    Toast.makeText(AddTransactionActivity.this, "Please select if the transaction is an Expense or Income", Toast.LENGTH_LONG).show();
                } else {
                    RadioButton radioButton = (RadioButton) findViewById(id);
                    boolean type = false;
                    if (radioButton.getText().toString().equals("Income"))
                        type = true;
                    if (TransactionList.getInstance().addTransaction(account, amount, type, "Being " + journalEntry)) {
                        Toast.makeText(AddTransactionActivity.this, "Transaction successful", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(AddTransactionActivity.this, "Transaction unsuccessful. Ensure that the transaction amount is less than balance.", Toast.LENGTH_LONG).show();
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

}
