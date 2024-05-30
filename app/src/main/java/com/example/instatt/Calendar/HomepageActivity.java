package com.example.instatt.Calendar;

import static com.example.instatt.Calendar.CalendarUtils.daysInWeekArray;
import static com.example.instatt.Calendar.CalendarUtils.monthYearFromDate;
import static com.example.instatt.Calendar.CalendarUtils.selectedDate;
import static com.example.instatt.LogIn.userEmail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instatt.Class.ClassDataHandler;
import com.example.instatt.Class.Event;
import com.example.instatt.Class.EventAdapter;
import com.example.instatt.LogIn;
import com.example.instatt.NavigationDrawerHandler;
import com.example.instatt.R;
import com.example.instatt.retrieveUser;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseError;

import java.time.LocalDate;
import java.util.ArrayList;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CalendarAdapter.OnItemListener {
    // Declare UI elements
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView studentName;
    TextView emailAddress;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize UI elements
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the navigation drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set the navigation item as checked
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        // Initialize the header view
        View headerView = navigationView.getHeaderView(0);
        // Find the TextViews for student name and email
        studentName = headerView.findViewById(R.id.student_name);
        emailAddress = headerView.findViewById(R.id.emailAddress);

        // Check if userEmail is null or empty
        if (userEmail == null || userEmail.isEmpty()) {
            // Handle the case where userEmail is null or empty
            Log.e("HomepageActivity", "User email is null or empty");
            // You might want to return, show a message, or handle the error as appropriate
            return;
        }

        // Retrieve user data and set it in the UI
        retrieveUser user = new retrieveUser();
        user.retrieveUserData(userEmail, studentName, emailAddress);

        // Initialize UI widgets and set the default selected date to today
        initWidgets();
        selectedDate = LocalDate.now();
        setWeekView();
    }

    private void initWidgets() {
        // Initialize UI widgets
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView() {
        // Set the month and year text in the UI
        monthYearText.setText(monthYearFromDate(selectedDate));

        // Get the list of days in the selected week
        ArrayList<LocalDate> days = daysInWeekArray(selectedDate);

        // Create and set up the CalendarAdapter for the RecyclerView
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // Set the event adapter to display events for the selected week
        setEventAdapter();

        if (userEmail != null && !userEmail.isEmpty()) {
            setEventAdapter();
            fetchClassDataFromFirebase();
        } else {
            // Handle the case where userEmail is null or empty
            handleMissingUserEmail();
        }
    }

    public void previousWeekAction(View view) {
        // Move to the previous week and update the UI
        selectedDate = selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        // Move to the next week and update the UI
        selectedDate = selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            // Update the selected date and refresh the UI
            selectedDate = date;
            setWeekView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the event adapter and class data from Firebase when the activity resumes
        setEventAdapter();
        fetchClassDataFromFirebase();
    }

    private void setEventAdapter() {
        // Get and display daily events for the selected date
        ArrayList<Event> dailyEvents = Event.eventsForDate(selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        if (eventListView != null) {
            eventListView.setAdapter(eventAdapter);
        }
    }

    public void MonthlyAction(View view) {
        // Start the MonthlyViewActivity
        startActivity(new Intent(this, MonthViewActivity.class));
    }

    private void fetchClassDataFromFirebase() {
        // Fetch class data from Firebase using ClassDataHandler
        ClassDataHandler.fetchClassData(userEmail, selectedDate, new ClassDataHandler.ClassDataListener() {
            @Override
            public void onClassDataFetched(ArrayList<Event> events) {
                // Update the calendar with the fetched events
                updateCalendarWithEvents(events);
            }

            @Override
            public void onClassDataFetchError(DatabaseError databaseError) {
                // Handle the Firebase database error
                Log.w("HomepageActivity", "fetchClassDataFromFirebase:onCancelled", databaseError.toException());
            }
        });
    }

    private void handleMissingUserEmail() {
        // Log the error or notify the user
        Log.e("HomepageActivity", "User email is null or empty. User needs to log in.");

        // Redirect user to the LogIn activity or show an error message
        Intent loginIntent = new Intent(this, LogIn.class);
        startActivity(loginIntent);
        finish(); // Optional: if you want to remove this activity from the back stack
    }

    private void updateCalendarWithEvents(ArrayList<Event> events) {
        // Update the event adapter with the fetched events
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), events);
        if (eventListView != null) {
            eventListView.setAdapter(eventAdapter);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation item selection using NavigationDrawerHandler
        return NavigationDrawerHandler.handleNavigationItemSelected(menuItem, this, drawerLayout);
    }
}


