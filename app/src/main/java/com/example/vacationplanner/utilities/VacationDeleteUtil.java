package com.example.vacationplanner.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Vacation;

/**
 * Utility class for handling vacation deletion logic.
 */
public class VacationDeleteUtil {
    /**
     * Deletes a vacation if it has no associated excursions.
     * @param context The application context for showing toasts.
     * @param vacation The vacation entity to delete.
     * @param repository The repository for database operations.
     * @return true if deletion was successful or not needed, false if it failed due to existing excursions.
     */
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
