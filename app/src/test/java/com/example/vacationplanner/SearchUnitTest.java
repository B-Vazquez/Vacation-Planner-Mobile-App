package com.example.vacationplanner;

import com.example.vacationplanner.entities.Transportation;
import com.example.vacationplanner.entities.Vacation;
import com.example.vacationplanner.utilities.SearchUtil;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link SearchUtil}
 */
public class SearchUnitTest {
    private List<Vacation> vacationList;

    @Before
    public void setUp() {
        vacationList = new ArrayList<>();
        vacationList.add(new Vacation(0, "Florida", "Marriott", "12/01/26", "12/02/26", Transportation.Bike));
        vacationList.add(new Vacation(0, "Texas", "Green Inn", "12/05/26", "12/08/26", Transportation.Walking));
        vacationList.add(new Vacation(0, "California", "Air BnB", "12/09/26", "12/10/26", Transportation.Car));
    }

    // Tests the search functionality when given a valid input to search for
    @Test
    public void valid_Search() {
        assertEquals("Valid title should return return list of size 1",1,
                SearchUtil.searchVacations("Title", "Florida", vacationList).size());
        assertEquals("Valid hotel should return return list of size 1",1,
                SearchUtil.searchVacations("Hotel", "Green Inn", vacationList).size());
        assertEquals("Valid transportation should return return list of size 1",1,
                SearchUtil.searchVacations("Transportation", "Walking", vacationList).size());
    }

    // Tests the search functionality when given an invalid input to search for
    @Test
    public void invalid_Search() {
        assertEquals("Invalid title should return list of size 0", 0,
                SearchUtil.searchVacations("Title", "Invalid", vacationList).size());
        assertEquals("Invalid hotel should return list of size 0", 0,
                SearchUtil.searchVacations("Hotel", "Invalid", vacationList).size());
        assertEquals("Invalid transportation should return list of size 0", 0,
                SearchUtil.searchVacations("Transportation", "Invalid", vacationList).size());
    }
}
