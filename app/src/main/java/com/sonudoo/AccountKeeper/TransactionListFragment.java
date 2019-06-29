package com.sonudoo.AccountKeeper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TransactionListFragment extends Fragment {
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
        transactionListFragmentView = inflater.inflate(R.layout.content_list_transactions, container, attachToRoot);

            /*
              Set up the adapter and layout for the recycler accountListView.
             */
        transactionListView = transactionListFragmentView.findViewById(R.id.main_activity_transaction_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        transactionListView.setLayoutManager(layoutManager);
        TransactionListAdapter transactionListAdapter = new TransactionListAdapter(getActivity(), TransactionList.getInstance(mContext).getTransactions());
        transactionListView.setAdapter(transactionListAdapter);

            /*
              Set up the popup filter window.
             */

        final View popUpView = inflater.inflate(R.layout.popup_filters, container, attachToRoot);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupFilterWindow = new PopupWindow(popUpView, width, height, isFocusable);
        popupFilterWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));


            /*
              Initialize the timestamp range to default
             */
        filterStartDate = popUpView.findViewById(R.id.pop_up_filter_start_date);
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
                    DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                /*
                                  Upon selection of date, show the date on
                                  the text box
                                 */
                            filterStartDate.setText(String.format("%d/%d" + "/%d", dayOfMonth, month + 1, year));
                            filterStartTimestamp = new GregorianCalendar(year, month, dayOfMonth).getTime().getTime();
                        }
                    }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
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
                DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            /*
                              Upon selection of date, show the date on
                              the text box
                             */
                        filterStartDate.setText(String.format("%d/%d/%d", dayOfMonth, month + 1, year));
                        filterStartTimestamp = new GregorianCalendar(year, month, dayOfMonth).getTime().getTime();
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
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
                    DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                /*
                                  Upon selection of date, show the date on
                                  the text box
                                 */
                            filterEndDate.setText(String.format("%d/%d/%d", dayOfMonth, month + 1, year));
                            filterEndTimestamp = new GregorianCalendar(year, month, dayOfMonth).getTime().getTime();
                        }
                    }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
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
                DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            /*
                              Upon selection of date, show the date on
                              the text box
                             */
                        filterEndDate.setText(String.format("%d/%d/%d", dayOfMonth, month + 1, year));
                        filterEndTimestamp = new GregorianCalendar(year, month, dayOfMonth).getTime().getTime();

                        /*
                            Add 23 hours, 59 minutes and 59 seconds to get
                            the end time for the day
                         */
                        long numberOfMilliSecondsInADay = 24 * 60 * 60 * 1000;
                        filterEndTimestamp += numberOfMilliSecondsInADay - 1;

                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
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
        final ArrayList<Account> accountList = (ArrayList<Account>) AccountList.getInstance(mContext).getAccounts().clone();
        accountList.add(new Account(accountList.size() + 1, "Select an " + "Account", "", 0));
        ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, accountList);
        filterAccountSpinner = popUpView.findViewById(R.id.pop_up_filter_account);
        filterAccountSpinner.setAdapter(arrayAdapter);
        filterAccountSpinner.setSelection(accountList.size() - 1);

        filterExpenseCheck = popUpView.findViewById(R.id.pop_up_filter_transaction_type_1);
        filterIncomeCheck = popUpView.findViewById(R.id.pop_up_filter_transaction_type_2);
        filterExpenseCheck.setChecked(true);
        filterIncomeCheck.setChecked(true);
        Button applyFilterButton = popUpView.findViewById(R.id.pop_up_filter_button);

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
                        Toast.makeText(mContext, "Start date must be " + "less than end date", Toast.LENGTH_LONG).show();
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
                ArrayList<Transaction> filteredTransactionList = TransactionList.getInstance(mContext).getFilteredTransactions(filterStartTimestamp, filterEndTimestamp, accountNumber, showExpense, showIncome);
                TransactionListAdapter transactionListAdapter = new TransactionListAdapter(getActivity(), filteredTransactionList);
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
