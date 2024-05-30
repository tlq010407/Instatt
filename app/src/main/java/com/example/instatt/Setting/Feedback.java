package com.example.instatt.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.instatt.R;

public class Feedback extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        EditText student_name = findViewById(R.id.student_name);
        EditText student_id = findViewById(R.id.student_id);
        EditText feedback_content = findViewById(R.id.feedback_content);
        Button send = findViewById(R.id.send_fed);
        ImageButton back = findViewById(R.id.back_btu);

        send.setOnClickListener(v -> {
            // Create a new intent for sending an email
            Intent intent = new Intent(Intent.ACTION_SEND);

// Set the type of the content to HTML format
            intent.setType("message/rfc822"); // Changed to "message/rfc822" for better email client support

// Put the recipient's email address
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tangliqi010407@outlook.com"}); // Put email in a String array

// Put the subject of the email
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from Instatt");

// Construct the body of the email with student details and feedback
            String emailBody = "Name: " + student_name.getText().toString() +
                    "\nStudent ID: " + student_id.getText().toString() +
                    "\nMessage: " + feedback_content.getText().toString();
            intent.putExtra(Intent.EXTRA_TEXT, emailBody);

// Start an activity to send the email
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Send Email"));
            }

            try {
                startActivity(Intent.createChooser(intent, "Please select Email"));
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(Feedback.this, "There are no Email Clients", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(v -> {
            Intent settingIntent = new Intent(Feedback.this, Setting.class);
            startActivity(settingIntent);
            // Finish the LogIn if you don't want it in the back stack
            finish();
        });
    }
}