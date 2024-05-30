package com.example.instatt.Class;


import static com.example.instatt.LogIn.userEmail;
import static com.example.instatt.retrieveUser.getStringBeforeAt;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instatt.Calendar.CalendarUtils;
import com.example.instatt.R;
import com.example.instatt.retrieveUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    // Constructor for the EventAdapter class
    public EventAdapter(@NonNull Context context, List<Event> events) {
        // Call the superclass constructor
        super(context, 0, events);
    }

    // Override the getView method to customize the item view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the Event object at the current position
        Event event = getItem(position);

        // Check if the convertView (item view) is null
        if (convertView == null) {
            // Inflate the layout if it's not already initialized
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.class_item, parent, false);
        }

        // Find views in the layout by their IDs
        TextView eventNameTV = convertView.findViewById(R.id.module_name_view);
        TextView eventStartTimeTV = convertView.findViewById(R.id.time_text_view_start);
        TextView eventEndTimeTV = convertView.findViewById(R.id.time_text_view_end);
        TextView eventPosition = convertView.findViewById(R.id.class_position_view);
        TextView eventType = convertView.findViewById(R.id.class_type_view);
        ImageButton status_icon = convertView.findViewById(R.id.status_icon);
        ImageView type_icon_view = convertView.findViewById(R.id.class_type_icon);
        View line = convertView.findViewById(R.id.vertical_line);
        TextView status_view = convertView.findViewById(R.id.class_status_view);

        // Check if the event object is not null
        if (event != null) {
            // Set event details in the TextViews
            fetchModuleName(event.getName(), eventNameTV);
            //eventNameTV.setText(event.getName());
            eventPosition.setText(event.getRoom());
            eventType.setText(event.getType().toUpperCase());
            LocationColorSet(event, eventPosition);

            // Get class type and event status
            ClassType classType = ClassType.getTypeFromString(event.getType().toUpperCase());
            status_icon.setClickable(false);

            // Handle the event status and set the status icon
            Status eventStatus = Status.getStatusFromString(event.getStatus().toUpperCase());
            status_icon.setImageResource(eventStatus.getIconResourceId());

            // Set the click listener for the status icon if status is "UNLOCK"
            if ("UNLOCK".equalsIgnoreCase(eventStatus.getStatusName())) {
                status_icon.setClickable(true);
                status_icon.setOnClickListener(v -> {
                    status_icon.setImageResource(eventStatus.getIconResourceId());
                    MarkAttendance(event.getDate().toString(), event.getName());
                });
            }

            // Set the image resource for the class type icon
            type_icon_view.setImageResource(classType.getIconResourceId());
            // Set the event start and end times in the TextViews
            eventStartTimeTV.setText(CalendarUtils.formattedTime(event.getStartTime()));
            eventEndTimeTV.setText(CalendarUtils.formattedTime(event.getEndTime()));

            // Set the status view and background color based on event status and date
            StatusSet(event, line, status_view);
        }

        // Return the modified item view
        return convertView;
    }

    // Method to mark attendance for a specific date and moduleCode
    // Method to mark attendance for an event
    private void MarkAttendance(String dateAbsent, String moduleCode) {
        // Extract student code from the email address
        String studentCode = getStringBeforeAt(userEmail);

        // Get a reference to the Firebase database path
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("students").child(studentCode).child("Class")
                .child(moduleCode).child(dateAbsent).child("Status");

        // Set the attendance status in the database to "SIGNED"
        databaseRef.setValue("SIGNED").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Attendance signed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to set the background color and status text based on event status and date
    private void StatusSet(Event event, View line, TextView status_view) {
        // Get the current date, class date, current time, class start time, and class end time
        LocalDate currentDate = LocalDate.now();
        LocalDate classDate = event.getDate();
        LocalTime currentTime = LocalTime.now();
        LocalTime ClassStart = event.getStartTime();
        LocalTime ClassEnd = event.getEndTime();

        // Check if the current date is after the class date
        if (currentDate.isAfter(classDate)) {
            // Set the background color to red and status text to "Status: Over"
            line.setBackgroundColor(Color.parseColor("#C10000"));
            status_view.setText("Status: Over");
        } else if (currentDate.isBefore(classDate)) {
            // Check if the current date is before the class date
            // Set the background color to a different shade and status text to "Status: Coming"
            line.setBackgroundColor(Color.parseColor("#17A2C5"));
            status_view.setText("Status: Coming");
        } else if (currentDate.isEqual(classDate)) {
            // Check if the current date is equal to the class date
            if (currentTime.isAfter(ClassEnd)) {
                // Set the background color to a different shade and status text to "Status: Coming"
                line.setBackgroundColor(Color.parseColor("#C10000"));
                status_view.setText("Status: Over");
            } else if (currentTime.isBefore(ClassStart)) {
                // Check if the current time is before the class start time
                // Set the background color to red and status text to "Status: Over"
                line.setBackgroundColor(Color.parseColor("#17A2C5"));
                status_view.setText("Status: Coming");
            } else {
                // If none of the above conditions match, set the background color to green
                // and status text to "Status: Processing"
                line.setBackgroundColor(Color.parseColor("#008000"));
                status_view.setText("Status: Processing");
            }
        }
    }

    private void fetchModuleName(String moduleCode, TextView eventNameTV) {
        // Extract the student code from the user's email
        String studentCode = retrieveUser.getStringBeforeAt(userEmail);

        // Get a reference to the Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Build the database path to fetch the module name
        DatabaseReference moduleRef = databaseRef
                .child("students")
                .child(studentCode)
                .child("Module")
                .child(moduleCode)
                .child("Name");

        // Use a ValueEventListener to fetch the module name
        moduleRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                // Handle any errors that occurred during the data retrieval
                Log.e("firebase", "Error getting data", task.getException());
            } else if (task.isSuccessful() && task.getResult().exists()) {
                // Data retrieval was successful, and data exists for the specified path
                Log.d("firebase", String.valueOf(task.getResult().getValue()));

                // Retrieve the module name from the data
                String moduleName = task.getResult().getValue(String.class);

                // Ensure we're on the main thread when modifying the UI
                eventNameTV.setText(moduleName);
            }
        });
    }
    private void LocationColorSet(Event event, TextView eventPosition){
        char firstLetter = event.getRoom().charAt(0);
        if (Character.toUpperCase(firstLetter) == 'B'){
            eventPosition.setBackgroundColor(Color.parseColor("#660a99f2"));
        } else if (Character.toUpperCase(firstLetter) == 'E') {
            eventPosition.setBackgroundColor(Color.parseColor("#66f76b07"));
        } else if (Character.toUpperCase(firstLetter) == 'F') {
            eventPosition.setBackgroundColor(Color.parseColor("#660b7547"));
        }else if (Character.toUpperCase(firstLetter) == 'D'){
            eventPosition.setBackgroundColor(Color.parseColor("#668a0af2"));
        } else if (Character.toUpperCase(firstLetter) == 'C') {
            eventPosition.setBackgroundColor(Color.parseColor("#66f20a11"));
        } else if (Character.toUpperCase(firstLetter) == 'N') {
            eventPosition.setBackgroundColor(Color.parseColor("#66c773f5"));
        }
    }

}

