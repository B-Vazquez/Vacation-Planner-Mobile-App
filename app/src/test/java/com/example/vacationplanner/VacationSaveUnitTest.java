package com.example.vacationplanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Vacation;
import com.example.vacationplanner.utilities.VacationSaveUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

/**
 * Unit tests for {@link VacationSaveUtil}.
 * Uses Robolectric to handle Android dependencies like Toast and Log.
 */
@RunWith(RobolectricTestRunner.class)
public class VacationSaveUnitTest {

    private Context context;

    @Mock
    private Repository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Robolectric provides a context that handles Toast and Log without crashing
        context = RuntimeEnvironment.getApplication();
    }

    @Test
    public void validInputs_vacationToSave() {
        boolean result = VacationSaveUtil.validateVacationToSave(
                context, "Summer Trip", "Hilton", "08/01/26", "08/10/26");
        assertTrue("Valid inputs should return true", result);
    }

    @Test
    public void invalidInput_vacationToSave() {
        assertFalse("Empty title should return false", 
                VacationSaveUtil.validateVacationToSave(context, "", "Hilton", "08/01/26", "08/10/26"));
        assertFalse("Empty hotel should return false", 
                VacationSaveUtil.validateVacationToSave(context, "Trip", "", "08/01/26", "08/10/26"));
        assertFalse("Empty start date should return false", 
                VacationSaveUtil.validateVacationToSave(context, "Trip", "Hilton", "", "08/10/26"));
        assertFalse("Empty end date should return false", 
                VacationSaveUtil.validateVacationToSave(context, "Trip", "Hilton", "08/01/26", ""));
    }

    @Test
    public void invalid_Date_Format_vacationToSave() {
        // Method expects MM/dd/yy
        boolean result = VacationSaveUtil.validateVacationToSave(
                context, "Trip", "Hilton", "2026-08-01", "2026-08-10");
        assertFalse("Invalid date format should return false", result);
    }

    @Test
    public void endDateBeforeStart_vacationToSave() {
        boolean result = VacationSaveUtil.validateVacationToSave(
                context, "Trip", "Hilton", "08/10/26", "08/01/26");
        assertFalse("End date before start date should return false", result);
    }

    @Test
    public void insert_NewVacation_saveVacation() {
        Vacation vacation = mock(Vacation.class);
        when(vacation.getVacationID()).thenReturn(-1);

        VacationSaveUtil.saveVacationToRepository(vacation, repository);

        // Verify that ID is set to 0 (for Room auto-generation) and insert is called
        verify(vacation).setVacationID(0);
        verify(repository).insert(vacation);
    }

    @Test
    public void update_ExistingVacation_saveVacation() {
        Vacation vacation = mock(Vacation.class);
        when(vacation.getVacationID()).thenReturn(1);

        VacationSaveUtil.saveVacationToRepository(vacation, repository);

        // Verify that update is called for an existing ID
        verify(repository).update(vacation);
    }
}
