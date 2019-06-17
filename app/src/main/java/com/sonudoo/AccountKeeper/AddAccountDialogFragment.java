package com.sonudoo.AccountKeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class AddAccountDialogFragment extends DialogFragment {
    private AccountList accountList;
    private TransactionList transactionList;
    private View dialogView;
    private TextInputEditText addAccountName;
    private TextInputEditText addAccountDesc;
    private TextInputEditText addAccountInitialBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.activity_add_account, null);
        addAccountName = dialogView.findViewById(R.id.add_account_fragment_account_name);
        addAccountDesc = dialogView.findViewById(R.id.add_account_fragment_account_desc);
        addAccountInitialBalance = dialogView.findViewById(R.id.add_account_fragment_account_initial_balance);


        accountList = AccountList.getInstance(getContext());
        transactionList = TransactionList.getInstance(getContext());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Add New Account");
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Operation cancelled" + " " + "by" + " user", Toast.LENGTH_LONG).show();
            }
        });
        return alertDialogBuilder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*
                  Account name must not be empty.
                  Account name must not clash.
                  Initial balance must be at least 0.01.
                 */
                    if (addAccountName.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getContext(), "Account name " + "must " + "not " + "be empty!", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            double amount = Double.parseDouble(addAccountInitialBalance.getText().toString());
                            if (amount < 0) {
                                Toast.makeText(getContext(), "Invalid " + "amount" + " entered. It must be " + "greater " + "than" + " " + "0.01!", Toast.LENGTH_LONG).show();
                            } else {
                                Account newAccount = accountList.addAccount(addAccountName.getText().toString().toUpperCase(), addAccountDesc.getText().toString());
                                if (newAccount == null) {
                                    Toast.makeText(getContext(), "Account " + "name " + "is" + " " + "already " + "taken!", Toast.LENGTH_LONG).show();
                                } else {
                                    if (amount >= 0.01) {
                                        transactionList.addTransaction(newAccount.accountNumber, amount, 1, "Being Initial amount added");
                                    }
                                    Toast.makeText(getContext(), "Successfully added!", Toast.LENGTH_LONG).show();
                                    ((MainActivity) getActivity()).reloadFragment();
                                    d.dismiss();
                                }
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Invalid " + "initial " + "amount entered!", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "An unknown " + "error " + "occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

}
