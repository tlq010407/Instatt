package com.example.instatt.Modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.example.instatt.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AbsentForm extends AppCompatActivity {
    private TextView textViewDate;
    private Button document_sub;
    private EditText first_name;
    private EditText last_name;
    private EditText reason_content;
    private EditText student_id;
    private EditText major ;
    private TextView module_name, absent_date;
    private CheckBox checkBox;
    private Uri fileUri;
    private EditText signature;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_MEDIA = 1;
    String moduleCode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_form);

        // Initialize UI elements
        textViewDate = findViewById(R.id.textView_date);
        Button buttonSubmit = findViewById(R.id.button_submit);
        document_sub = findViewById(R.id.docu_submit);
        student_id = findViewById(R.id.student_id);
        module_name = findViewById(R.id.module_name);
        major = findViewById(R.id.major);
        absent_date = findViewById(R.id.date_absent);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        reason_content = findViewById(R.id.reason);
        ImageButton back_btu = findViewById(R.id.back_btu);
        checkBox = findViewById(R.id.checkbox);
        signature = findViewById(R.id.signature);

        // Get module information from the previous activity
        String moduleName = getIntent().getStringExtra("moduleName");
        String absent_day = getIntent().getStringExtra("DateAbsent");
        moduleCode = getIntent().getStringExtra("moduleCode");

        // Set a click listener for the date TextView
        textViewDate.setOnClickListener(v -> showDatePickerDialog());

        // Handle back button click
        back_btu.setOnClickListener(v -> {
            Intent intent = new Intent(AbsentForm.this, ModuleDetails.class);
            intent.putExtra("ModuleCode", moduleCode);
            intent.putExtra("ModuleName", moduleName);
            startActivity(intent);
            finish();
        });

        // Handle document submission button click
        document_sub.setOnClickListener(v -> requestMediaPermissions());

        // Set module name and absent date
        module_name.setText(moduleName);
        absent_date.setText(absent_day);

        // Handle form submission button click
        buttonSubmit.setOnClickListener(v -> submitForm());
    }

    // Function to submit the form
    private void submitForm() {
        // Check if all fields are filled
        if (areAllFieldsFilled()) {
            if (checkBox.isChecked()) {
                // Create an email intent
                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setType("*/*"); // Set to a generic MIME type
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"instatt.attendance@nottingham.edu.my"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Absent Form");

                // Create email content
                String emailText = "First Name: " + first_name.getText() + "\nLast Name: " + last_name.getText() +
                        "\nStudent ID: " + student_id.getText() +
                        "\nMajor: " + major.getText() +
                        "\nModule Name: " + module_name.getText() +
                        "\nAbsent Date: " + absent_date.getText() + "\nMessage: " + reason_content.getText();
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);

                // Attach a file if selected
                if (fileUri != null) {
                    ArrayList<Uri> uris = new ArrayList<>();
                    uris.add(fileUri); // Add your file URI here
                    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                }

                // Mark as excused absent
                markAsExcusedAbsent();

                // Start an email client or show a toast if no clients are available
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AbsentForm.this, "There are no Email Clients", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AbsentForm.this, "Please check the checkbox.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AbsentForm.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to check if all form fields are filled
    private boolean areAllFieldsFilled() {
        return !TextUtils.isEmpty(first_name.getText())
                && !TextUtils.isEmpty(last_name.getText())
                && !TextUtils.isEmpty(student_id.getText())
                && !TextUtils.isEmpty(major.getText())
                && !TextUtils.isEmpty(module_name.getText())
                && !TextUtils.isEmpty(absent_date.getText())
                && !TextUtils.isEmpty(reason_content.getText())
                && !TextUtils.isEmpty(signature.getText());
    }

    // Function to mark the absence as excused
    private void markAsExcusedAbsent() {
        String studentCode = getIntent().getStringExtra("studentCode");
        String dateAbsent = absent_date.getText().toString();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("students").child(studentCode)
                .child("Class")
                .child(moduleCode)
                .child(dateAbsent)
                .child("Status");

        // Set the status to "Excused Absent"
        databaseRef.setValue("Excused Absent").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AbsentForm.this, "Status updated to Excused Absent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AbsentForm.this, "Failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to show a date picker dialog
    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                    textViewDate.setText(selectedDate); // Set the selected date on the textViewDate
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Function to request media permissions for attaching files
    private void requestMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsNeeded = new ArrayList<>();

            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
            }
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_VIDEO);
            }
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_AUDIO);
            }

            if (!permissionsNeeded.isEmpty()) {
                requestPermissions(permissionsNeeded.toArray(new String[0]), PERMISSION_REQUEST_MEDIA);
            } else {
                openFilePicker();
            }
        }
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_MEDIA) {
            boolean allPermissionsGranted = true;

            // Check if all requested permissions have been granted
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // All permissions are granted. You can perform the action that requires these permissions
                openFilePicker();
            } else {
                // Permissions are denied. You can notify the user and guide them to enable permissions manually if necessary
                Toast.makeText(this, "Permission denied! You need to grant permissions to attach files.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Function to open the file picker
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allows any file type. You can be specific for documents like "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), PICK_FILE_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the file picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the Uri of the selected file
            fileUri = data.getData();
            // You can display the file path or do other actions with the file URI
            String filePath = fileUri.getPath();
            Toast.makeText(this, "File Selected: " + filePath, Toast.LENGTH_LONG).show();
            document_sub.setText("Document Uploaded Successful.");
        }
    }
}