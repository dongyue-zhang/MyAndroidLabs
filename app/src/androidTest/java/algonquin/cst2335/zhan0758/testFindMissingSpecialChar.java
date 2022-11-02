package algonquin.cst2335.zhan0758;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import algonquin.cst2335.zhan0758.ui.MainActivity;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class testFindMissingSpecialChar {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    /**
     * Tests password with special character missing
     */
    @Test
    public void testMissingSpecialChar() {
        ViewInteraction appCompatEditText = onView(withId(R.id.password_filed));
        appCompatEditText.perform(click());

        appCompatEditText.perform(replaceText("Password123"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.login_button));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.password_message));
        textView.check(matches(withText("You shall not pass")));
    }
}
