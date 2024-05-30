package com.example.instatt.Calendar;

import static com.example.instatt.Calendar.CalendarUtils.daysInWeekArray;
import static com.example.instatt.Calendar.CalendarUtils.monthYearFromDate;
import static com.example.instatt.Calendar.CalendarUtils.selectedDate;
import static com.example.instatt.LogIn.userEmail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instatt.Class.ClassDataHandler;
import com.example.instatt.Class.Event;
import com.example.instatt.Class.EventAdapter;
import com.example.instatt.R;
import com.google.firebase.database.DatabaseError;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        // Initialize widgets and set up the back button click listener
        initWidgets();
        setBackButtonListener();

        // Set up the initial week view and fetch class data from Firebase
        setWeekView();
        fetchClassDataFromFirebase();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setBackButtonListener() {
        button = findViewById(R.id.back_btu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the HomepageActivity
                Intent settingIntent = new Intent(WeekViewActivity.this, HomepageActivity.class);
                startActivity(settingIntent);

                // Finish the WeekViewActivity if you don't want it in the back stack
                finish();
            }
        });
    }

    private void setWeekView() {
        // Set the month and year text based on the selected date
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));

        // Get the days in the selected week
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        // Set up the calendar RecyclerView with the selected week's days
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // Set up the event list for the selected week
        setEventAdapter();
        fetchClassDataFromFirebase();
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
                // Handle the Firebase database error (you can log or show an error message)
                Log.w("Week View Activity", "fetchClassDataFromFirebase:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateCalendarWithEvents(ArrayList<Event> events) {
        // Update the event list view with the fetched events
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), events);
        if (eventListView != null) {
            eventListView.setAdapter(eventAdapter);
        }
    }

    // Handle the previous week button click
    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    // Handle the next week button click
    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        // Handle calendar item click, set the selected date, and update the week view
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the event adapter and fetch class data when the activity resumes
        setEventAdapter();
        fetchClassDataFromFirebase();
    }

    private void setEventAdapter() {
        // Get daily events for the selected date and set the event list adapter
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }
}
