package com.example.instatt.Class;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.instatt.retrieveUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ClassDataHandler {
    // Define a listener interface to handle the fetched events
    public interface ClassDataListener {
        void onClassDataFetched(ArrayList<Event> events);
        void onClassDataFetchError(DatabaseError databaseError);
    }

    // Fetch class data from Firebase and notify the listener
    public static void fetchClassData(String userEmail, LocalDate selectedDate, ClassDataListener listener) {
        String studentCode = retrieveUser.getStringBeforeAt(userEmail);

        // Get a reference to the Firebase database for class data
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("students").child(studentCode).child("Class");

        // Add a ValueEventListener to fetch and update class events
        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Event> events = new ArrayList<>();
                for (DataSnapshot moduleSnapshot : dataSnapshot.getChildren()) {
                    String moduleCode = moduleSnapshot.getKey(); // e.g., "COMP3040"
                    for (DataSnapshot dateSnapshot : moduleSnapshot.getChildren()) {
                        String dateKey = dateSnapshot.getKey(); // e.g., "09-27-2023"
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate date = LocalDate.parse(dateKey, dateFormatter);

                        // Check if the event date matches the selected date
                        if (selectedDate.equals(date)) {
                            ClassItem classData = dateSnapshot.getValue(ClassItem.class);
                            if (classData != null) {
                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                                LocalTime startTime = LocalTime.parse(classData.getStartTime(), timeFormatter);
                                LocalTime endTime = LocalTime.parse(classData.getEndTime(), timeFormatter);
                                String type = classData.getType();
                                String status = classData.getStatus();
                                String room = classData.getRoom();

                                Event event = new Event(moduleCode, type, date, startTime, endTime, room, status);
                                events.add(event);
                            }
                        } else {
                            // Log date information for debugging
                            Log.d("selected date", selectedDate.toString());
                            Log.d("date", date.toString());
                        }
                    }
                }
                // Notify the listener with the fetched events
                listener.onClassDataFetched(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Notify the listener of the Firebase database error
                listener.onClassDataFetchError(databaseError);
            }
        });
    }
}

