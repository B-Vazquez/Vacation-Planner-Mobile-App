package com.example.vacationplanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlarmManager;
import android.content.Context;

import com.example.vacationplanner.utilities.AlarmUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowAlarmManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Unit tests for {@link AlarmUtil}.
 * Uses Robolectric to handle Android dependencies and verify AlarmManager scheduling.
 */
@RunWith(RobolectricTestRunner.class)
public class AlarmUnitTest {

    private Context context;
    private ShadowAlarmManager shadowAlarmManager;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = shadowOf(alarmManager);
    }

    @Test
    public void setAlarm_ValidDate_SchedulesAlarm() throws ParseException {
        String dateString = "08/20/26";
        String message = "Vacation Alert";

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Date parsedDate = sdf.parse(dateString);
        long expectedTime = Objects.requireNonNull(parsedDate).getTime();

        AlarmUtil.setAlarm(context, dateString, message);

        ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        assertNotNull("Alarm should be scheduled", scheduledAlarm);
        assertEquals("Trigger time should match expected parsed date time", expectedTime, scheduledAlarm.triggerAtTime);
        assertEquals("Alarm type should be RTC_WAKEUP", AlarmManager.RTC_WAKEUP, scheduledAlarm.type);

        // Verify that the intent contains the correct message
        String actualMessage = shadowOf(scheduledAlarm.operation).getSavedIntent().getStringExtra("message");
        assertEquals("Intent message should match", message, actualMessage);
    }

    @Test
    public void setAlarm_InvalidDate_DoesNotScheduleAlarm() {
        String invalidDate = "2026-08-20"; // Incorrect format (yyyy-MM-dd)
        AlarmUtil.setAlarm(context, invalidDate, "Invalid Date Alert");

        ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        assertNull("Alarm should not be scheduled for an invalid date format", scheduledAlarm);
    }

    @Test
    public void setAlarm_NullDate_DoesNotScheduleAlarm() {
        AlarmUtil.setAlarm(context, null, "Null Date Alert");

        ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        assertNull("Alarm should not be scheduled for a null date", scheduledAlarm);
    }
}
