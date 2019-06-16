package com.sonudoo.AccountKeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.sonudoo.AccountKeeper.MainActivity.ACCOUNT_KEEPER_DIRECTORY;
import static com.sonudoo.AccountKeeper.MainActivity.SEPARATOR_CONSTANT;

class BackupDialogFragment extends DialogFragment {
    static BackupDialogFragment singleton_instance = null;
    private EditText cipherPin;

    static BackupDialogFragment getInstance() {
        if (BackupDialogFragment.singleton_instance == null) {
            singleton_instance = new BackupDialogFragment();
        }
        return singleton_instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.pin_dialog_layout, null);
        cipherPin = dialogView.findViewById(R.id.cipher_pin);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Set a backup encryption pin");
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                    if (cipherPin.getText().toString().length() != 6) {
                        Toast.makeText(getContext(), "PIN must be 6 " + "digit" + " long", Toast.LENGTH_LONG).show();
                    } else {
                        AccountList accountListInstance = AccountList.getInstance(getActivity());
                        TransactionList transactionListInstance = TransactionList.getInstance(getActivity());
                        Gson gson = new Gson();
                        String accountListJson = gson.toJson(accountListInstance.getAccounts());
                        String transactionListJson = gson.toJson(transactionListInstance.getTransactions());
                        Cipher cipher = Cipher.getInstance();
                        int passcode = Integer.parseInt(cipherPin.getText().toString());
                        /*
                         * 1. Convert the UTF-16 string to base 64
                         * string. This would
                         * help in encryption which only supports
                         * each character
                         * represented by maximum of 7-bit only ( <
                         * 128 ASCII).
                         * 2. The combined base 64 string is converted
                         * to encrypted base
                         * 64 string.
                         * 3. The encrypted string is then saved to
                         * output stream using
                         * UTF-8 encoding.
                         */
                        String combinedString = android.util.Base64.encodeToString((accountListJson + SEPARATOR_CONSTANT + transactionListJson).getBytes(), Base64.DEFAULT);

                        String stringtoSave = cipher.encryptString(combinedString, passcode);
                        Log.d("File", stringtoSave);
                        File accountKeeperDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ACCOUNT_KEEPER_DIRECTORY);
                        accountKeeperDirectory.mkdirs();
                        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + ACCOUNT_KEEPER_DIRECTORY;
                        String backupFilename = "backup_" + (new Date().toString()) + ".ak";

                        File backupFile = new File(root, backupFilename);
                        try {
                            FileOutputStream f = new FileOutputStream(backupFile);
                            f.write(stringtoSave.getBytes());
                            f.close();
                            Toast.makeText(getActivity(), "Saved " + "Successfully " + "to " + ACCOUNT_KEEPER_DIRECTORY + " folder", Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getActivity(), "An error " + "occurred. " + "Please make " + "sure" + " that you have provided the " + "storage" + " " + "permissions to the app" + ".", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "An error " + "occurred. " + "Please make " + "sure" + " that you have provided the " + "storage" + " " + "permissions to the app" + ".", Toast.LENGTH_LONG).show();
                        }
                        d.dismiss();
                    }
                }
            });
        }
    }
}
