package com.sonudoo.AccountKeeper;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {

    private final int AUTHENTICATION_REQUEST_CODE = 19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                if (keyguardManager.isKeyguardSecure()) {
                    /*
                      If authentication is enabled
                     */
                    Intent authIntent = keyguardManager.createConfirmDeviceCredentialIntent("Authentication", "Authentication required to ");
                    startActivityForResult(authIntent, AUTHENTICATION_REQUEST_CODE);
                } else {
                    /*
                      Else continue with normal startup
                     */
                    Intent mainActivityIntent = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(mainActivityIntent);
                }
            }

        }, 1000L);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        /*
          This method handles the result of authentication.
          If authentication is successful then it performs normal startup.
          Else the activity closes itself.
         */
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == AUTHENTICATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent mainActivityIntent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
            } else {
                finish();
            }
        }
    }
}
