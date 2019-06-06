package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {
    /**
     * This class is the adapter for the account list recycler view.
     */

    private final ArrayList<Account> accountList;
    private final LayoutInflater layoutInflater;
    private final Context context;

    AccountListAdapter(Context context, ArrayList<Account> accountList) {
        this.context = context;
        this.accountList = accountList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.account_list_layout,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        SharedPreferences sp =
                PreferenceManager.getDefaultSharedPreferences(context);
        String sharedPreferenceCurrencyKey = "currency";
        String sharedPreferenceCurrencyDefaultValue = "₹";
        String currency = sp.getString(sharedPreferenceCurrencyKey,
                sharedPreferenceCurrencyDefaultValue);
        viewHolder.accountNameText.setText(accountList.get(i).accountName);
        viewHolder.accountDescText.setText(accountList.get(i).accountDesc);
        viewHolder.accountBalanceText.setText(String.format("%s %s", currency
                , accountList.get(i).getBalance()));
        viewHolder.editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAccountIntent = new Intent(context,
                        EditAccountActivity.class);
                editAccountIntent.putExtra(EditAccountActivity.ACCOUNT_NUMBER
                        , i + 1);
                context.startActivity(editAccountIntent);
            }
        });
        viewHolder.transferAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountList.size() < 2) {
                    Toast.makeText(context,
                            "At two accounts are needed to " + "conduct a " + "transfer", Toast.LENGTH_LONG).show();
                } else {
                    Intent interAccountTransferIntent = new Intent(context,
                            InterAccountTransferActivity.class);
                    interAccountTransferIntent.putExtra(InterAccountTransferActivity.ACCOUNT_NUMBER, i + 1);
                    context.startActivity(interAccountTransferIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.accountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView accountNameText;
        final TextView accountBalanceText;
        final TextView accountDescText;
        final Button editAccountButton;
        final Button transferAccountButton;

        ViewHolder(View view) {
            super(view);
            accountNameText =
                    view.findViewById(R.id.main_activity_account_name_text);
            accountBalanceText =
                    view.findViewById(R.id.main_activity_account_balance_text);
            accountDescText =
                    view.findViewById(R.id.main_activity_account_desc_text);
            editAccountButton =
                    view.findViewById(R.id.main_activity_edit_account_button);
            transferAccountButton =
                    view.findViewById(R.id.main_activity_transfer_button);
        }
    }
}
