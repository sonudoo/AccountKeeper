package com.sonudoo.AccountKeeper;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

public class AddTransactionDialogFragmentTest {

    public AccountList accountList;
    public TransactionList transactionList;
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule(MainActivity.class);

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
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
    public void addTransactionTest() {
        final Account account = new Account(0, "ADDTRANSACTIONTEST", "test", 50.00);

        // Add the new account
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());

        // Add an income transaction

        onView(withId(R.id.main_activity_add_transaction_button)).perform(click());
        onView(withId(R.id.add_transaction_fragment_transaction_account)).perform(click());

        onData(anything()).atPosition(getPosition(account.accountName)).perform(click()); // It's not a spinner, it's a drop down

        onView(withId(R.id.add_transaction_fragment_transaction_account)).check(matches(withSpinnerText(containsString(account.accountName))));
        onView(withId(R.id.add_transaction_fragment_transaction_journal_entry)).perform(typeText("Add income test"));
        onView(withId(R.id.add_transaction_fragment_transaction_amount)).perform(typeText(Double.toString(30.00)));
        onView(withId(R.id.add_transaction_fragment_transaction_type_1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.navigation_transactions)).perform(click());

        // Scroll not required, as the transaction would be at the top

        onView(withIndex(withText(account.accountName.toUpperCase()), 0)).check(matches(isDisplayed()));
        onView(withIndex(withText("From Income Account"), 0)).check(matches(isDisplayed()));
        onView(withIndex(withText("Being Add income test"), 0)).check(matches(isDisplayed()));


        assertEquals(80.00, accountList.getAccounts().get(getPosition(account.accountName)).getBalance(), 1e-6);

        // Add a expense transaction

        onView(withId(R.id.navigation_home)).perform(click());

        onView(withId(R.id.main_activity_add_transaction_button)).perform(click());
        onView(withId(R.id.add_transaction_fragment_transaction_account)).perform(click());

        onData(anything()).atPosition(getPosition(account.accountName)).perform(click());

        onView(withId(R.id.add_transaction_fragment_transaction_account)).check(matches(withSpinnerText(containsString(account.accountName))));
        onView(withId(R.id.add_transaction_fragment_transaction_journal_entry)).perform(typeText("Add expense test"));
        onView(withId(R.id.add_transaction_fragment_transaction_amount)).perform(typeText(Double.toString(45.00)));
        onView(withId(R.id.add_transaction_fragment_transaction_type_2)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.navigation_transactions)).perform(click());

        // Scroll not required, as the transaction would be at the top

        onView(withIndex(withText(account.accountName.toUpperCase()), 0)).check(matches(isDisplayed()));
        onView(withIndex(withText("To Expense Account"), 0)).check(matches(isDisplayed()));
        onView(withText("Being Add expense test")).check(matches(isDisplayed()));

        assertEquals(35.00, accountList.getAccounts().get(getPosition(account.accountName)).getBalance(), 1e-6);
    }

    @Test
    public void addTransactionAmountTest() {
        final Account account = new Account(0, "ADDTRANSACTIONAMOUNTTEST", "Description", 10.00);
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());


        onView(withId(R.id.main_activity_add_transaction_button)).perform(click());
        onView(withId(R.id.add_transaction_fragment_transaction_account)).perform(click());
        onData(anything()).atPosition(getPosition(account.accountName)).perform(click()); // It's not a spinner, it's a drop down

        onView(withId(R.id.add_transaction_fragment_transaction_account)).check(matches(withSpinnerText(containsString(account.accountName))));
        onView(withId(R.id.add_transaction_fragment_transaction_journal_entry)).perform(typeText("Add expense test"));
        onView(withId(R.id.add_transaction_fragment_transaction_amount)).perform(typeText(Double.toString(30.00)));
        onView(withId(R.id.add_transaction_fragment_transaction_type_2)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.navigation_transactions)).check(doesNotExist());
    }
}