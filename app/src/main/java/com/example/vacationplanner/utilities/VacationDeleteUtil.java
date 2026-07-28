package com.example.vacationplanner.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Vacation;

public class VacationDeleteUtil {
    public static void deleteVacation(Context context, Vacation vacation, Repository repository){
        if(repository.getAssociatedExcursions(vacation.getVacationID()).isEmpty()){
            Toast.makeText(context, "Unable to delete a vacation with excursions", Toast.LENGTH_LONG).show();
        }
        repository.delete(vacation);
    }
}
