package com.sonudoo.AccountKeeper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AccountListFragment extends Fragment {

    private final boolean attachToRoot = false;
    private AccountListAdapter accountListAdapter;
    /**
     * This class holds views and data for the account list fragment.
     */

    private AccountList accountListInstance;
    private View accountListView;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        accountListView = inflater.inflate(R.layout.content_list_accounts, container, attachToRoot);

        RecyclerView accountList = accountListView.findViewById(R.id.main_activity_account_list);
        final int spanCount = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        accountList.setLayoutManager(gridLayoutManager);
        accountListInstance = AccountList.getInstance(mContext);
        accountListAdapter = new AccountListAdapter(getActivity(), accountListInstance.getAccounts());
        accountList.setAdapter(accountListAdapter);
        return accountListView;
    }

    @Override
    public void onResume() {
        super.onResume();
        accountListAdapter.notifyDataSetChanged();
            /*
              If there are no accounts, then no accounts text is displayed.
             */
        TextView noAccountsText = accountListView.findViewById(R.id.main_activity_no_account_text);
        if (accountListInstance.getAccounts().size() == 0) {
            noAccountsText.setVisibility(View.VISIBLE);
        } else {
            noAccountsText.setVisibility(View.INVISIBLE);
        }
    }
}