package com.example.vacationplanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.Transportation;
import com.example.vacationplanner.entities.Vacation;
import com.example.vacationplanner.utilities.ExcursionSaveUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link ExcursionSaveUtil}.
 * Uses Robolectric to handle Android dependencies like Toast and Log.
 */
@RunWith(RobolectricTestRunner.class)
public class ExcursionSaveUnitTest {

    private Context context;

    @Mock
    private Repository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        context = RuntimeEnvironment.getApplication();
    }

    @Test
    public void ValidInputs_validateExcursionToSave() {
        int vacationID = 1;
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(new Vacation(vacationID, "Trip", "Hotel", "08/01/26", "08/10/26", Transportation.Bike));
        when(repository.getAllVacations()).thenReturn(vacations);

        assertTrue("Valid excursion date within vacation range should return true",
                ExcursionSaveUtil.validateExcursionToSave(context, "Excursion", "08/05/26", vacationID, repository));
    }

    @Test
    public void VacationNotSaved_validateExcursionToSave() {
        assertFalse("Unsaved vacation (ID = -1) should return false",
                ExcursionSaveUtil.validateExcursionToSave(context, "Excursion", "08/05/26", -1, repository));
    }

    @Test
    public void EmptyFields_validateExcursionToSave() {
        assertFalse("Empty title should return false", 
                ExcursionSaveUtil.validateExcursionToSave(context, "", "08/05/26", 1, repository));
        assertFalse("Empty date should return false", 
                ExcursionSaveUtil.validateExcursionToSave(context, "Excursion", "", 1, repository));
    }

    @Test
    public void DateOutsideVacationRange_validateExcursionToSave() {
        int vacationID = 1;
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(new Vacation(vacationID, "Trip", "Hotel", "08/01/26", "08/10/26", Transportation.Bike));
        when(repository.getAllVacations()).thenReturn(vacations);

        assertFalse("Date before vacation start should return false", 
                ExcursionSaveUtil.validateExcursionToSave(context, "Excursion", "07/31/26", vacationID, repository));
        assertFalse("Date after vacation end should return false", 
                ExcursionSaveUtil.validateExcursionToSave(context, "Excursion", "08/11/26", vacationID, repository));
    }

    @Test
    public void InvalidDateFormat_validateExcursionToSave() {
        int vacationID = 1;
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(new Vacation(vacationID, "Trip", "Hotel", "08/01/26", "08/10/26", Transportation.Bike));

        when(repository.getAllVacations()).thenReturn(vacations);

        assertFalse("Invalid date format should return false",
                ExcursionSaveUtil.validateExcursionToSave(context, "Excursion", "2026-08-05", vacationID, repository));
    }

    @Test
    public void insert_NewExcursion_saveExcursionToRepository() {
        ExcursionSaveUtil.saveExcursionToRepository(-1, 1, "Excursion", "08/05/26", repository);
        verify(repository).insert(any(Excursion.class));
    }

    @Test
    public void update_ExistingExcursion_saveExcursionToRepository() {
        ExcursionSaveUtil.saveExcursionToRepository(100, 1, "Excursion", "08/05/26", repository);
        verify(repository).update(any(Excursion.class));
    }
}
