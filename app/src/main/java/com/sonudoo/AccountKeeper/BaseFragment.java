package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

class BaseFragment extends Fragment {
    /**
     * This inner class holds views and data for BaseFragment
     */
    private final String sharedPreferenceCurrencyKey = "currency";
    private final String sharedPreferenceCurrencyDefault = "₹";
    private final boolean attachToRoot = false;
    private AccountList accountListInstance;
    private TransactionList transactionListInstance;
    private View baseFragmentView;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        baseFragmentView = inflater.inflate(R.layout.content_home, container, attachToRoot);
        accountListInstance = AccountList.getInstance(mContext);
        transactionListInstance = TransactionList.getInstance(mContext);
        final FloatingActionButton addTransactionButton = baseFragmentView.findViewById(R.id.main_activity_add_transaction_button);
        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountListInstance.getAccounts().size() == 0) {
                    Toast.makeText(mContext, "At least one account " + "is" + " needed to " + "transact", Toast.LENGTH_LONG).show();
                } else {
                    AddTransactionDialogFragment addTransactionDialogFragment = new AddTransactionDialogFragment();
                    addTransactionDialogFragment.show(getActivity().getSupportFragmentManager(), "Restore");

                }
            }
        });

        FloatingActionButton addAccountButton = baseFragmentView.findViewById(R.id.main_activity_add_account_button);
        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAccountDialogFragment addAccountDialogFragment = new AddAccountDialogFragment();
                addAccountDialogFragment.show(getActivity().getSupportFragmentManager(), "Restore");
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String currency = sharedPreferences.getString(sharedPreferenceCurrencyKey, sharedPreferenceCurrencyDefault);
        double currentBalance = accountListInstance.getTotalBalance();
        double expenditureToday = transactionListInstance.getTodaysExpenditureAmount();
        double expenditureYesterday = transactionListInstance.getYesterdaysExpenditureAmount();
        double incomeToday = transactionListInstance.getTodaysIncomeAmount();
        TextView balanceText = baseFragmentView.findViewById(R.id.main_activity_balance_text);
        balanceText.setText(String.format("%s %s", currency, String.format("%" + ".2f", currentBalance)));
        TextView expenditureText = baseFragmentView.findViewById(R.id.main_activity_expenditure_text);
        expenditureText.setText(String.format("%s %s", currency, String.format("%.2f", expenditureToday)));
        TextView incomeText = baseFragmentView.findViewById(R.id.main_activity_income_text);
        incomeText.setText(String.format("%s %s", currency, String.format("%" + ".2f", incomeToday)));
        if (expenditureToday > 0.009) {
            /*
              If expenditure is non-zero, then show the comparative
              figures with respect to a day before.
             */
            TextView expenditureComment = baseFragmentView.findViewById(R.id.main_activity_expenditure_comment);
            expenditureComment.setVisibility(View.VISIBLE);
            if (expenditureToday >= expenditureYesterday) {
                /*
                  Red text would be displayed.
                 */
                expenditureComment.setTextColor(Color.rgb(200, 0, 0));
                if (expenditureYesterday < 0.01) {
                    expenditureComment.setText("No expenditure was made " + "yesterday.");
                } else {
                    double percentageChange = (expenditureToday - expenditureYesterday) * 100 / expenditureYesterday;
                    expenditureComment.setText(String.format("%s%% more " + "than yesterday", String.format("%.2f", percentageChange)));
                }
            } else {
                /*
                  Green text would be displayed.
                 */
                expenditureComment.setTextColor(Color.rgb(0, 100, 0));
                double percentageChange = (expenditureYesterday - expenditureToday) * 100 / expenditureYesterday;
                expenditureComment.setText(String.format("%s%% less than " + "yesterday", String.format("%.2f", percentageChange)));

            }
        } else {
            TextView expenditureComment = baseFragmentView.findViewById(R.id.main_activity_expenditure_comment);
            expenditureComment.setVisibility(View.INVISIBLE);
        }
    }
}