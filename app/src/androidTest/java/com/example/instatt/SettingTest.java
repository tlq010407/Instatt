package com.example.instatt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.instatt.Setting.Feedback;
import com.example.instatt.Setting.NotificationAlarm;
import com.example.instatt.Setting.Setting;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SettingTest {

    @Rule
    public IntentsTestRule<Setting> intentsTestRule = new IntentsTestRule<>(Setting.class);
    @Test
    public void testNotificationSettingButton() {
        // Click on the back button
        onView(withId(R.id.btn_notification)).perform(click());

        // Verify if NotificationAlarm activity is started
        intended(hasComponent(NotificationAlarm.class.getName()));

        // Verify that the activity finishes
        assertTrue(intentsTestRule.getActivity().isFinishing());
    }

    @Test
    public void testFeedbackSettingButton() {
        // Click on the back button
        onView(withId(R.id.btn_feedback)).perform(click());

        // Verify if NotificationAlarm activity is started
        intended(hasComponent(Feedback.class.getName()));

        // Verify that the activity finishes
        assertTrue(intentsTestRule.getActivity().isFinishing());
    }
}
