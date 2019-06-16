package com.sonudoo.AccountKeeper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String ACCOUNT_KEEPER_DIRECTORY = "/AccountKeeper";
    public static final String SEPARATOR_CONSTANT = "\n\n@@\n\n";
    /*
      Main Activity has three fragments within -
      1. Base fragment containing Home baseFragmentView.
      2. Account list fragment containing account list
      baseFragmentView.
      3. Transaction list fragment containing transaction list
      baseFragmentView and
      related filter popup views.
     */
    /*
     * isStartupExecuted is set to true only when the user has passed the
     * authentication.
     */
    private final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 2343;
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 2344;
    private final int RESTORE_REQUEST_CODE = 20;
    private boolean isStartupCodeExecuted;
    private FloatingActionButton filterButton;
    private TransactionListFragment transactionListFragment;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(new BaseFragment());
                    filterButton.hide(); // Hide the filter button on Home
                    return true;
                case R.id.navigation_transactions:
                    transactionListFragment = new TransactionListFragment();
                    filterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                              Show the filter pop up window defined in the
                              transaction list fragment baseFragmentView.
                             */
                            transactionListFragment.popupFilterWindow.showAtLocation(transactionListFragment.getView(), Gravity.CENTER, 0, 0);
                            Button applyFilterButton = transactionListFragment.popupFilterWindow.getContentView().findViewById(R.id.pop_up_filter_button);
                            applyFilterButton.requestFocus();
                            transactionListFragment.popupFilterWindow.setElevation(20);
                        }
                    });
                    filterButton.show();
                    showFragment(transactionListFragment);
                    return true;
                case R.id.navigation_accounts:
                    showFragment(new AccountListFragment());
                    filterButton.hide();
                    return true;
            }
            return false;
        }
    };
    private BottomNavigationView navView;


    private void showFragment(Fragment fragment) {
        /*
          Loads a fragment to main activity
         */
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_scroll_view, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("  " + "Account Keeper");
        getSupportActionBar().setIcon(R.drawable.ic_account_balance_wallet_black_24dp);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        startupCode();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, RESTORE_REQUEST_CODE);
                } else {
                    Toast.makeText(this, "Backup/Restore functionality " + "won't " + "work", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    BackupDialogFragment pinDialogFragment = BackupDialogFragment.getInstance();
                    pinDialogFragment.show(getSupportFragmentManager(), "Backup");
                } else {
                    Toast.makeText(this, "Backup/Restore functionality won't " + "work", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void startupCode() {
        /*
          filterButton belongs to main activity baseFragmentView because it
          requires a
          non-scrolling position.
          It must be shown only on transaction list fragment.
         */
        navView = findViewById(R.id.main_activity_bottom_navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        /*
          Base fragment is the default fragment to load
         */
        showFragment(new BaseFragment());

        /*
          Hide the filter button upon activity startup
         */
        filterButton = findViewById(R.id.main_activity_filter_transaction_button);
        filterButton.hide();

        isStartupCodeExecuted = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        /*
          This method handles the result of authentication.
          If authentication is successful then it performs normal startup.
          Else the activity closes itself.
         */
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == RESTORE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                RestoreDialogFragment restoreDialogFragment = new RestoreDialogFragment(uri);
                restoreDialogFragment.show(getSupportFragmentManager(), "Restore");

            }
        }
    }

    @Override
    public void onResume() {
        /*
          This method loads the home fragment whenever the activity resumes
          and has passed authentication
         */
        if (isStartupCodeExecuted) {
            showFragment(new BaseFragment());
            navView.setSelectedItemId(R.id.navigation_home);
        }
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAffinity();
            System.exit(0);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.backup) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                BackupDialogFragment pinDialogFragment = BackupDialogFragment.getInstance();
                pinDialogFragment.show(getSupportFragmentManager(), "Backup");
            }
        } else if (id == R.id.restore) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                startActivityForResult(intent, RESTORE_REQUEST_CODE);
            }
        } else if (id == R.id.share) {
            Intent shareAppIntent = new Intent(Intent.ACTION_SEND);
            shareAppIntent.setType("text/plain");
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, "Download Account " + "Keeper today.");
            startActivity(shareAppIntent);
            return true;
        } else if (id == R.id.settings) {
            Intent settingsActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsActivityIntent);
        } else if (id == R.id.about) {
            AboutDialogFragment aboutDialogFragment = new AboutDialogFragment();
            aboutDialogFragment.show(getSupportFragmentManager(), "About");
        } else if (id == R.id.exit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
