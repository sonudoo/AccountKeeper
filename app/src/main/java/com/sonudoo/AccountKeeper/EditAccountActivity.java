package com.sonudoo.AccountKeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditAccountActivity extends AppCompatActivity {
    public static final String ACCOUNT_NUMBER = "com.sonudoo.AccountKeeper.ACCOUNT_NUMBER";

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        account = intent.getParcelableExtra(ACCOUNT_NUMBER);
        final EditText editAccountName = (EditText) findViewById(R.id.edit_account_name);
        final EditText editAccountDesc = (EditText) findViewById(R.id.edit_account_desc);
        editAccountName.setText(account.accountName);
        editAccountDesc.setText(account.accountDesc);

        final AccountList accountList = AccountList.getInstance();
        Button editButton = (Button) findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                account.accountName = editAccountName.getText().toString();
                account.accountDesc = editAccountDesc.getText().toString();
                Intent i = new Intent(EditAccountActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

}
