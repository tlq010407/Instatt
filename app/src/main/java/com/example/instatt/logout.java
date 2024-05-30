package com.example.instatt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class logout {
    public void LogOut(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to LogOut?");

        // "Yes" button with positive click listener
        builder.setPositiveButton("Yes", (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(context,"Log Out Successfully.",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LogIn.class);
            context.startActivity(intent);
        });

        // "No" button with negative click listener
        builder.setNegativeButton("No", (dialog, which) -> {
            // Dismiss the dialog if the user chooses not to LogOut
            dialog.dismiss();
        });

        builder.show();
    }
}
