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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class EditAccountActivityDialogFragmentTest {

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
    public void editAccountTest() {
        final Account account = new Account(0, "EDITACCOUNTTEST", "test", 50.00);

        final Account account2 = new Account(0, "EDITACCOUNTEXISITINGTEST", "test", 50.00);

        // Add the new account
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());

        // Edit the account name and description

        onView(withId(R.id.navigation_accounts)).perform(click());

        onView(withId(R.id.main_activity_account_list)).perform(actionOnItemAtPosition(getPosition(account.accountName), clickChildViewWithId(R.id.main_activity_edit_account_button)));

        onView(withId(R.id.edit_account_fragment_account_name)).perform(clearText());
        onView(withId(R.id.edit_account_fragment_account_desc)).perform(clearText());

        onView(withId(R.id.edit_account_fragment_account_name)).perform(typeText("EDITACCOUNTNEWNAME"));
        onView(withId(R.id.edit_account_fragment_account_desc)).perform(typeText("New description"));
        onView(withId(android.R.id.button1)).perform(click());

        assertEquals("EDITACCOUNTNEWNAME", accountList.getAccounts().get(getPosition("EDITACCOUNTNEWNAME")).accountName);
        assertEquals("New description", accountList.getAccounts().get(getPosition("EDITACCOUNTNEWNAME")).accountDesc);

        // Edit the account name to an existing account name

        onView(withId(R.id.navigation_home)).perform(click());

        // Add the new account
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account2.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account2.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account2.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.navigation_accounts)).perform(click());

        onView(withId(R.id.main_activity_account_list)).perform(actionOnItemAtPosition(getPosition("EDITACCOUNTNEWNAME"), clickChildViewWithId(R.id.main_activity_edit_account_button)));
        onView(withId(R.id.edit_account_fragment_account_name)).perform(clearText());
        onView(withId(R.id.edit_account_fragment_account_name)).perform(typeText("EDITACCOUNTEXISITINGTEST"));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.navigation_accounts)).check(doesNotExist());

    }
}