package com.sonudoo.AccountKeeper;

import android.app.DatePickerDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    /*
      Main Activity has three fragments within -
      1. Base fragment containing Home baseFragmentView.
      2. Account list fragment containing account list baseFragmentView.
      3. Transaction list fragment containing transaction list
      baseFragmentView and
      related filter popup views.
     */
    /**
     * isStartupExecuted is set to true only when the user has passed the
     * authentication.
     */
    private final int AUTHENTICATION_REQUEST_CODE = 19;
    private boolean isStartupCodeExecuted;
    private FloatingActionButton filterButton;
    private TransactionListFragment transactionListFragment;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(new BaseFragment());
                    filterButton.hide(); // Hide the filter button on Home
                    // baseFragmentView
                    return true;
                case R.id.navigation_transactions:
                    transactionListFragment = new TransactionListFragment();
                    filterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                              Show the filter pop up window defined in the
                              transaction list fragment baseFragmentView.
                             */
                            transactionListFragment.popupFilterWindow.showAtLocation(transactionListFragment.getView(), Gravity.CENTER, 0, 0);
                            Button applyFilterButton =
                                    transactionListFragment.popupFilterWindow.getContentView().findViewById(R.id.pop_up_filter_button);
                            applyFilterButton.requestFocus();
                            transactionListFragment.popupFilterWindow.setElevation(20);
                        }
                    });
                    filterButton.show();
                    showFragment(transactionListFragment);
                    return true;
                case R.id.navigation_accounts:
                    showFragment(new AccountListFragment());
                    filterButton.hide();
            }
            return false;
        }
    };
    private BottomNavigationView navView;


    private void showFragment(Fragment fragment) {
        /*
          Loads a fragment to main activity
         */
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_scroll_view, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =
                findViewById(R.id.inter_account_transfer_activity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);

        /*
          Upon main activity start conduct an authentication if enabled.
         */
        isStartupCodeExecuted = false;
        KeyguardManager keyguardManager =
                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        if (keyguardManager.isKeyguardSecure()) {
            /*
              If authentication is enabled
             */
            Intent authIntent =
                    keyguardManager.createConfirmDeviceCredentialIntent(
                            "Authentication", "Authentication required to ");
            startActivityForResult(authIntent, AUTHENTICATION_REQUEST_CODE);
        } else {
            /*
              Else continue with normal startup
             */
            startupCode();
        }

    }

    private void startupCode() {
        /*
          filterButton belongs to main activity baseFragmentView because it
          requires a
          non-scrolling position.
          It must be shown only on transaction list fragment.
         */
        navView = findViewById(R.id.main_activity_bottom_navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        /*
          Base fragment is the default fragment to load
         */
        showFragment(new BaseFragment());

        /*
          Hide the filter button upon activity startup
         */
        filterButton =
                findViewById(R.id.main_activity_filter_transaction_button);
        filterButton.hide();

        isStartupCodeExecuted = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        /*
          This method handles the result of authentication.
          If authentication is successful then it performs normal startup.
          Else the activity closes itself.
         */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHENTICATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                startupCode();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onResume() {
        /*
          This method loads the home fragment whenever the activity resumes
          and has passed authentication
         */
        if (isStartupCodeExecuted) {
            showFragment(new BaseFragment());
            navView.setSelectedItemId(R.id.navigation_home);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            Intent shareAppIntent = new Intent(Intent.ACTION_SEND);
            shareAppIntent.setType("text/plain");
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, "Download Account " +
                    "Keeper today.");
            startActivity(shareAppIntent);
            return true;
        } else if (id == R.id.exit) {
            finish();
        } else if (id == R.id.settings) {
            Intent settingsActivityIntent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivity(settingsActivityIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    static class BaseFragment extends Fragment {
        private final String sharedPreferenceCurrencyKey = "currency";
        private final String sharedPreferenceCurrencyDefault = "₹";
        private final boolean attachToRoot = false;
        /**
         * This inner class holds views and data for BaseFragment
         */
        private AccountList accountListInstance;
        private TransactionList transactionListInstance;
        private View baseFragmentView;
        private Context mContext;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container
                , Bundle savedInstanceState) {
            mContext = getContext();
            baseFragmentView = inflater.inflate(R.layout.content_home,
                    container, attachToRoot);
            accountListInstance = AccountList.getInstance(mContext);
            transactionListInstance = TransactionList.getInstance(mContext);
            FloatingActionButton addTransactionButton =
                    baseFragmentView.findViewById(R.id.main_activity_add_transaction_button);
            addTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (accountListInstance.getAccounts().size() == 0) {
                        Toast.makeText(mContext, "At least one account " +
                                "is" + " needed to " + "transact",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Intent addTransactionActivityIntent =
                                new Intent(mContext,
                                        AddTransactionActivity.class);
                        startActivity(addTransactionActivityIntent);
                    }
                }
            });

            FloatingActionButton addAccountButton =
                    baseFragmentView.findViewById(R.id.main_activity_add_account_button);
            addAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addAccountActivityIntent = new Intent(mContext,
                            AddAccountActivity.class);
                    startActivity(addAccountActivityIntent);
                }
            });
            return baseFragmentView;
        }

        @Override
        public void onResume() {
            /*
              This method reloads the account balances and expenditures in
              the transactionListFragmentView.
             */
            super.onResume();
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(mContext);
            String currency =
                    sharedPreferences.getString(sharedPreferenceCurrencyKey,
                            sharedPreferenceCurrencyDefault);
            double currentBalance = accountListInstance.getTotalBalance();
            double expenditureToday =
                    transactionListInstance.getTodaysExpenditureAmount();
            double expenditureYesterday =
                    transactionListInstance.getYesterdaysExpenditureAmount();
            double incomeToday =
                    transactionListInstance.getTodaysIncomeAmount();
            TextView balanceText =
                    baseFragmentView.findViewById(R.id.main_activity_balance_text);
            balanceText.setText(String.format("%s %s", currency,
                    String.format("%.2f", currentBalance)));
            TextView expenditureText =
                    baseFragmentView.findViewById(R.id.main_activity_expenditure_text);
            expenditureText.setText(String.format("%s %s", currency,
                    String.format("%.2f", expenditureToday)));
            TextView incomeText =
                    baseFragmentView.findViewById(R.id.main_activity_income_text);
            incomeText.setText(String.format("%s %s", currency,
                    String.format("%.2f", incomeToday)));
            if (expenditureToday > 0.009) {
                /*
                  If expenditure is non-zero, then show the comparative
                  figures with respect to a day before.
                 */
                TextView expenditureComment =
                        baseFragmentView.findViewById(R.id.main_activity_expenditure_comment);
                expenditureComment.setVisibility(View.VISIBLE);
                if (expenditureToday >= expenditureYesterday) {
                    /*
                      Red text would be displayed.
                     */
                    expenditureComment.setTextColor(Color.rgb(200, 0, 0));
                    if (expenditureYesterday < 0.01) {
                        expenditureComment.setText("No " + "expenditure was " + "made yesterday.");
                    } else {
                        double percentageChange =
                                (expenditureToday - expenditureYesterday) * 100 / expenditureYesterday;
                        expenditureComment.setText(String.format("%s%% more " + "than yesterday", String.format("%.2f", percentageChange)));
                    }
                } else {
                    /*
                      Green text would be displayed.
                     */
                    expenditureComment.setTextColor(Color.rgb(0, 100, 0));
                    double percentageChange = (expenditureYesterday - expenditureToday) * 100 / expenditureYesterday;
                    expenditureComment.setText(String.format("%s%% less than "
                            + "yesterday", String.format("%.2f",
                            percentageChange)));

                }
            } else {
                TextView expenditureComment =
                        baseFragmentView.findViewById(R.id.main_activity_expenditure_comment);
                expenditureComment.setVisibility(View.INVISIBLE);
            }
        }
    }

    static class TransactionListFragment extends Fragment {
        private final boolean attachToRoot = false;
        private final boolean isFocusable = false;
        /**
         * This class represents views and data for the transaction list
         * fragment.
         */

        PopupWindow popupFilterWindow;
        private EditText filterStartDate;
        private long filterStartTimestamp;
        private EditText filterEndDate;
        private long filterEndTimestamp;
        private CheckBox filterExpenseCheck;
        private CheckBox filterIncomeCheck;
        private Spinner filterAccountSpinner;
        private RecyclerView transactionListView;
        private View transactionListFragmentView;
        private Context mContext;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mContext = getContext();
            transactionListFragmentView =
                    inflater.inflate(R.layout.content_list_transactions,
                            container, attachToRoot);

            /*
              Set up the adapter and layout for the recycler accountListView.
             */
            transactionListView =
                    transactionListFragmentView.findViewById(R.id.main_activity_transaction_list);
            LinearLayoutManager layoutManager =
                    new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            transactionListView.setLayoutManager(layoutManager);
            TransactionListAdapter transactionListAdapter =
                    new TransactionListAdapter(getActivity(),
                            TransactionList.getInstance(mContext).getTransactions());
            transactionListView.setAdapter(transactionListAdapter);

            /*
              Set up the popup filter window.
             */

            final View popUpView = inflater.inflate(R.layout.popup_filters,
                    container, attachToRoot);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            popupFilterWindow = new PopupWindow(popUpView, width, height,
                    isFocusable);

            /*
              Initialize the timestamp range to default
             */
            filterStartDate =
                    popUpView.findViewById(R.id.pop_up_filter_start_date);
            filterStartTimestamp = -1;
            filterEndDate = popUpView.findViewById(R.id.pop_up_filter_end_date);
            filterEndTimestamp = -1;

            /*
              Open Date dialog picker when date field is focused or clicked.
             */
            filterStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        DatePickerDialog dialog =
                                new DatePickerDialog(mContext,
                                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int dayOfMonth) {
                                /*
                                  Upon selection of date, show the date on
                                  the text box
                                 */
                                filterStartDate.setText(String.format("%d/%d" + "/%d", dayOfMonth, month + 1, year));
                                filterStartTimestamp =
                                        new GregorianCalendar(year, month,
                                                dayOfMonth).getTime().getTime();
                            }
                        }, Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        dialog.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                /*
                                  If cancelled, reset the filter to default
                                  value
                                 */
                                filterStartDate.setText("");
                                filterStartTimestamp = -1;
                            }
                        });
                        dialog.show();
                    }
                }
            });

            filterStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int month, int dayOfMonth) {
                            /*
                              Upon selection of date, show the date on
                              the text box
                             */
                            filterStartDate.setText(String.format("%d/%d/%d",
                                    dayOfMonth, month + 1, year));
                            filterStartTimestamp = new GregorianCalendar(year
                                    , month, dayOfMonth).getTime().getTime();
                        }
                    }, Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    dialog.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            /*
                              If cancelled, reset the filter to default
                              value
                             */
                            filterStartDate.setText("");
                            filterStartTimestamp = -1;
                        }
                    });
                    dialog.show();

                }
            });
            filterEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        DatePickerDialog dialog =
                                new DatePickerDialog(mContext,
                                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int dayOfMonth) {
                                /*
                                  Upon selection of date, show the date on
                                  the text box
                                 */
                                filterEndDate.setText(String.format("%d/%d/%d"
                                        , dayOfMonth, month + 1, year));
                                filterEndTimestamp =
                                        new GregorianCalendar(year, month,
                                                dayOfMonth).getTime().getTime();
                            }
                        }, Calendar.getInstance().get(Calendar.YEAR),
                                        Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        dialog.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                /*
                                  If cancelled, reset the filter to default
                                  value
                                 */
                                filterEndDate.setText("");
                                filterEndTimestamp = -1;
                            }
                        });
                        dialog.show();
                    }
                }

            });

            filterEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int month, int dayOfMonth) {
                            /*
                              Upon selection of date, show the date on
                              the text box
                             */
                            filterEndDate.setText(String.format("%d/%d/%d",
                                    dayOfMonth, month + 1, year));
                            filterEndTimestamp = new GregorianCalendar(year,
                                    month, dayOfMonth).getTime().getTime();
                        }
                    }, Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    dialog.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            /*
                              If cancelled, reset the filter to default
                              value
                             */
                            filterEndDate.setText("");
                            filterEndTimestamp = -1;
                        }
                    });
                    dialog.show();

                }
            });


            /*
              Add a dummy account to the account list. This account would be
              entitled "Select an Account" and would represent
             */
            final ArrayList<Account> accountList =
                    (ArrayList<Account>) AccountList.getInstance(mContext).getAccounts().clone();
            accountList.add(new Account(accountList.size() + 1,
                    "Select an " + "Account", "", 0));
            ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_dropdown_item, accountList);
            filterAccountSpinner =
                    popUpView.findViewById(R.id.pop_up_filter_account);
            filterAccountSpinner.setAdapter(arrayAdapter);
            filterAccountSpinner.setSelection(accountList.size() - 1);

            filterExpenseCheck =
                    popUpView.findViewById(R.id.pop_up_filter_transaction_type_1);
            filterIncomeCheck =
                    popUpView.findViewById(R.id.pop_up_filter_transaction_type_2);
            filterExpenseCheck.setChecked(true);
            filterIncomeCheck.setChecked(true);
            Button applyFilterButton =
                    popUpView.findViewById(R.id.pop_up_filter_button);

            applyFilterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                      If start time is set and end time is not set then set
                      end time as current time.
                      If end time is set and start time is not set then set
                      start time to 0.
                     */
                    if (filterStartTimestamp != -1) {
                        if (filterEndTimestamp == -1) {
                            filterEndTimestamp = new Date().getTime();
                        }
                    }
                    if (filterEndTimestamp != -1) {
                        if (filterStartTimestamp == -1) {
                            filterStartTimestamp = 0;
                        }
                    }
                    if (filterStartTimestamp != -1 && filterEndTimestamp != -1) {
                        if (filterStartTimestamp > filterEndTimestamp) {
                            Toast.makeText(mContext, "Start date must be " +
                                    "less than end date", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    long accountNumber = -1;
                    if (filterAccountSpinner.getSelectedItemId() != accountList.size() - 1) {
                        accountNumber = filterAccountSpinner.getSelectedItemId() + 1;
                    }
                    boolean showExpense = filterExpenseCheck.isChecked();
                    boolean showIncome = filterIncomeCheck.isChecked();

                    /*
                      Get the filtered transactions and display them.
                     */
                    ArrayList<Transaction> filteredTransactionList =
                            TransactionList.getInstance(mContext).getFilteredTransactions(filterStartTimestamp, filterEndTimestamp, accountNumber, showExpense, showIncome);
                    TransactionListAdapter transactionListAdapter =
                            new TransactionListAdapter(getActivity(),
                                    filteredTransactionList);
                    transactionListView.setAdapter(transactionListAdapter);
                    transactionListAdapter.notifyDataSetChanged();
                    popupFilterWindow.dismiss();
                }
            });
            applyFilterButton.requestFocus();
            return transactionListFragmentView;
        }

        @Override
        public void onPause() {
            popupFilterWindow.dismiss();
            super.onPause();
        }
    }

    static class AccountListFragment extends Fragment {

        private final boolean attachToRoot = false;
        private AccountListAdapter accountListAdapter;
        /**
         * This class holds views and data for the account list fragment.
         */

        private AccountList accountListInstance;
        private View accountListView;
        private Context mContext;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container
                , Bundle savedInstanceState) {
            mContext = getContext();
            accountListView = inflater.inflate(R.layout.content_list_accounts
                    , container, attachToRoot);

            RecyclerView accountList =
                    accountListView.findViewById(R.id.main_activity_account_list);
            final int spanCount = 1;
            GridLayoutManager gridLayoutManager =
                    new GridLayoutManager(getActivity(), spanCount);
            accountList.setLayoutManager(gridLayoutManager);
            accountListInstance = AccountList.getInstance(mContext);
            accountListAdapter = new AccountListAdapter(getActivity(), accountListInstance.getAccounts());
            accountList.setAdapter(accountListAdapter);
            return accountListView;
        }

        @Override
        public void onResume() {
            super.onResume();
            accountListAdapter.notifyDataSetChanged();
            /*
              If there are no accounts, then no accounts text is displayed.
             */
            TextView noAccountsText = accountListView.findViewById(R.id.main_activity_no_account_text);
            if (accountListInstance.getAccounts().size() == 0) {
                noAccountsText.setVisibility(View.VISIBLE);
            } else {
                noAccountsText.setVisibility(View.INVISIBLE);
            }
        }
    }
}
