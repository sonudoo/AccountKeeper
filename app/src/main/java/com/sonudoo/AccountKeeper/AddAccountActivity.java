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
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final AccountList accountList = AccountList.getInstance();
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText addAccountName = (EditText) findViewById(R.id.add_account_name);
                EditText addAccountDesc = (EditText) findViewById(R.id.add_account_description);
                EditText addAccountInitialBalance = (EditText) findViewById(R.id.add_account_initail_amount);
                accountList.addAccount(addAccountName.getText().toString(), addAccountDesc.getText().toString(), Double.parseDouble(addAccountInitialBalance.getText().toString()));
                Intent i = new Intent(AddAccountActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

}
