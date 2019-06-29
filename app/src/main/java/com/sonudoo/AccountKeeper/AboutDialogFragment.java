package com.sonudoo.AccountKeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AboutDialogFragment extends DialogFragment {
    /**
     * This dialog fragment is used to create the About Dialog Fragment
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.content_about, null);
        /*
            Flat icons credits
         */
        TextView flatIcon = dialogView.findViewById(R.id.flat_icon_text);
        flatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.flaticon.com"));
                startActivity(browserIntent);
            }
        });

        /*
            Gson credits
         */

        TextView gsonLicenseText = dialogView.findViewById(R.id.gson_license_text);
        gsonLicenseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/google/gson/blob/master/LICENSE"));
                startActivity(browserIntent);
            }
        });

        /*
            Rate App
         */

        TextView rateAppText = dialogView.findViewById(R.id.rate_app_text);
        rateAppText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google" + ".com/store/apps/details?id=" + getContext().getPackageName())));
                }
            }
        });

        /*
            Github Icon
         */

        ImageView githubIcon = dialogView.findViewById(R.id.icon_github);
        githubIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/sonudoo"));
                startActivity(browserIntent);
            }
        });

        /*
            LinkedIn Icon
         */

        ImageView linkedInIcon = dialogView.findViewById(R.id.icon_linkedin);
        linkedInIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/sushantkgupta"));
                startActivity(browserIntent);
            }
        });

        /*
            Web Icon
         */

        ImageView webIcon = dialogView.findViewById(R.id.icon_web);
        webIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sonudoo.com/"));
                startActivity(browserIntent);
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("About Account Keeper");
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        return alertDialogBuilder.create();
    }
}
