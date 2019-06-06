package com.sonudoo.AccountKeeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditAccountActivity extends AppCompatActivity {
    public static final String ACCOUNT_NUMBER = "com.sonudoo.AccountKeeper.ACCOUNT_NUMBER";
    private Account mAccount;
    private EditText editAccountName;
    private EditText editAccountDesc;
    private String savedAccountName;
    private String savedAccountDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Toolbar toolbar = findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mAccount = intent.getParcelableExtra(ACCOUNT_NUMBER);
        editAccountName = (EditText) findViewById(R.id.edit_account_activity_account_name);
        editAccountDesc = (EditText) findViewById(R.id.edit_account_activity_account_desc);

        if (savedInstanceState == null) {
            editAccountName.setText(mAccount.accountName);
            editAccountDesc.setText(mAccount.accountDesc);
        } else {
            editAccountName.setText(savedInstanceState.getString(savedAccountName));
            editAccountDesc.setText(savedInstanceState.getString(savedAccountDesc));
        }

        final AccountList accountList = AccountList.getInstance();
        Button editButton = (Button) findViewById(R.id.edit_account_activity_button);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (editAccountName.getText().toString().compareTo("") == 0) {
                    Toast.makeText(EditAccountActivity.this, "Account name must not be empty", Toast.LENGTH_LONG).show();
                } else if (accountList.updateAccount(mAccount.accountNumber,
                        editAccountName.getText().toString().toUpperCase(),
                        editAccountDesc.getText().toString()) == false) {
                    Toast.makeText(EditAccountActivity.this, "Account name clashes with another. Please choose a different name", Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAccount.accountName = editAccountName.getText().toString();
        mAccount.accountDesc = editAccountDesc.getText().toString();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(savedAccountName, editAccountName.getText().toString());
        outState.putString(savedAccountDesc, editAccountDesc.getText().toString());
    }
}
