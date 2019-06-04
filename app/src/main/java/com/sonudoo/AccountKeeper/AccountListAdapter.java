package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {

    private final ArrayList<Account> accountList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    AccountListAdapter(Context context, ArrayList<Account> accountList) {
        this.context = context;
        this.accountList = accountList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.account_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        ((ViewHolder) viewHolder).accountNameText.setText(accountList.get(i).accountName);
        ((ViewHolder) viewHolder).accountDescText.setText(accountList.get(i).accountDesc);
        ((ViewHolder) viewHolder).accountBalanceText.setText("₹ " + Double.toString(accountList.get(i).getBalance()));
        ((ViewHolder) viewHolder).editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditAccountActivity.class);
                intent.putExtra(EditAccountActivity.ACCOUNT_NUMBER, accountList.get(i));
                context.startActivity(intent);
            }
        });
        ((ViewHolder) viewHolder).transferAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountList.size() < 2) {
                    Toast.makeText(context, "At two accounts are needed to conduct a transfer", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(context, InterAccountTransferActivity.class);
                    intent.putExtra(InterAccountTransferActivity.ACCOUNT_NUMBER, accountList.get(i));
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.accountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView accountNameText;
        public final TextView accountBalanceText;
        public final TextView accountDescText;
        public final Button editAccountButton;
        public final Button transferAccountButton;

        ViewHolder(View view) {
            super(view);
            accountNameText = (TextView) view.findViewById(R.id.main_activity_account_name_text);
            accountBalanceText = (TextView) view.findViewById(R.id.main_activity_account_balance_text);
            accountDescText = (TextView) view.findViewById(R.id.main_activity_account_desc_text);
            editAccountButton = (Button) view.findViewById(R.id.main_activity_edit_account_button);
            transferAccountButton = (Button) view.findViewById(R.id.main_activity_transfer_button);
        }
    }
}
