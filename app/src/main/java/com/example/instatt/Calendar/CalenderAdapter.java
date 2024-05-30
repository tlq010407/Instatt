package com.example.instatt.Calendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instatt.Calendar.CalendarUtils;
import com.example.instatt.Calendar.CalendarViewHolder;
import com.example.instatt.R;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days; // List of dates to display
    private final OnItemListener onItemListener; // Listener for item clicks

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days; // Initialize the list of dates
        this.onItemListener = onItemListener; // Initialize the item click listener
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) // Check if it's a month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666); // Set cell height for month view
        else // It's a week view
            layoutParams.height = parent.getHeight(); // Set cell height for week view

        return new CalendarViewHolder(view, onItemListener, days); // Create and return a new CalendarViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position); // Get the date at the current position
        if (date != null) { // Check if the date is not null
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth())); // Set the day of the month
            if (date.equals(CalendarUtils.selectedDate)) { // Check if the date is the selected date
                holder.parentView.setBackgroundColor(Color.LTGRAY); // Set background color to light gray
            } else if (!CalendarUtils.getEventsForDate(date).isEmpty()) { // Check if there are events for the date
                holder.parentView.setBackgroundColor(Color.parseColor("#FFEB3B")); // Set background color to a custom color (example)
            } else { // No events for the date
                holder.parentView.setBackgroundColor(Color.WHITE); // Set background color to white
            }
        } else { // Date is null (not within the current month)
            holder.dayOfMonth.setText(""); // Clear the text
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size(); // Return the total number of items (dates)
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date); // Define an interface for item click events
    }
}