package com.sonudoo.AccountKeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddTransactionDialogFragment extends DialogFragment {
    private AccountList accountList;
    private TransactionList transactionList;
    private View dialogView;
    private Spinner addTransactionAccount;
    private TextInputEditText addTransactionAmount;
    private TextInputEditText addTransactionJournalEntry;
    private RadioGroup addTransactionType;
    private ArrayList<Account> accountListCopy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.activity_add_transaction, null);
        addTransactionAccount = dialogView.findViewById(R.id.add_transaction_fragment_transaction_account);
        addTransactionAmount = dialogView.findViewById(R.id.add_transaction_fragment_transaction_amount);
        addTransactionJournalEntry = dialogView.findViewById(R.id.add_transaction_fragment_transaction_journal_entry);
        addTransactionType = dialogView.findViewById(R.id.add_transaction_fragment_transaction_type);

        accountListCopy = (ArrayList<Account>) AccountList.getInstance(getContext()).getAccounts().clone();
        accountListCopy.add(new Account(accountListCopy.size() + 1, "Select " + "an " + "Account", "", 0));
        ArrayAdapter<Account> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, accountListCopy);
        addTransactionAccount.setAdapter(arrayAdapter);
        addTransactionAccount.setSelection(accountListCopy.size() - 1);

        accountList = AccountList.getInstance(getContext());
        transactionList = TransactionList.getInstance(getContext());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Add New Transaction");
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
                      Transaction amount must be at least 0.01.
                      Journal entry must not be empty.
                      One of expense or income must be selected.
                     */
                    double transactionAmount;
                    try {
                        transactionAmount = Double.parseDouble(addTransactionAmount.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid " + "amount entered", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String journalEntry = addTransactionJournalEntry.getText().toString();
                    int id = addTransactionType.getCheckedRadioButtonId();
                    if (addTransactionAccount.getSelectedItemId() == accountListCopy.size() - 1) {
                        Toast.makeText(getContext(), "Select an account to " + "transact from", Toast.LENGTH_LONG).show();
                    } else if (journalEntry.compareTo("") == 0) {
                        Toast.makeText(getContext(), "Journal " + "entry must not be empty", Toast.LENGTH_LONG).show();
                    } else if (id == -1) {
                        Toast.makeText(getContext(), "Please " + "select if the transaction is an Expense or " + "Income", Toast.LENGTH_LONG).show();
                    } else if (transactionAmount < 0.01) {
                        Toast.makeText(getContext(), "Invalid " + "amount entered.  Minimum transaction amount is " + "0" + ".01", Toast.LENGTH_LONG).show();
                    } else {
                        RadioButton radioButton = dialogView.findViewById(id);
                        int type = 0;
                        if (radioButton.getText().toString().equals("Income"))
                            type = 1;
                        if (TransactionList.getInstance(getContext()).addTransaction((int) addTransactionAccount.getSelectedItemId() + 1, transactionAmount, type, "Being " + journalEntry)) {
                            Toast.makeText(getContext(), "Transaction successful", Toast.LENGTH_LONG).show();
                            ((MainActivity) getActivity()).reloadFragment();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Transaction unsuccessful. Ensure that the " + "transaction amount is less than " + "balance.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

}
