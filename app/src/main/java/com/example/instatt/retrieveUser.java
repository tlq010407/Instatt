package com.example.instatt;

import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class retrieveUser {
    public void retrieveUserData(String userEmail, TextView studentName, TextView emailAddress) {
        // Extract the sanitized email (user identifier) from the full email
        String sanitizedEmail = getStringBeforeAt(userEmail);

        // Get a reference to the Firebase Realtime Database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (sanitizedEmail != null) {
            // Build the database path to fetch the user's name
            DatabaseReference nameRef = mDatabase.child("students").child(sanitizedEmail).child("Name");

            // Use a ValueEventListener to fetch the user's name
            nameRef.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    // Handle any errors that occurred during the data retrieval
                    Log.e("firebase", "Error getting data", task.getException());
                } else if (task.isSuccessful() && task.getResult().exists()) {
                    // Data retrieval was successful, and data exists for the specified path
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    // Retrieve the user's name from the data
                    String studentNameText = task.getResult().getValue(String.class);

                    // Ensure on the main thread when modifying the UI
                    studentName.setText(studentNameText);
                    emailAddress.setText(userEmail);
                }
            });
        } else {
            // Handle the case where the sanitized email is null or empty
            Log.e("retrieveUser", "Sanitized email is null or empty");
        }
    }

    // A utility method to extract the string before the "@" symbol in an email
    public static String getStringBeforeAt(String email) {
        if (email == null || !email.contains("@")) {
            return null; // Or handle this case as you see fit
        }
        return email.split("@")[0];
    }
}

