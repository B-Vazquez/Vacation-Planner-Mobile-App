package com.example.vacationplanner.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Vacation;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * Utility class for validating and saving Vacation entities.
 */
public class VacationSaveUtil {
    private static final String TAG = "VacationSaveUtil";
    private static final String DATE_FORMAT = "MM/dd/yy";

    /**
     * Validates vacation inputs before saving.
     * @param context The application context for showing error toasts.
     * @param title Vacation title.
     * @param hotel Hotel name.
     * @param start Start date string.
     * @param end End date string.
     * @return true if valid, false otherwise.
     */
    public static boolean validateVacationToSave(Context context, String title, String hotel, String start, String end) {
        if (!validateInputsNotEmpty(title, hotel, start, end)){
            Toast.makeText(context, "All fields must be filled.", Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            if (!validateDates(start, end)){
                Toast.makeText(context, "End date must come after start date.", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            Log.e(TAG, "Date parsing failed during validation", e);
            Toast.makeText(context, "Invalid date format. Use MM/dd/yy.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Persists the vacation to the repository.
     * Handles both new insertions (ID = -1) and updates.
     * @param vacation The vacation entity to save.
     * @param repository The repository for database operations.
     */
    public static void saveVacationToRepository(Vacation vacation, Repository repository) {
        if (vacation.getVacationID() == -1) {
            // Room auto-generates ID when set to 0
            vacation.setVacationID(0);
            repository.insert(vacation);
        } else {
            repository.update(vacation);
        }
    }

    private static boolean validateInputsNotEmpty(String title, String hotel, String start, String end) {
        return !title.isEmpty() && !hotel.isEmpty() && !start.isEmpty() && !end.isEmpty();
    }

    private static boolean validateDates(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);
        
        if (startDate == null || endDate == null) return false;
        return startDate.before(endDate);
    }
}

