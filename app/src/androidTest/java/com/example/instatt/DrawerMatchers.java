package com.example.instatt;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.drawerlayout.widget.DrawerLayout;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class DrawerMatchers {
    public static Matcher<View> isClosed() {
        return new BoundedMatcher<View, DrawerLayout>(DrawerLayout.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("is closed");
            }

            @Override
            protected boolean matchesSafely(DrawerLayout drawerLayout) {
                return !drawerLayout.isDrawerOpen(GravityCompat.START);
            }
        };
    }
}

