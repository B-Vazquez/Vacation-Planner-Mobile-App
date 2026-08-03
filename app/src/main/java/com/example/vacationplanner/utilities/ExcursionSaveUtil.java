package com.example.vacationplanner.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.Vacation;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * Utility class for validating and saving Excursion entities.
 */
public class ExcursionSaveUtil {
    private static final String TAG = "ExcursionSaveUtil";
    private static final String DATE_FORMAT = "MM/dd/yy";

    /**
     * Validates excursion inputs before saving.
     * @param context The application context for showing error toasts.
     * @param title Excursion title.
     * @param date Excursion date string.
     * @param vacationID The associated vacation ID.
     * @param repository The repository to fetch vacation dates for validation.
     * @return true if valid, false otherwise.
     */
    public static boolean validateExcursionToSave(Context context, String title, String date, int vacationID, Repository repository) {
        if(vacationID == -1){
            Toast.makeText(context, "Vacation must be saved before adding excursions.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(title.isEmpty() || date.isEmpty()){
            Toast.makeText(context, "All fields must be filled to save excursion.", Toast.LENGTH_LONG).show();
            return false;
        }

        String startDate = "", endDate = "";
        for (Vacation vacation : repository.getAllVacations()) {
            if (vacation.getVacationID() == vacationID) {
                startDate = vacation.getStart_date();
                endDate = vacation.getEnd_date();
                break;
            }
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            Date excursionDate = sdf.parse(date);
            Date vacationStart = sdf.parse(startDate);
            Date vacationEnd = sdf.parse(endDate);

            if (excursionDate == null || vacationStart == null || vacationEnd == null) {
                return false;
            }

            if (excursionDate.before(vacationStart) || excursionDate.after(vacationEnd)){
                Toast.makeText(context, "Excursion date must be between the start and end date of its vacation.", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing dates for excursion validation", e);
            Toast.makeText(context, "Invalid date format. Use MM/dd/yy.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Persists the excursion to the repository.
     * @param excursionID Current ID (-1 if new).
     * @param vacationID The associated vacation ID.
     * @param title Excursion title.
     * @param date Excursion date.
     * @param repository The repository for database operations.
     */
    public static void saveExcursionToRepository(int excursionID, int vacationID, String title, String date, Repository repository){
        if(excursionID != -1){
            Excursion excursion = new Excursion(excursionID, title, vacationID, date);
            repository.update(excursion);
        } else {
            // Room auto-generates ID when set to 0
            Excursion excursion = new Excursion(0, title, vacationID, date);
            repository.insert(excursion);
        }
    }
}

