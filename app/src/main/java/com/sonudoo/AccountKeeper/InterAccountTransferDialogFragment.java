package com.sonudoo.AccountKeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;

public class InterAccountTransferDialogFragment extends DialogFragment {
    private Spinner accountSpinner1;
    private Spinner accountSpinner2;
    private TextInputEditText interBankTransferAmount;
    private TransactionList transactionListInstance;
    private AccountList accountListInstance;
    private View dialogView;
    private int accountNumber;
    private ArrayList<Account> accountListCopy;

    public static final InterAccountTransferDialogFragment newInstance(int accountNumber) {
        InterAccountTransferDialogFragment interAccountTransferDialogFragment = new InterAccountTransferDialogFragment();
        Bundle bdl = new Bundle(1);
        bdl.putInt("ACCOUNT_NUMBER", accountNumber);
        interAccountTransferDialogFragment.setArguments(bdl);
        interAccountTransferDialogFragment.accountNumber = accountNumber;
        return interAccountTransferDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog);
        Bundle bdl = getArguments();
        if (bdl != null) {
            this.accountNumber = bdl.getInt("ACCOUNT_NUMBER");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.activity_inter_account_transfer, null);
        accountSpinner1 = dialogView.findViewById(R.id.inter_account_transfer_fragment_inter_account_transfer_account_1);
        accountSpinner2 = dialogView.findViewById(R.id.inter_account_transfer_fragment_inter_account_transfer_account_2);
        interBankTransferAmount = dialogView.findViewById(R.id.inter_account_transfer_fragment_inter_account_transfer_amount);


        accountListInstance = AccountList.getInstance(getContext());
        transactionListInstance = TransactionList.getInstance(getContext());
        accountListCopy = (ArrayList<Account>) accountListInstance.getAccounts().clone();

        accountListCopy.add(new Account(accountListInstance.getAccounts().size() + 1, "Select an " + "Account", "", 0));

        ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, accountListCopy);
        accountSpinner1.setAdapter(arrayAdapter);
        accountSpinner2.setAdapter(arrayAdapter);
        accountSpinner1.setSelection(accountNumber - 1);
        accountSpinner2.setSelection(accountListCopy.size() - 1);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Transfer Amount");
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("Transfer", new DialogInterface.OnClickListener() {
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
                      The selected accounts must be different.
                      The transaction amount must be at least 0.01.
                      The maximum transfer amount is equal to the with-drawable
                      balance.
                     */
                    if (accountSpinner1.getSelectedItem() == null) {
                        Toast.makeText(getContext(), "Please" + " select the account to transfer from", Toast.LENGTH_LONG).show();
                    } else if (accountSpinner2.getSelectedItem() == null) {
                        Toast.makeText(getContext(), "Please" + " select the account to transfer to", Toast.LENGTH_LONG).show();
                    } else if (accountSpinner1.getSelectedItemId() == accountListCopy.size() - 1) {
                        Toast.makeText(getContext(), "Please" + " select the " + "account to transfer from", Toast.LENGTH_LONG).show();
                    } else if (accountSpinner2.getSelectedItemId() == accountListCopy.size() - 1) {
                        Toast.makeText(getContext(), "Please" + " select the account to transfer to", Toast.LENGTH_LONG).show();
                    } else if (accountSpinner1.getSelectedItemId() == accountSpinner2.getSelectedItemId()) {
                        Toast.makeText(getContext(), "The " + "two accounts must be different", Toast.LENGTH_LONG).show();
                    } else {
                        long fromAccountIndex = accountSpinner1.getSelectedItemId();
                        long toAccountIndex = accountSpinner2.getSelectedItemId();
                        try {
                            double amount = Double.parseDouble(interBankTransferAmount.getText().toString());
                            if (amount < 0.01) {
                                Toast.makeText(getContext(), "Invalid amount entered. Minimum " + "transfer" + " amount is 0.01", Toast.LENGTH_LONG).show();
                            } else {
                                Account fromAccount = accountListInstance.getAccounts().get((int) fromAccountIndex);
                                Account toAccount = accountListInstance.getAccounts().get((int) toAccountIndex);
                                long transactionTimestamp = new Date().getTime();
                                if (transactionListInstance.addTransaction(fromAccount.accountNumber, amount, 2, "Being Amount transferred to " + toAccount.accountName + " Account", transactionTimestamp)) {
                                    transactionListInstance.addTransaction(toAccount.accountNumber, amount, 3, "Being Amount transferred from " + fromAccount.accountName + " Account", transactionTimestamp);
                                    Toast.makeText(getContext(), "Transfer Successful", Toast.LENGTH_LONG).show();
                                    ((MainActivity) getActivity()).reloadFragment();
                                    dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Cannot withdraw more than " + accountListInstance.getAccounts().get((int) fromAccountIndex).getBalance(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Invalid amount entered", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

}
