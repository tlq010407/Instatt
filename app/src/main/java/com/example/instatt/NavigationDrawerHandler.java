package com.example.instatt;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.instatt.Calendar.HomepageActivity;
import com.example.instatt.Modules.AttendanceHistory;
import com.example.instatt.PersonalDetail.PersonalDetail;
import com.example.instatt.Setting.Setting;

public class NavigationDrawerHandler {
    public static boolean handleNavigationItemSelected(MenuItem menuItem, Activity activity, DrawerLayout drawerLayout) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.nav_att) {
            // Start the AttendanceHistory activity
            Intent attIntent = new Intent(activity, AttendanceHistory.class);
            activity.startActivity(attIntent);
        } else if (itemId == R.id.nav_info) {
            // Start the PersonalDetail activity
            Intent infoIntent = new Intent(activity, PersonalDetail.class);
            activity.startActivity(infoIntent);
        } else if (itemId == R.id.nav_setting) {
            // Start the Setting activity
            Intent settingIntent = new Intent(activity, Setting.class);
            activity.startActivity(settingIntent);
        } else if (itemId == R.id.nav_logout) {
            // Perform LogOut action
            logout Logout = new logout();
            Logout.LogOut(activity);
        } else if (itemId == R.id.nav_home) {
            // Start the Homepage activity
            Intent homeIntent = new Intent(activity, HomepageActivity.class);
            activity.startActivity(homeIntent);
        }

        // Close the navigation drawer after selecting an item
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}

