package com.example.instatt;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import com.example.instatt.Calendar.HomepageActivity;

@RunWith(AndroidJUnit4.class)
public class LogInTest {

    @Rule
    public IntentsTestRule<LogIn> intentsTestRule = new IntentsTestRule<>(LogIn.class);

    @Test
    public void testSuccessfulLogin() {
        // Type email and password
        onView(withId(R.id.emailAddress)).perform(typeText("hfylt5@nottingham.edu.my"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.Password)).perform(typeText("20259468"));

        // Close soft keyboard if necessary
        closeSoftKeyboard();

        // Click on the login button
        onView(withId(R.id.login_button)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check if the HomePageActivity is opened
        intended(hasComponent(HomepageActivity.class.getName()));
    }

    @Test
    public void testInvalidLoginAttempt() {
        // Type incorrect email and password
        onView(withId(R.id.emailAddress)).perform(typeText("wrong@example.com"));
        onView(withId(R.id.Password)).perform(typeText("wrongPassword"));

        // Close soft keyboard if necessary
        closeSoftKeyboard();

        // Click on the login button
        onView(withId(R.id.login_button)).perform(click());
    }

    @Test
    public void testUIElementsVisibility() {
        onView(withId(R.id.emailAddress)).check(matches(isDisplayed()));
        onView(withId(R.id.Password)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }
}



