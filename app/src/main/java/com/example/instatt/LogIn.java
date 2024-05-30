package com.example.instatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import com.example.instatt.Calendar.HomepageActivity;
import com.google.firebase.auth.FirebaseAuth;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailAddress;
    private EditText password;
    public static String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.Password);
        Button loginButton = findViewById(R.id.login_button);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Set a click listener for the login button
        loginButton.setOnClickListener(v -> {
            // Get the email and password entered by the user
            String email = emailAddress.getText().toString().trim();
            String pwd = password.getText().toString().trim();

            // Check if email or password is empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LogIn.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(LogIn.this, "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use Firebase Authentication to sign in the user
            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(LogIn.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign-in was successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Get the user's email and store it
                                userEmail = user.getEmail();
                                Toast.makeText(LogIn.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                navigateToHomepage(); // Proceed to the homepage
                            }
                        } else {
                            // Sign-in failed, display an error message
                            Toast.makeText(LogIn.this, "Login Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void navigateToHomepage() {
        // Create an intent to navigate to the homepage activity
        Intent homepageIntent = new Intent(LogIn.this, HomepageActivity.class);
        startActivity(homepageIntent);
        finish(); // Finish the LogIn if you don't want it in the back stack
    }
}
