package com.example.vacationplanner.utilities;


import android.content.Context;
import android.widget.Toast;

import com.example.vacationplanner.entities.Excursion;

import java.util.List;

/**
 * Utility class for formatting vacation details for sharing.
 */
public class VacationShareUtil {
    /**
     * Formats vacation and its excursions into a shareable string.
     * @param context The application context for showing toasts.
     * @param title Vacation title.
     * @param hotel Hotel name.
     * @param start Start date string.
     * @param end End date string.
     * @param excursionList List of associated excursions.
     * @return A StringBuilder containing formatted trip details, or null if validation fails.
     */
    public static StringBuilder shareVacationDetails(Context context, String title, String hotel, String start, String end, List<Excursion> excursionList){
        if(title.isEmpty() || hotel.isEmpty() || start.isEmpty() || end.isEmpty()){
            Toast.makeText(context, "All fields must be filled to share.", Toast.LENGTH_LONG).show();
            return null;
        }
        StringBuilder shareText = new StringBuilder();
        shareText.append("Title: ").append(title);
        shareText.append("\nHotel: ").append(hotel);
        shareText.append("\nStart Date: ").append(start);
        shareText.append("\nEnd Date: ").append(end);
        if (!excursionList.isEmpty()){
            shareText.append("\n\nExcursions:");
            for(Excursion excursion : excursionList){
                shareText.append("\nTitle: ").append(excursion.getExcursionName());
                shareText.append("\nDate: ").append(excursion.getDate());
            }
        }
        return shareText;
    }
}
