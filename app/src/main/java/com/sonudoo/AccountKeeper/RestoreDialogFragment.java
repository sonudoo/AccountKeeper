package com.sonudoo.AccountKeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class RestoreDialogFragment extends DialogFragment {
    private final Uri uri;
    private EditText cipherPin;

    RestoreDialogFragment(Uri uri) {
        this.uri = uri;
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

        alertDialogBuilder.setTitle("Enter the pin to decrypt the backup");
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
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                            byte[] tmpArray = new byte[10000000];
                            int data;
                            int filePtr = 0;
                            while ((data = inputStream.read()) != -1) {
                                tmpArray[filePtr++] = (byte) data;
                            }
                            byte[] tmpArrayCopy;
                            tmpArrayCopy = Arrays.copyOf(tmpArray, filePtr);

                            Cipher cipher = Cipher.getInstance();

                            int passcode = Integer.parseInt(cipherPin.getText().toString());


                            String encryptedString = new String(tmpArrayCopy); // Base 64
                            // encrypted string

                            String decryptedString = cipher.decryptString(encryptedString, passcode); // Base 64
                            // decrypted string
                            String originalString = new String(android.util.Base64.decode(decryptedString, Base64.DEFAULT));
                            String[] _d = originalString.split(MainActivity.SEPARATOR_CONSTANT);
                            Gson gson = new Gson();
                            Account[] newAccountList = gson.fromJson(_d[0], Account[].class);
                            Transaction[] newTransactionList = gson.fromJson(_d[1], Transaction[].class);

                            AccountList accountListInstance = AccountList.getInstance(getActivity());
                            accountListInstance.restoreDatabase(newAccountList);

                            TransactionList transactionListInstance = TransactionList.getInstance(getActivity());
                            transactionListInstance.restoreDatabase(newTransactionList);
                            ((MainActivity) getActivity()).reloadFragment();
                            d.dismiss();
                            Toast.makeText(getContext(), "Restore " + "Successful!", Toast.LENGTH_LONG).show();


                        } catch (FileNotFoundException e) {
                            Toast.makeText(getContext(), "An error " + "occurred. " + "Please make " + "sure" + " that you have " + "provided the " + "storage " + "permissions to the app.", Toast.LENGTH_LONG).show();
                            d.dismiss();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "An error " + "occurred. " + "Please make " + "sure" + " that you have " + "provided the " + "storage " + "permissions to the app.", Toast.LENGTH_LONG).show();
                            d.dismiss();

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Incorrect PIN " + "or File is " + "corrupted.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
}