package com.example.vacationplanner.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Vacation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class VacationSaveUtil {
    public static boolean validateVacationToSave(Context context, String title, String hotel, String start, String end) throws Exception {
        if (!validateInputsNotEmpty(title, hotel, start, end)){
            Toast.makeText(context, "All fields must be filled.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validateDates(start, end)){
            Toast.makeText(context, "End date must come after start date", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static void saveVacationToRepository(Vacation vacation, Repository repository) {
        int vacationID = vacation.getVacationID();
        if (vacationID == -1) {
            if (repository.getAllVacations().isEmpty()) {
                vacation.setVacationID(1);
            }
            else {
                vacation.setVacationID(repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1);
            }
            repository.insert(vacation);
        } else {
            repository.update(vacation);
        }
    }

    private static boolean validateInputsNotEmpty(String title, String hotel, String start, String end) {
        return !title.isEmpty() && !hotel.isEmpty() && !start.isEmpty() && !end.isEmpty();
    }

    private static boolean validateDates(String start, String end) throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);
        return Objects.requireNonNull(startDate).before(endDate);
    }
}
