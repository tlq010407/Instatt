package com.example.instatt.Calendar;

import static com.example.instatt.Calendar.CalendarUtils.daysInMonthArray;
import static com.example.instatt.Calendar.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.instatt.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class MonthViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);

        // Initialize widgets (UI elements)
        initWidgets();

        // Set the initial Month View
        setMonthView();
    }

    private void initWidgets() {
        // Find and initialize the RecyclerView and TextView
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        // Set the text displaying the current month and year
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));

        // Get the list of days in the selected month
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        // Create and set up the CalendarAdapter for the RecyclerView
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, MonthViewActivity.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view) {
        // Move to the previous month and update the Month View
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        // Move to the next month and update the Month View
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            // Handle item click - When a date is clicked, set it as the selected date and update the Month View
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    public void weeklyAction(View view) {
        // Start the Week View Activity when the weekly action button is clicked
        startActivity(new Intent(this, WeekViewActivity.class));
    }
}