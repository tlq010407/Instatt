package com.example.instatt;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

import com.example.instatt.Setting.Feedback;

@RunWith(AndroidJUnit4.class)
public class FeedbackTest {

    @Rule
    public IntentsTestRule<Feedback> intentsTestRule = new IntentsTestRule<>(Feedback.class);
    @Test
    public void testBackButton() {
        // Click on the back button
        onView(withId(R.id.back_btu)).perform(click());

        // Verify that the activity finishes
        assertTrue(intentsTestRule.getActivity().isFinishing());
    }
}

