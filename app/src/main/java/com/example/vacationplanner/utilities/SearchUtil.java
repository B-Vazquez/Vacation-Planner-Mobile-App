package com.example.vacationplanner.utilities;

import com.example.vacationplanner.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

public class SearchUtil {

    public static List<Vacation> searchVacations(String searchChoice, String textToSearchFor, List<Vacation> allVacations){
        List<Vacation> searchedVacationList = new ArrayList<>();
        switch(searchChoice){
            case("Title"):
                searchedVacationList = searchByTitle(textToSearchFor, allVacations);
                break;
            case("Hotel"):
                searchedVacationList = searchByHotel(textToSearchFor, allVacations);
                break;
            case("Transportation"):
                searchedVacationList = searchByTransportation(textToSearchFor, allVacations);
                break;
        }
        return searchedVacationList;
    }

    private static List<Vacation> searchByTitle(String titleToSearchFor, List<Vacation> allVacations){
        List<Vacation> searchedVacations = new ArrayList<>();
        for(Vacation i : allVacations){
            if(i.getTitle().toLowerCase().contains(titleToSearchFor)){
                searchedVacations.add(i);
            }
        }
        return searchedVacations;
    }

    private static List<Vacation> searchByHotel(String hotelToSearchFor, List<Vacation> allVacations){
        List<Vacation> searchedVacations = new ArrayList<>();
        for(Vacation i : allVacations){
            if(i.getHotel().toLowerCase().contains(hotelToSearchFor)){
                searchedVacations.add(i);
            }
        }
        return searchedVacations;
    }

    private static List<Vacation> searchByTransportation(String transportationToSearchFor, List<Vacation> allVacations){
        List<Vacation> searchedVacations = new ArrayList<>();
        for(Vacation i : allVacations){
            if(i.getTransport().toString().toLowerCase().contains(transportationToSearchFor)){
                searchedVacations.add(i);
            }
        }
        return searchedVacations;
    }
}
