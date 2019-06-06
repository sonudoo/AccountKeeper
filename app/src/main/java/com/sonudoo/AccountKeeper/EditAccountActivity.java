package com.sonudoo.AccountKeeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class EditAccountActivity extends AppCompatActivity {
    /**
     * This activity is used for editing account detail.
     * Unlike other activities, this activity saves state.
     */

    public static final String ACCOUNT_NUMBER =
            "com.sonudoo.AccountKeeper" + ".EditAccountActivity.ACCOUNT_NUMBER";
    private static final int DEFAULT_VALUE = -1;
    private int accountNumber;
    private EditText editAccountName;
    private EditText editAccountDesc;
    private String savedAccountName;
    private String savedAccountDesc;
    private AccountList accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Toolbar toolbar =
                findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);
        Intent receivedIntent = getIntent();
        accountNumber =
                receivedIntent.getIntExtra(com.sonudoo.AccountKeeper.EditAccountActivity.ACCOUNT_NUMBER, DEFAULT_VALUE);

        if (accountNumber == -1) {
            Toast.makeText(this, "Invalid selection", Toast.LENGTH_LONG).show();
            finish();
        }

        editAccountName = findViewById(R.id.edit_account_activity_account_name);
        editAccountDesc = findViewById(R.id.edit_account_activity_account_desc);

        if (savedInstanceState == null) {
            editAccountName.setText(AccountList.getInstance(this).getAccount(accountNumber).accountName);
            editAccountDesc.setText(AccountList.getInstance(this).getAccount(accountNumber).accountDesc);
        } else {
            editAccountName.setText(savedInstanceState.getString(savedAccountName));
            editAccountDesc.setText(savedInstanceState.getString(savedAccountDesc));
        }

        accountList = AccountList.getInstance(this);

        Button editButton = findViewById(R.id.edit_account_activity_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  Account name must be unique and non-empty.
                 */
                if (editAccountName.getText().toString().compareTo("") == 0) {
                    Toast.makeText(EditAccountActivity.this,
                            "Account name " + "must not be empty",
                            Toast.LENGTH_LONG).show();
                } else if (!accountList.updateAccount(accountNumber,
                        editAccountName.getText().toString().toUpperCase(),
                        editAccountDesc.getText().toString())) {
                    Toast.makeText(EditAccountActivity.this,
                            "Account name " + "clashes with another. Please " + "choose a different " + "name", Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }

            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        AccountList.getInstance(this).getAccount(accountNumber).accountName =
                editAccountName.getText().toString();
        AccountList.getInstance(this).getAccount(accountNumber).accountDesc =
                editAccountDesc.getText().toString();
    }

    @Override
    public void onSaveInstanceState(Bundle outState,
                                    PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(savedAccountName,
                editAccountName.getText().toString());
        outState.putString(savedAccountDesc,
                editAccountDesc.getText().toString());
    }
}
