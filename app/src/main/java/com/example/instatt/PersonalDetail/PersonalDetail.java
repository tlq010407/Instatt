package com.example.instatt.PersonalDetail;


import static com.example.instatt.LogIn.userEmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.instatt.NavigationDrawerHandler;
import com.example.instatt.R;
import com.example.instatt.logout;
import com.example.instatt.retrieveUser;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button logout;
    Button change_password;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_att);
        // Initialize the header view
        View headerView = navigationView.getHeaderView(0);
        // Find the TextViews for student name and email
        TextView studentName = headerView.findViewById(R.id.student_name);
        TextView emailAddress = headerView.findViewById(R.id.emailAddress);
        retrieveUser user = new retrieveUser();
        user.retrieveUserData(userEmail, studentName, emailAddress);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_info);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Retrieve the user email passed from the login activity or current user
        TextView username = findViewById(R.id.username);
        TextView id = findViewById(R.id.student_id);
        TextView major = findViewById(R.id.major);
        TextView year = findViewById(R.id.year);

        String sanitizedEmail = user.getStringBeforeAt(userEmail);
        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("students").child(sanitizedEmail);

        // Retrieve and set user data
        retrieveAndSetUserData("Name", username, "Hello! %s");
        retrieveAndSetUserData("StudentID", id, "Student ID: %s");
        retrieveAndSetUserData("Major", major, "Major: %s");
        retrieveAndSetUserData("Year", year, "Year: %s");

        TextView email = findViewById(R.id.user_email);
        email.setText(userEmail);

        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(v -> {
            com.example.instatt.logout logout = new logout();
            logout.LogOut(this);
        });

        change_password = findViewById(R.id.change_password_button);
        change_password.setOnClickListener(v -> {
            ChangePassword dialog = new ChangePassword();
            dialog.show(getSupportFragmentManager(), "changePassword");
        });
    }
    private void retrieveAndSetUserData(String childKey, TextView textView, String format) {
        mDatabase.child(childKey).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String value = String.valueOf(task.getResult().getValue());
                runOnUiThread(() -> textView.setText(String.format(format, value)));
            } else {
                Log.e("firebase", "Error getting data", task.getException());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return NavigationDrawerHandler.handleNavigationItemSelected(menuItem, this, drawerLayout);
    }

}