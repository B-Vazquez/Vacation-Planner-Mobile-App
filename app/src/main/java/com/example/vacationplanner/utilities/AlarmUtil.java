package com.example.vacationplanner.utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.vacationplanner.UI.MainActivity;
import com.example.vacationplanner.UI.MyReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for scheduling alerts and notifications.
 */
public class AlarmUtil {
    private static final String TAG = "AlarmUtil";
    private static final String DATE_FORMAT = "MM/dd/yy";

    /**
     * Schedules a broadcast alarm for a given date and message.
     * @param context The application context.
     * @param dateString The date string in MM/dd/yy format.
     * @param message The message to be sent via the broadcast receiver.
     */
    public static void setAlarm(Context context, String dateString, String message) {
        if (dateString == null || dateString.isEmpty()) return;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            Date date = sdf.parse(dateString);
            if (date == null) return;

            long triggerTime = date.getTime();
            Intent intent = new Intent(context, MyReceiver.class);
            intent.putExtra("message", message);

            // Using FLAG_IMMUTABLE as required by newer Android APIs
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 
                    ++MainActivity.numAlert, 
                    intent, 
                    PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Failed to parse date for alert: " + dateString, e);
        }
    }
}
