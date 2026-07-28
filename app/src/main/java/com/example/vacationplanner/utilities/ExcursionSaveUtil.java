package com.example.vacationplanner.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.Vacation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ExcursionSaveUtil {
    public static boolean validateExcursionToSave(Context context, String title, String date, int vacationID, Repository repository) throws Exception {
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
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Date excursionDate = dateFormat.parse(date);
        Date vacationStart = dateFormat.parse(startDate);
        Date vacationEnd = dateFormat.parse(endDate);
        if (Objects.requireNonNull(excursionDate).before(vacationStart) || Objects.requireNonNull(excursionDate).after(vacationEnd)){
            Toast.makeText(context, "Excursion date must be between the start and end date of it's vacation.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static void saveExcursionToRepository(int excursionID, int vacationID, String title, String date, Repository repository){
        if(excursionID != -1){
            Excursion excursion = new Excursion(excursionID, title, vacationID, date);
            repository.update(excursion);
            return;
        }
        if(repository.getAllExcursions().isEmpty()) {
            excursionID = 1;
        }
        else{
            excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
        }
        Excursion excursion = new Excursion(excursionID, title, vacationID, date);
        repository.insert(excursion);
    }
}
