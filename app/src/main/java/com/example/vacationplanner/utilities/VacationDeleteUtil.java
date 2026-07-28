package com.example.vacationplanner.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Vacation;

public class VacationDeleteUtil {
    public static boolean deleteVacation(Context context, Vacation vacation, Repository repository){
        if (vacation.getVacationID() == -1){
            return true;
        }
        if(!repository.getAssociatedExcursions(vacation.getVacationID()).isEmpty()){
            Toast.makeText(context, "Unable to delete a vacation with excursions", Toast.LENGTH_LONG).show();
            return false;
        }
        repository.delete(vacation);
        return true;
    }
}
