package com.example.instatt.Setting;

import static com.example.instatt.LogIn.userEmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.instatt.NavigationDrawerHandler;
import com.example.instatt.R;
import com.example.instatt.retrieveUser;
import com.google.android.material.navigation.NavigationView;


public class Setting extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button feedbackButton;
    Button notificationSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Initialize UI elements
        drawerLayout = findViewById(R.id.drawer_setting);
        navigationView = findViewById(R.id.nav_setting);
        toolbar = findViewById(R.id.toolbar_setting);
        feedbackButton = findViewById(R.id.btn_feedback);

        // Make the navigation view intractable
        navigationView.bringToFront();

        // Set up the ActionBarDrawerToggle for navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set the selected item in the navigation drawer
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_setting);

        // Initialize the header view of the navigation drawer
        View headerView = navigationView.getHeaderView(0);

        // Find the TextViews for student name and email in the header
        TextView studentName = headerView.findViewById(R.id.student_name);
        TextView emailAddress = headerView.findViewById(R.id.emailAddress);

        // Create an instance of the retrieveUser class to retrieve user data
        retrieveUser user = new retrieveUser();
        // Retrieve and set user data (student name and email) in the header
        user.retrieveUserData(userEmail, studentName, emailAddress);

        // Initialize and set an OnClickListener for the notification settings button
        notificationSetting = findViewById(R.id.btn_notification);
        notificationSetting.setOnClickListener(v -> {
            // Start the NotificationAlarm activity
            Intent notificationIntent = new Intent(Setting.this, NotificationAlarm.class);
            startActivity(notificationIntent);
            finish(); // Finish the current activity
        });

        // Initialize and set an OnClickListener for the feedback button
        feedbackButton = findViewById(R.id.btn_feedback);
        feedbackButton.setOnClickListener(v -> {
            // Start the Feedback activity
            Intent feedbackIntent = new Intent(Setting.this, Feedback.class);
            startActivity(feedbackIntent);
            finish(); // Finish the current activity
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation item selection using the NavigationDrawerHandler
        return NavigationDrawerHandler.handleNavigationItemSelected(menuItem, this, drawerLayout);
    }
}
