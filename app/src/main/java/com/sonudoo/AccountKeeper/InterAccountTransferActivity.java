package com.sonudoo.AccountKeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class InterAccountTransferActivity extends AppCompatActivity {
    public static final String ACCOUNT_NUMBER = "com.sonudoo.AccountKeeper.InterAccountTransferActivity";
    private Spinner spinner1;
    private Spinner spinner2;
    private EditText interBankTransferAmount;
    private TransactionList transactionListInstance;
    private AccountList accountListInstance;
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_account_transfer);
        Toolbar toolbar = findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);

        accountListInstance = AccountList.getInstance();
        transactionListInstance = TransactionList.getInstance();
        Intent intent = getIntent();
        mAccount = intent.getParcelableExtra(ACCOUNT_NUMBER);
        ArrayAdapter<Account> arrayAdapter =
                new ArrayAdapter<Account>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        accountListInstance.getAccounts());
        spinner1 = (Spinner) findViewById(R.id.inter_account_transfer_activity_inter_account_transfer_account_1);
        spinner2 = (Spinner) findViewById(R.id.inter_account_tranfer_activity_inter_account_transfer_account_2);
        interBankTransferAmount = (EditText) findViewById(R.id.inter_account_transfer_amount);
        spinner1.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter);
        spinner1.setSelection(mAccount.accountNumber - 1);
        Button interBankTransferButton = (Button) findViewById(R.id.inter_account_transfer_activity_inter_account_transfer_button);
        interBankTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner1.getSelectedItem() == null) {
                    Toast.makeText(InterAccountTransferActivity.this, "Please select the account to transfer from", Toast.LENGTH_LONG).show();
                } else if (spinner2.getSelectedItem() == null) {
                    Toast.makeText(InterAccountTransferActivity.this, "Please select the account to transfer to", Toast.LENGTH_LONG).show();
                } else if (spinner1.getSelectedItemId() == spinner2.getSelectedItemId()) {
                    Toast.makeText(InterAccountTransferActivity.this, "The two accounts must be different", Toast.LENGTH_LONG).show();
                } else {
                    long fromAccountIndex = spinner1.getSelectedItemId();
                    long toAccountIndex = spinner2.getSelectedItemId();
                    try {
                        double amount = Double.parseDouble(interBankTransferAmount.getText().toString());
                        if (amount < 0.01) {
                            Toast.makeText(InterAccountTransferActivity.this, "Invalid amount entered", Toast.LENGTH_LONG).show();
                        } else {
                            Account fromAccount = accountListInstance.getAccounts().get((int) fromAccountIndex);
                            Account toAccount = accountListInstance.getAccounts().get((int) toAccountIndex);
                            if (transactionListInstance.addTransaction(fromAccount.accountNumber, amount, 0, "Being Amount transferred to " + toAccount.accountName + " Account") == true) {
                                transactionListInstance.addTransaction(toAccount.accountNumber, amount, 1, "Being Amount transferred from " + fromAccount.accountName + " Account");
                                Toast.makeText(InterAccountTransferActivity.this, "Transfer Successful", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(InterAccountTransferActivity.this, "Cannot withdraw more than " + accountListInstance.getAccounts().get((int) fromAccountIndex).getBalance(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(InterAccountTransferActivity.this, "Invalid amount entered", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

}
