package com.example.instatt.Setting;

import static com.example.instatt.LogIn.userEmail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.instatt.Class.ClassDataHandler;
import com.example.instatt.Class.Event;
import com.example.instatt.R;
import com.google.firebase.database.DatabaseError;

import java.time.LocalDate;
import java.util.ArrayList;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.ACTION_SET_ALARM".equals(intent.getAction())) {
            fetchClassDataFromFirebase(context);
            //setupSimpleNotification(context);
        }
    }
    private void fetchClassDataFromFirebase(Context context) {
        LocalDate dateTmr = LocalDate.now().plusDays(1);
        ClassDataHandler.fetchClassData(userEmail, dateTmr, new ClassDataHandler.ClassDataListener() {
            @Override
            public void onClassDataFetched(ArrayList<Event> events) {
                updateNotificationWithEvents(context, events);
            }

            @Override
            public void onClassDataFetchError(DatabaseError databaseError) {
                Log.w("HomepageActivity", "fetchClassDataFromFirebase:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateNotificationWithEvents(Context context, ArrayList<Event> events) {
        String notificationContent = formatEventList(events);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "androidknowledge")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Your Classes for Tomorrow")
                .setContentText(notificationContent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat.from(context).notify(123, builder.build());
    }

    private String formatEventList(ArrayList<Event> events) {
        StringBuilder formattedEvents = new StringBuilder();
        for (Event event : events) {
            formattedEvents.append(event.getName()).append(" at ").append(event.getStartTime()).append("\n");
        }
        return formattedEvents.toString();
    }
}
