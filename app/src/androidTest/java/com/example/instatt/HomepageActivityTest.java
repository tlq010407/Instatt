package com.example.instatt;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.instatt.Calendar.HomepageActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomepageActivityTest {

    @Rule
    public ActivityTestRule<HomepageActivity> activityRule = new ActivityTestRule<>(HomepageActivity.class, true, false);
    @Before
    public void setUp() {
        // Set a non-null userEmail before launching the activity
        LogIn.userEmail = "hfylt5@nottingham.edu.my";
        activityRule.launchActivity(null);
    }
    @Test
    public void testNavigationDrawerInteraction() {
        // Open Drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on a navigation item
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_home));

        // Check if the drawer is closed
        onView(withId(R.id.drawer_layout)).check(matches(DrawerMatchers.isClosed()));
    }
    @Test
    public void testRecyclerViewIsPopulated() {
        // Wait for data to load using Idling Resources or Thread.sleep (not recommended)

        // Check if the RecyclerView is displayed
        onView(withId(R.id.calendarRecyclerView)).check(matches(isDisplayed()));

        // Scroll to a position to ensure data is loaded in the RecyclerView
        onView(withId(R.id.calendarRecyclerView)).perform(RecyclerViewActions.scrollToPosition(0));

        // Check if an item at position 0 is displayed (you can improve this by checking for specific content)
        onView(withId(R.id.calendarRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

}

