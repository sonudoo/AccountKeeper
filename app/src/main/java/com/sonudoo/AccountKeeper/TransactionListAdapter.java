package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {
    /**
     * This class is the adapter for transaction list recycler view.
     */

    private final ArrayList<Transaction> transactionList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    TransactionListAdapter(Context context,
                           ArrayList<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.transaction_list_layout,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SharedPreferences sp =
                PreferenceManager.getDefaultSharedPreferences(context);
        String sharedPreferenceCurrencyKey = "currency";
        String sharedPreferenceCurrencyDefaultValue = "₹";
        String currency = sp.getString(sharedPreferenceCurrencyKey,
                sharedPreferenceCurrencyDefaultValue);
        viewHolder.transactionAccountName.setText(AccountList.getInstance(context).getAccount(transactionList.get(i).transactionAccountNumber).accountName);
        viewHolder.transactionJournalEntry.setText(transactionList.get(i).transactionJournalEntry);
        viewHolder.transactionAmount.setText(String.format("%s %s", currency,
                transactionList.get(i).transactionAmount));

        /*
          Expenses are shown in red.
          Incomes are shown in green.
         */

        if (transactionList.get(i).transactionType == 0 || transactionList.get(i).transactionType == 2) {
            viewHolder.transactionType.setTextColor(Color.rgb(200, 0, 0));
            viewHolder.transactionType.setText(context.getString(R.string.to_expense_account));
        } else {
            viewHolder.transactionType.setTextColor(Color.rgb(0, 100, 0));
            viewHolder.transactionType.setText(context.getString(R.string.from_income_account));
        }
        viewHolder.transactionTime.setText(new Date(transactionList.get(i).transactionTimestamp).toString());
    }

    @Override
    public int getItemCount() {
        return this.transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView transactionAccountName;
        final TextView transactionAmount;
        final TextView transactionTime;
        final TextView transactionJournalEntry;
        final TextView transactionType;

        ViewHolder(View view) {
            super(view);
            transactionAccountName =
                    view.findViewById(R.id.main_activity_transaction_list_from_account);
            transactionAmount =
                    view.findViewById(R.id.main_activity_transaction_list_amount);
            transactionTime =
                    view.findViewById(R.id.main_activity_transaction_list_date);
            transactionJournalEntry =
                    view.findViewById(R.id.main_activity_transaction_list_journal_entry);
            transactionType =
                    view.findViewById(R.id.main_activity_transaction_list_type);
        }
    }
}
