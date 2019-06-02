package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {

    private final ArrayList<Account> accountList;
    private final LayoutInflater layoutInflater;

    AccountListAdapter(Context context, ArrayList<Account> accountList) {
        this.accountList = accountList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.account_list_frame_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).accountNameText.setText(accountList.get(i).accountName);
        ((ViewHolder) viewHolder).accountDescText.setText(accountList.get(i).accountDesc);
        ((ViewHolder) viewHolder).accountBalanceText.setText("Rs. " + Double.toString(accountList.get(i).getBalance()));
    }

    @Override
    public int getItemCount() {
        return this.accountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView accountNameText;
        public final TextView accountBalanceText;
        public final TextView accountDescText;

        ViewHolder(View view) {
            super(view);
            accountNameText = (TextView) view.findViewById(R.id.account_name_text);
            accountBalanceText = (TextView) view.findViewById(R.id.account_balance_text);
            accountDescText = (TextView) view.findViewById(R.id.account_desc_text);
        }
    }
}
