package com.sonudoo.AccountKeeper;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class AddAccountDialogFragmentTest {

    public AccountList accountList;
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        accountList = AccountList.getInstance(InstrumentationRegistry.getInstrumentation().getContext());
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
    public void addAccountTest() {
        final Account account = new Account(0, "ADDACCOUNTTEST", "test", 15.00);
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.navigation_accounts)).perform(click());

        onView(withId(R.id.main_activity_account_list)).perform(scrollToPosition(getPosition(account.accountName)));
        onView(withText(account.accountName.toUpperCase())).check(matches(isDisplayed()));

        assertEquals(account.accountName, accountList.getAccounts().get(getPosition(account.accountName)).accountName);
        assertEquals(account.accountDesc, accountList.getAccounts().get(getPosition(account.accountName)).accountDesc);
        assertEquals(account.getBalance(), accountList.getAccounts().get(getPosition(account.accountName)).getBalance(), 1e-6);
    }

    @Test
    public void addAccountDuplicateNameTest() {
        final Account account = new Account(0, "DUPLICATENAMETEST", "Description 1", 0.00);
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.navigation_accounts)).check(matches(isDisplayed()));

        final Account account2 = new Account(0, "DUPLICATENAMETEST", "Description" + " 2", 12.00);
        onView(withId(R.id.main_activity_add_account_button)).perform(click());
        onView(withId(R.id.add_account_fragment_account_name)).perform(typeText(account2.accountName));
        onView(withId(R.id.add_account_fragment_account_desc)).perform(typeText(account2.accountDesc));
        onView(withId(R.id.add_account_fragment_account_initial_balance)).perform(typeText(Double.toString(account2.getBalance())));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.navigation_accounts)).check(doesNotExist());
    }
}