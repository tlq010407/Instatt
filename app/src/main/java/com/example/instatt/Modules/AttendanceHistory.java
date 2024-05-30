package com.example.instatt.Modules;

import static com.example.instatt.LogIn.userEmail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instatt.NavigationDrawerHandler;
import com.example.instatt.R;
import com.example.instatt.retrieveUser;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class AttendanceHistory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ModuleAdapter moduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        // Initialize views and components
        drawerLayout = findViewById(R.id.drawer_att);
        navigationView = findViewById(R.id.nav_view_att);
        toolbar = findViewById(R.id.toolbar_att);
        setSupportActionBar(toolbar);

        // Make the navigation drawer icon clickable
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set the navigation item click listener
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_att);

        // Initialize the header view and retrieve user data for the navigation drawer header
        View headerView = navigationView.getHeaderView(0);
        TextView studentName = headerView.findViewById(R.id.student_name);
        TextView emailAddress = headerView.findViewById(R.id.emailAddress);
        retrieveUser user = new retrieveUser();
        user.retrieveUserData(userEmail, studentName, emailAddress);
        String studentCode = retrieveUser.getStringBeforeAt(userEmail);

        // Initialize the RecyclerView for modules
        recyclerView = findViewById(R.id.modules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configure FirebaseRecyclerAdapter to display modules for the current student
        FirebaseRecyclerOptions<Module> options =
                new FirebaseRecyclerOptions.Builder<Module>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students").child(studentCode).child("Module"), Module.class)
                        .build();
        moduleAdapter = new ModuleAdapter(options);
        recyclerView.setAdapter(moduleAdapter);

        // Set an item click listener for modules to navigate to module details
        moduleAdapter.setOnItemClickListener(position -> {
            String moduleName = moduleAdapter.getItem(position).getName();
            String moduleCode = moduleAdapter.getItem(position).ModuleCode;
            Intent moduleIntent = new Intent(AttendanceHistory.this, ModuleDetails.class);
            moduleIntent.putExtra("ModuleName", moduleName);
            moduleIntent.putExtra("ModuleCode", moduleCode);
            startActivity(moduleIntent);
            finish();
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation item selection using a helper method
        return NavigationDrawerHandler.handleNavigationItemSelected(menuItem, this, drawerLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in the moduleAdapter (FirebaseRecyclerAdapter)
        moduleAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes when the activity is not in the foreground
        moduleAdapter.stopListening();
    }
}