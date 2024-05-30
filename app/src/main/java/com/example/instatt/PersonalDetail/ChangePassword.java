package com.example.instatt.PersonalDetail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.instatt.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends DialogFragment {
    // Define a constant string for identifying this dialog
    public static final String CHANGE_PASSWORD = "changePassword";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;

        // Check the tag of the dialog to determine its type
        if (getTag().equals(CHANGE_PASSWORD)) {
            dialog = changePassword(getContext()); // Create the change password dialog
        }
        return dialog;
    }

    // Custom method to create the change password dialog
    public Dialog changePassword(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.change_pawssword, null);
        builder.setView(view);

        // Initialize UI elements from the layout
        EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;
        Button changePasswordButton;

        oldPasswordEditText = view.findViewById(R.id.old_password);
        newPasswordEditText = view.findViewById(R.id.new_password);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password);
        changePasswordButton = view.findViewById(R.id.change_password_button);

        // Initialize Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Set an onClickListener for the "Change Password" button
        changePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            // Check if the new passwords match
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the currently signed-in user
            FirebaseUser user = mAuth.getCurrentUser();

            // Check if the user is signed in and the old password is provided
            if (user != null && oldPassword.length() > 0) {
                // Create an AuthCredential for authentication
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

                // authenticate the user with the provided old password
                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update the user's password with the new password
                        user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                dismiss(); // Dismiss the dialog upon successful password change
                            } else {
                                Toast.makeText(context, "Failed to change password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

        // Create and display the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
