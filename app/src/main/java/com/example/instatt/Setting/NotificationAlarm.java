package com.example.instatt.Setting;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;

import com.example.instatt.R;
import com.example.instatt.databinding.ActivityNotificationAlarmBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.util.Calendar;
public class NotificationAlarm extends AppCompatActivity {

    private ActivityNotificationAlarmBinding binding;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    ImageButton back;
    String [] permission = new String[]{
            Manifest.permission.POST_NOTIFICATIONS
    };
    public boolean permission_Post_Notification = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNotificationAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        back = findViewById(R.id.back_button); // Assuming you have a back button with this ID
        back.setOnClickListener(v -> {
            Intent settingIntent = new Intent(NotificationAlarm.this, Setting.class);
            startActivity(settingIntent);
            finish(); // Finish the current activity
        });

        createNotificationChannel();
        binding.selectTime.setOnClickListener(view -> {
            timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Alarm Time")
                    .build();
            timePicker.show(getSupportFragmentManager(), "androidknowledge");
            timePicker.addOnPositiveButtonClickListener(view1 -> {
                if (timePicker.getHour() > 12) {
                    binding.selectTime.setText(
                            String.format("%02d", (timePicker.getHour() - 12)) + ":" + String.format("%02d", timePicker.getMinute()) + "PM"
                    );
                } else {
                    binding.selectTime.setText(timePicker.getHour() + ":" + timePicker.getMinute() + "AM");
                }
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            });
        });
        binding.setAlarm.setOnClickListener(view -> {
            requestPermissionNotification();
            if (permission_Post_Notification) {
                Toast.makeText(NotificationAlarm.this, "Notification Permission Granted..", Toast.LENGTH_SHORT).show();
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(NotificationAlarm.this, NotificationReceiver.class);
                intent.setAction("com.example.ACTION_SET_ALARM"); // Custom action for the alarm
                alarmIntent = PendingIntent.getBroadcast(NotificationAlarm.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);
                Toast.makeText(NotificationAlarm.this, "Alarm Set", Toast.LENGTH_SHORT).show();
            }
        });
        binding.cancelAlarm.setOnClickListener(view -> {
            Intent intent = new Intent(NotificationAlarm.this, NotificationReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(NotificationAlarm.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            if (alarmManager == null) {
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            }
            alarmManager.cancel(alarmIntent);
            Toast.makeText(NotificationAlarm.this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "akchannel";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("androidknowledge", name, imp);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void requestPermissionNotification(){
        if (ContextCompat.checkSelfPermission(NotificationAlarm.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            permission_Post_Notification = true;
        }else {
            requestPermissionLauncherNotificaiton.launch(permission[0]);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncherNotificaiton =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if(isGranted){
                    permission_Post_Notification = true;
                }else{
                    permission_Post_Notification=false;
                    showPermissionDialog();
                }
            });
    public void showPermissionDialog() {
        new AlertDialog.Builder(NotificationAlarm.this)
                .setTitle("Alert for Permission")
                .setMessage("The app needs permission to send notifications. Please enable it in the app settings.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

}