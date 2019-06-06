package com.sonudoo.AccountKeeper;

import android.app.DatePickerDialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private AccountList accountListInstance;
    private TransactionList transactionListInstance;
    private boolean startUpExecuted;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(new BaseFragment());
                    filterButton.hide();
                    return true;
                case R.id.navigation_transactions:
                    tlf = new TransactionListFragment();

                    filterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tlf.popupWindow.showAtLocation(tlf.getView(), Gravity.CENTER, 0, 0);
                            Button applyFilterButton = (Button) tlf.popupWindow.getContentView().findViewById(R.id.pop_up_filter_button);
                            applyFilterButton.requestFocus();
                            tlf.popupWindow.setElevation(20);
                        }
                    });
                    filterButton.show();
                    showFragment(tlf);
                    return true;
                case R.id.navigation_accounts:
                    showFragment(new AccountListFragment());
                    filterButton.hide();
            }
            return false;
        }
    };
    private FloatingActionButton filterButton;
    private TransactionListFragment tlf;
    private BottomNavigationView navView;


    protected void showFragment(Fragment fragment) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.main_activity_scroll_view, fragment);
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
        startUpExecuted = false;
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        if (km.isKeyguardSecure()) {
            Intent authIntent = km.createConfirmDeviceCredentialIntent("Authenticate", "Authenticate");
            startActivityForResult(authIntent, 19);
        } else {
            startupCode();
        }


    }

    private void startupCode() {
        DatabaseHandler db = new DatabaseHandler(this);

        accountListInstance = AccountList.getInstance(db);
        transactionListInstance = TransactionList.getInstance(db);
        navView = findViewById(R.id.main_activity_bottom_navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showFragment(new BaseFragment());
        filterButton = (FloatingActionButton) findViewById(R.id.main_activity_filter_transaction_button);
        filterButton.hide();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        startUpExecuted = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19) {
            if (resultCode == RESULT_OK) {
                startupCode();
            } else {
                finish();
            }
        }
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
        } else if (id == R.id.settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        if (startUpExecuted) {
            showFragment(new BaseFragment());
            navView.setSelectedItemId(R.id.navigation_home);
        }
        super.onResume();

    }

    public static class BaseFragment extends Fragment {
        private AccountList accountListInstance;
        private TransactionList transactionListInstance;
        private View view;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.content_home, container, false);
            accountListInstance = AccountList.getInstance();
            transactionListInstance = TransactionList.getInstance();
            FloatingActionButton addTransactionButton = (FloatingActionButton) view.findViewById(R.id.main_activity_add_transaction_button);
            addTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (accountListInstance.getAccounts().size() == 0) {
                        Toast.makeText(getContext(), "At least one account is needed to transact", Toast.LENGTH_LONG).show();
                    } else {
                        Intent i = new Intent(getContext(), AddTransactionActivity.class);
                        startActivity(i);
                    }
                }
            });

            FloatingActionButton addAccountButton = (FloatingActionButton) view.findViewById(R.id.main_activity_add_account_button);
            addAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), AddAccountActivity.class);
                    startActivity(i);
                }
            });

            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            String currency = sp.getString("currency", "₹");
            double balance = accountListInstance.getTotalBalance();
            double expenditureToday = transactionListInstance.getTodaysExpenditureAmount();
            double expenditureYesterday = transactionListInstance.getYesterdaysExpenditureAmount();
            double incomeToday = transactionListInstance.getTodaysIncomeAmount();
            TextView balanceText = (TextView) view.findViewById(R.id.main_activity_balance_text);
            balanceText.setText(currency + " " + String.format("%.2f", balance));
            TextView expenditureText = (TextView) view.findViewById(R.id.main_activity_expenditure_text);
            expenditureText.setText(currency + " " + String.format("%.2f", expenditureToday));
            TextView incomeText = (TextView) view.findViewById(R.id.main_activity_income_text);
            incomeText.setText(currency + " " + String.format("%.2f", incomeToday));
            if (expenditureToday != 0) {
                TextView expenditureComment = (TextView) view.findViewById(R.id.main_activity_expenditure_comment);
                expenditureComment.setVisibility(View.VISIBLE);
                if (expenditureToday >= expenditureYesterday) {
                    expenditureComment.setTextColor(Color.rgb(200, 0, 0));
                    if (expenditureYesterday < 0.01) {
                        expenditureComment.setText("No expenditure was made yesterday.");
                    } else {
                        double percentageChange = (expenditureToday - expenditureYesterday) * 100 / expenditureYesterday;
                        expenditureComment.setText(String.format("%.2f", percentageChange) + "% more than yesterday");
                    }
                } else {
                    expenditureComment.setTextColor(Color.rgb(0, 100, 0));
                    double percentageChange = (expenditureYesterday - expenditureToday) * 100 / expenditureYesterday;
                    expenditureComment.setText(String.format("%.2f", percentageChange) + "% less than yesterday");

                }
            } else {
                TextView expenditureComment = (TextView) view.findViewById(R.id.main_activity_expenditure_comment);
                expenditureComment.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static class TransactionListFragment extends Fragment {

        public PopupWindow popupWindow;
        private EditText startDate;
        private long startTimestamp;
        private EditText endDate;
        private long endTimestamp;
        private CheckBox expenseCheck;
        private CheckBox incomeCheck;
        private Spinner spinner;
        private RecyclerView transactionList;
        private View view;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.content_list_transactions, container, false);
            transactionList = (RecyclerView) view.findViewById(R.id.main_activity_transaction_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            transactionList.setLayoutManager(layoutManager);
            TransactionListAdapter transactionListAdapter = new TransactionListAdapter(getActivity(), TransactionList.getInstance().getTransactions());
            transactionList.setAdapter(transactionListAdapter);

            final View popUpView = inflater.inflate(R.layout.popup_filters, container, false);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            popupWindow = new PopupWindow(popUpView, width, height, false);
            startDate = (EditText) popUpView.findViewById(R.id.pop_up_filter_start_date);
            startTimestamp = -1;
            endDate = (EditText) popUpView.findViewById(R.id.pop_up_filter_end_date);
            endTimestamp = -1;
            startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus == true) {
                        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        startDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                        startTimestamp = new GregorianCalendar(year, month, dayOfMonth).getTime().getTime();
                                    }
                                }, Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        dialog.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                startDate.setText("");
                                startTimestamp = -1;
                            }
                        });
                        dialog.show();
                    }
                }
            });
            endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus == true) {
                        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        endDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                        endTimestamp = new GregorianCalendar(year, month, dayOfMonth).getTime().getTime();
                                    }
                                }, Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        dialog.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                endDate.setText("");
                                endTimestamp = -1;
                            }
                        });
                        dialog.show();
                    }
                }

            });


            final ArrayList<Account> accountList = (ArrayList<Account>) AccountList.getInstance().getAccounts().clone();
            accountList.add(new Account(accountList.size() + 1, "Select an Account", "", 0));
            ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<Account>(getContext(), android.R.layout.simple_spinner_dropdown_item, accountList);
            spinner = (Spinner) popUpView.findViewById(R.id.pop_up_filter_account);
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(accountList.size() - 1);

            expenseCheck = (CheckBox) popUpView.findViewById(R.id.pop_up_filter_transaction_type_1);
            incomeCheck = (CheckBox) popUpView.findViewById(R.id.pop_up_filter_transaction_type_2);
            expenseCheck.setChecked(true);
            incomeCheck.setChecked(true);
            Button applyFilterButton = (Button) popUpView.findViewById(R.id.pop_up_filter_button);
            applyFilterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (startTimestamp != -1) {
                        if (endTimestamp == -1) {
                            endTimestamp = new Date().getTime();
                        }
                    }
                    if (endTimestamp != -1) {
                        if (startTimestamp == -1) {
                            startTimestamp = 0;
                        }
                    }
                    if (startTimestamp != -1 && endTimestamp != -1) {
                        if (startTimestamp > endTimestamp) {
                            Toast.makeText(getContext(), "Start date must be less than end date", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    long accountNumber = -1;
                    if (spinner.getSelectedItemId() != accountList.size() - 1) {
                        accountNumber = spinner.getSelectedItemId() + 1;
                    }
                    boolean showExpense = expenseCheck.isChecked();
                    boolean showIncome = incomeCheck.isChecked();
                    ArrayList<Transaction> filteredTransactionList =
                            TransactionList.getInstance().getFilteredTransactions(
                                    startTimestamp, endTimestamp, accountNumber, showExpense, showIncome
                            );
                    TransactionListAdapter transactionListAdapter = new TransactionListAdapter(getActivity(), filteredTransactionList);
                    transactionList.setAdapter(transactionListAdapter);
                    transactionListAdapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }
            });
            applyFilterButton.requestFocus();
            return view;
        }

        @Override
        public void onPause() {
            popupWindow.dismiss();
            super.onPause();
        }

        @Override
        public void onDestroy() {
            popupWindow.dismiss();
            super.onDestroy();
        }
    }

    public static class AccountListFragment extends Fragment {

        private AccountList accountListInstance;
        private AccountListAdapter accountListAdapter;
        private View view;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.content_list_accounts, container, false);

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
            TextView noAccountsText = (TextView) view.findViewById(R.id.main_activity_no_account_text);
            if (accountListInstance.getAccounts().size() == 0) {
                noAccountsText.setVisibility(View.VISIBLE);
            } else {
                noAccountsText.setVisibility(View.INVISIBLE);
            }
        }
    }
}
