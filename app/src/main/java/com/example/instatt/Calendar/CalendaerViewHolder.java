package com.example.instatt.Calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instatt.R;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;

    // Constructor for CalendarViewHolder
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days) {
        super(itemView);

        // Initialize views and data
        parentView = itemView.findViewById(R.id.parentView); // Parent view of the item
        dayOfMonth = itemView.findViewById(R.id.cellDayText); // TextView to display day of the month
        this.onItemListener = onItemListener; // Listener for item click events
        itemView.setOnClickListener(this); // Set an OnClickListener on the item view
        this.days = days; // List of dates associated with the calendar
    }

    @Override
    public void onClick(View view) {
        int position = getBindingAdapterPosition(); // Get the adapter position of the clicked item
        if (position != RecyclerView.NO_POSITION) {
            // Call onItemClick method of the listener with the clicked position and date
            onItemListener.onItemClick(position, days.get(position));
        }
    }
}
