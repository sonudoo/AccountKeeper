package com.sonudoo.AccountKeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class InterAccountTransferActivity extends AppCompatActivity {
    public static final String ACCOUNT_NUMBER =
            "com.sonudoo.AccountKeeper" + ".InterAccountTransferActivity" +
                    ".ACCOUNT_NUMBER";
    private static final int DEFAULT_VALUE = -1;
    private Spinner accountSpinner1;
    private Spinner accountSpinner2;
    private EditText interBankTransferAmount;
    private TransactionList transactionListInstance;
    private AccountList accountListInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_account_transfer);
        Toolbar toolbar =
                findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);

        accountListInstance = AccountList.getInstance(this);
        transactionListInstance = TransactionList.getInstance(this);
        Intent receivedIntent = getIntent();
        int accountNumber = receivedIntent.getIntExtra(ACCOUNT_NUMBER,
                DEFAULT_VALUE);
        if (accountNumber == -1) {
            Toast.makeText(this, "Invalid selection", Toast.LENGTH_LONG).show();
            finish();
        }
        ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                accountListInstance.getAccounts());
        accountSpinner1 =
                findViewById(R.id.inter_account_transfer_activity_inter_account_transfer_account_1);
        accountSpinner2 =
                findViewById(R.id.inter_account_tranfer_activity_inter_account_transfer_account_2);
        interBankTransferAmount =
                findViewById(R.id.inter_account_transfer_amount);
        accountSpinner1.setAdapter(arrayAdapter);
        accountSpinner2.setAdapter(arrayAdapter);
        /*
          Set the spinner 1 to the account of the account number of the
          intent received.
         */
        accountSpinner1.setSelection(accountNumber - 1);
        Button interBankTransferButton =
                findViewById(R.id.inter_account_transfer_activity_inter_account_transfer_button);
        interBankTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  The selected accounts must be different.
                  The transaction amount must be at least 0.01.
                  The maximum transfer amount is equal to the with-drawable
                  balance.
                 */
                if (accountSpinner1.getSelectedItem() == null) {
                    Toast.makeText(InterAccountTransferActivity.this, "Please"
                            + " select the account to transfer from",
                            Toast.LENGTH_LONG).show();
                } else if (accountSpinner2.getSelectedItem() == null) {
                    Toast.makeText(InterAccountTransferActivity.this, "Please"
                            + " select the account to transfer to",
                            Toast.LENGTH_LONG).show();
                } else if (accountSpinner1.getSelectedItemId() == accountSpinner2.getSelectedItemId()) {
                    Toast.makeText(InterAccountTransferActivity.this,
                            "The " + "two accounts must be different",
                            Toast.LENGTH_LONG).show();
                } else {
                    long fromAccountIndex = accountSpinner1.getSelectedItemId();
                    long toAccountIndex = accountSpinner2.getSelectedItemId();
                    try {
                        double amount = Double.parseDouble(interBankTransferAmount.getText().toString());
                        if (amount < 0.01) {
                            Toast.makeText(InterAccountTransferActivity.this,
                                    "Invalid amount entered. Minimum " +
                                            "transfer" + " amount is 0.01",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Account fromAccount =
                                    accountListInstance.getAccounts().get((int) fromAccountIndex);
                            Account toAccount =
                                    accountListInstance.getAccounts().get((int) toAccountIndex);
                            if (transactionListInstance.addTransaction(fromAccount.accountNumber, amount, 2, "Being Amount transferred to " + toAccount.accountName + " Account")) {
                                transactionListInstance.addTransaction(toAccount.accountNumber, amount, 3, "Being Amount transferred from " + fromAccount.accountName + " Account");
                                Toast.makeText(InterAccountTransferActivity.this, "Transfer Successful", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(InterAccountTransferActivity.this, "Cannot withdraw more than " + accountListInstance.getAccounts().get((int) fromAccountIndex).getBalance(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(InterAccountTransferActivity.this,
                                "Invalid amount entered", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

}
