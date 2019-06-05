package com.sonudoo.AccountKeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AccountListAdapter accountListAdapter;
    private AccountList accountListInstance;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(new BaseFragment());
                    return true;
                case R.id.navigation_dashboard:
                    showFragment(new TransactionListFragment());
                    return true;
            }
            return false;
        }
    };
    private TransactionList transactionListInstance;

    protected void showFragment(Fragment fragment) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.main_activity_frame_layout, fragment);
                ft.commit();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        DatabaseHandler db = new DatabaseHandler(this);

        accountListInstance = AccountList.getInstance(db);
        transactionListInstance = TransactionList.getInstance(db);
        BottomNavigationView navView = findViewById(R.id.main_activity_bottom_navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showFragment(new BaseFragment());
        FloatingActionButton addTransactionButton = (FloatingActionButton) findViewById(R.id.main_activity_add_transaction_button);
        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountListInstance.getAccounts().size() == 0) {
                    Toast.makeText(MainActivity.this, "At least one account is needed to transact", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(MainActivity.this, AddTransactionActivity.class);
                    startActivity(i);
                }
            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        addTransactionButton.bringToFront();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "Download Account Keeper today.");
            startActivity(i);
            return true;
        } else if (id == R.id.exit) finish();
        else if (id == R.id.add_account_menu_item) {
            Intent i = new Intent(MainActivity.this, AddAccountActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static class BaseFragment extends Fragment {
        private AccountListAdapter accountListAdapter;
        private AccountList accountListInstance;
        private View view;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.content_home, container, false);
            RecyclerView accountList = (RecyclerView) view.findViewById(R.id.main_activity_account_list);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
            accountList.setLayoutManager(gridLayoutManager);
            accountListInstance = AccountList.getInstance();
            accountListAdapter = new AccountListAdapter(getActivity(), accountListInstance.getAccounts());
            accountList.setAdapter(accountListAdapter);
            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            accountListAdapter.notifyDataSetChanged();
            TextView balanceText = (TextView) view.findViewById(R.id.main_activity_balance_text);
            balanceText.setText("₹ " + Double.toString(accountListInstance.getTotalBalance()));
            TextView noAccountsText = (TextView) view.findViewById(R.id.main_activity_no_account_text);
            if (accountListInstance.getAccounts().size() == 0) {
                noAccountsText.setVisibility(View.VISIBLE);
            } else {
                noAccountsText.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static class TransactionListFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.content_list_transactions, container, false);
            RecyclerView transactionList = (RecyclerView) view.findViewById(R.id.main_activity_transaction_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            transactionList.setLayoutManager(layoutManager);
            TransactionListAdapter transactionListAdapter = new TransactionListAdapter(getActivity(), TransactionList.getInstance().getTransactions());
            transactionList.setAdapter(transactionListAdapter);
            return view;
        }
    }
}
