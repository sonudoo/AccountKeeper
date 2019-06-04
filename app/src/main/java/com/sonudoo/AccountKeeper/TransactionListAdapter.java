package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    private final ArrayList<Transaction> transactionList;
    private final LayoutInflater layoutInflater;
    Context context;

    TransactionListAdapter(Context context, ArrayList<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.transaction_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).transactionAccountName.setText(transactionList.get(i).fromAccount.accountName);
        ((ViewHolder) viewHolder).transactionJournalEntry.setText(transactionList.get(i).journalEntry);
        ((ViewHolder) viewHolder).transactionAmount.setText("₹ " + Double.toString(transactionList.get(i).amount));
        if (transactionList.get(i).toAccount == false) {
            ((ViewHolder) viewHolder).transactionType.setTextColor(Color.rgb(100, 0, 0));
            ((ViewHolder) viewHolder).transactionType.setText("To Expense Account");
        } else {
            ((ViewHolder) viewHolder).transactionType.setTextColor(Color.rgb(0, 100, 0));
            ((ViewHolder) viewHolder).transactionType.setText("From Income Account");
        }
        ((ViewHolder) viewHolder).transactionTime.setText(new Date(transactionList.get(i).timestamp).toString());
    }

    @Override
    public int getItemCount() {
        return this.transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView transactionAccountName;
        public final TextView transactionAmount;
        public final TextView transactionTime;
        public final TextView transactionJournalEntry;
        public final TextView transactionType;

        ViewHolder(View view) {
            super(view);
            transactionAccountName = (TextView) view.findViewById(R.id.main_activity_transaction_list_from_account);
            transactionAmount = (TextView) view.findViewById(R.id.main_activity_transaction_list_amount);
            transactionTime = (TextView) view.findViewById(R.id.main_activity_transaction_list_date);
            transactionJournalEntry = (TextView) view.findViewById(R.id.main_activity_transaction_list_journal_entry);
            transactionType = (TextView) view.findViewById(R.id.main_activity_transaction_list_type);
        }
    }
}
