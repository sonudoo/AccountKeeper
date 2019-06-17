package com.sonudoo.AccountKeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditAccountDialogFragment extends DialogFragment {
    private int accountNumber;
    private EditText editAccountName;
    private EditText editAccountDesc;
    private AccountList accountList;
    private View dialogView;

    public static final EditAccountDialogFragment newInstance(int accountNumber) {
        EditAccountDialogFragment editAccountDialogFragment = new EditAccountDialogFragment();
        Bundle bdl = new Bundle(1);
        bdl.putInt("ACCOUNT_NUMBER", accountNumber);
        editAccountDialogFragment.setArguments(bdl);
        editAccountDialogFragment.accountNumber = accountNumber;
        return editAccountDialogFragment;
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
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.activity_edit_account, null);
        editAccountName = dialogView.findViewById(R.id.edit_account_fragment_account_name);
        editAccountDesc = dialogView.findViewById(R.id.edit_account_fragment_account_desc);

        editAccountName.setText(AccountList.getInstance(getContext()).getAccount(accountNumber).accountName);
        editAccountDesc.setText(AccountList.getInstance(getContext()).getAccount(accountNumber).accountDesc);

        accountList = AccountList.getInstance(getContext());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Update Account Details");
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
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
                      Account name must be unique and
                      non-empty.
                    */
                    String s1 = editAccountName.getText().toString().toUpperCase();
                    String s2 = editAccountDesc.getText().toString();
                    if (editAccountName.getText().toString().compareTo("") == 0) {
                        Toast.makeText(getContext(), "Account name " + "must " + "not " + "be empty!", Toast.LENGTH_LONG).show();
                    } else {
                        boolean result = accountList.updateAccount(accountNumber, s1, s2);
                        if (result == false) {
                            Toast.makeText(getContext(), "Account name " + "clashes " + "with another. Please " + "choose a different " + "name!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Account details" + " edited successfully!", Toast.LENGTH_LONG).show();
                            ((MainActivity) getActivity()).reloadFragment();
                            dismiss();
                        }
                    }
                }
            });
        }
    }
}
