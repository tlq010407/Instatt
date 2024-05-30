package com.example.instatt.Modules;

import static com.example.instatt.LogIn.userEmail;
import static com.example.instatt.retrieveUser.getStringBeforeAt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instatt.Class.ClassAdapter;
import com.example.instatt.Class.ClassItem;
import com.example.instatt.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;



public class ModuleDetails extends AppCompatActivity {
    Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView textViewProgress;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    TextView total_class_count, attend_class_count, absent_class_count, excused_absent_class_count;
    Query query;
    String moduleCode, moduleName, studentCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_details);

        toolbar = findViewById(R.id.toolbar);
        TextView module_name = findViewById(R.id.module_name);
        ImageButton back = findViewById(R.id.back_btu);
        ImageButton save = findViewById(R.id.save_btu);
        progressBar = findViewById(R.id.progress_bar);
        textViewProgress = findViewById(R.id.text_view_progress);
        total_class_count = findViewById(R.id.total_class_view);
        attend_class_count = findViewById(R.id.attend_class_view);
        absent_class_count = findViewById(R.id.absent_class_view);
        excused_absent_class_count = findViewById(R.id.excused_absent_class_view);

        moduleCode = getIntent().getStringExtra("ModuleCode");
        studentCode = getStringBeforeAt(userEmail);
        moduleName = getIntent().getStringExtra("ModuleName");
        module_name.setText(moduleName);

        back.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {
                intent = new Intent(ModuleDetails.this, AttendanceHistory.class);
                startActivity(intent);
                // Finish the LogIn if you don't want it in the back stack
                finish();
            }
        });

        // Initialize RecyclerView and set LayoutManager
        recyclerView = findViewById(R.id.classes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Prepare query for FirebaseRecyclerOptions
        query = FirebaseDatabase.getInstance().getReference().child("students").child(studentCode).child("Class").child(moduleCode);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalClasses = (int) dataSnapshot.getChildrenCount();
                int locked = 0, signed = 0, absent = 0, excusedAbsent = 0;

                // After getting the count, initialize the adapter
                FirebaseRecyclerOptions<ClassItem> options = new FirebaseRecyclerOptions.Builder<ClassItem>()
                        .setQuery(query, ClassItem.class)
                        .build();

                classAdapter = new ClassAdapter(options, ModuleDetails.this, studentCode, moduleName, moduleCode);
                recyclerView.setAdapter(classAdapter);
                classAdapter.startListening();

                for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                    ClassItem classItem = classSnapshot.getValue(ClassItem.class);
                    if (classItem != null) {
                        String status = classItem.getStatus().toLowerCase(); // Convert to lowercase
                        switch (status) {
                            case "lock":
                                locked++;
                                break;
                            case "signed":
                                signed++;
                                break;
                            case "absent":
                                absent++;
                                break;
                            case "excused absent":
                                excusedAbsent++;
                                break;
                            // Add more cases as needed, using lowercase status values
                        }
                    }
                }
                totalClasses = totalClasses - locked;
                // Now you have the count of each status, you can update your UI accordingly
                total_class_count.setText("Total Class: " + totalClasses);
                attend_class_count.setText("Attend: " + signed);
                absent_class_count.setText("Absent: " + absent);
                excused_absent_class_count.setText("Excused Absence: " + excusedAbsent);
                double progress = (double) signed / totalClasses * 100;
                updateProgressBar(progress);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(v -> exportDataToPDF());
    }
    private void updateProgressBar(double progress) {
        if (!Double.isNaN(progress)) {
            progressBar.setProgress((int) progress);
            textViewProgress.setText((int)progress + "%");
        } else {
            progressBar.setProgress(0);
            textViewProgress.setText("0%");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (classAdapter != null) {
            classAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (classAdapter != null) {
            classAdapter.stopListening();
        }
    }

    private void exportDataToPDF() {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    // Step 1: Create a document.
                    String path = getExternalFilesDir(null) + "/MyDataset.pdf"; // Path to save file
                    PdfWriter writer = new PdfWriter(path);
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);
                    // Step 2: Parse data and add to document.
                    document.add(new Paragraph("Module Name: " + moduleName + " "+moduleCode+
                            "\n Student ID:" + studentCode));
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Assume ClassItem is a model class for your data
                        ClassItem item = snapshot.getValue(ClassItem.class);
                        // Add data to the document
                        document.add(new Paragraph("Date: " +item.getDate()+" "+item.getStartTime()+"-"+item.getEndTime()+
                                " Type: "+item.getType()+" Status: "+item.getStatus())); // Customize this line based on your ClassItem structure
                    }

                    // Step 3: Close the document
                    document.close();

                    // Optional: Open or share the PDF
                    openPdf(path);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    // Define an ActivityResultLauncher
    ActivityResultLauncher<Intent> viewPdfActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // The PDF viewer activity returned result OK
                    Intent intent = new Intent(this, ModuleDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });

    // Use this launcher to open PDF
    private void openPdf(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(path));
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        viewPdfActivityResultLauncher.launch(intent);
    }

}