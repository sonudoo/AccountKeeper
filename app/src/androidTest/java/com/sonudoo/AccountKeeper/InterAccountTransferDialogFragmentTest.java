package com.sonudoo.AccountKeeper;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;

public class InterAccountTransferDialogFragmentTest {

    public AccountList accountList;
    public TransactionList transactionList;
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule(MainActivity.class);

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    @Before
    public void setUp() {
        accountList = AccountList.getInstance(InstrumentationRegistry.getInstrumentation().getContext());
        transactionList = TransactionList.getInstance(InstrumentationRegistry.getInstrumentation().getContext());
    }

    public int getPosition(String accountName) {
        for (int i = 0; i < accountList.getAccounts().size(); i++) {
            if (accountList.getAccounts().get(i).accountName.compareTo(accountName) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void interAccountTransferTest() {
        final Account account1 = new Account(0, "TRANSFERACCOUNT1", "test", 50.00);
        final Account account2 = new Account(0, "TRANSFERACCOUNT2", "test", 0.00);

        // Add the new account
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account1.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account1.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account1.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());

        // Add the new account
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account2.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account2.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account2.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());

        // Add an inter account transfer

        onView(withId(R.id.navigation_accounts)).perform(click());

        onView(withId(R.id.main_activity_account_list)).perform(actionOnItemAtPosition(getPosition(account1.accountName), clickChildViewWithId(R.id.main_activity_transfer_button)));

        onView(withId(R.id.inter_account_transfer_fragment_inter_account_transfer_account_1)).perform(click());
        onData(anything()).atPosition(getPosition(account1.accountName)).perform(click()); // It's not a spinner, it's a drop down

        onView(withId(R.id.inter_account_transfer_fragment_inter_account_transfer_account_2)).perform(click());
        onData(anything()).atPosition(getPosition(account2.accountName)).perform(click()); // It's not a spinner, it's a drop down

        onView(withId(R.id.inter_account_transfer_fragment_inter_account_transfer_amount)).perform(typeText(Double.toString(30.00)));
        onView(withId(android.R.id.button1)).perform(click());

        assertEquals(20.00, accountList.getAccounts().get(getPosition(account1.accountName)).getBalance(), 1e-6);
        assertEquals(30.00, accountList.getAccounts().get(getPosition(account2.accountName)).getBalance(), 1e-6);

        // Transfer a larger amount

        onView(withId(R.id.main_activity_account_list)).perform(actionOnItemAtPosition(getPosition(account1.accountName), clickChildViewWithId(R.id.main_activity_transfer_button)));

        onView(withId(R.id.inter_account_transfer_fragment_inter_account_transfer_account_1)).perform(click());
        onData(anything()).atPosition(getPosition(account1.accountName)).perform(click()); // It's not a spinner, it's a drop down

        onView(withId(R.id.inter_account_transfer_fragment_inter_account_transfer_account_2)).perform(click());
        onData(anything()).atPosition(getPosition(account2.accountName)).perform(click()); // It's not a spinner, it's a drop down

        onView(withId(R.id.inter_account_transfer_fragment_inter_account_transfer_amount)).perform(typeText(Double.toString(30.00)));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.navigation_accounts)).check(doesNotExist());

        assertEquals(20.00, accountList.getAccounts().get(getPosition(account1.accountName)).getBalance(), 1e-6);
        assertEquals(30.00, accountList.getAccounts().get(getPosition(account2.accountName)).getBalance(), 1e-6);

    }
}