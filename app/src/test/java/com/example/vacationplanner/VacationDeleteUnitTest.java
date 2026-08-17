package com.example.vacationplanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.Vacation;
import com.example.vacationplanner.utilities.VacationDeleteUtil;

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
 * Unit tests for {@link VacationDeleteUtil}.
 * Uses Robolectric to handle Android dependencies like Toast and Log.
 */
@RunWith(RobolectricTestRunner.class)
public class VacationDeleteUnitTest {
    private Context context;

    @Mock
    private Repository repository;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        // Robolectric provides a context that handles Toast and Log without crashing
        context = RuntimeEnvironment.getApplication();
    }

    @Test
    public void successful_VacationDelete() {
        Vacation vacation = mock(Vacation.class);

        when(vacation.getVacationID()).thenReturn(0);

        assertTrue("Valid vacation should return true when deleted",
                VacationDeleteUtil.deleteVacation(context, vacation, repository));

        // Verify that delete is called for valid vacation
        verify(repository).delete(vacation);
    }

    @Test
    public void unsuccessful_VacationDelete() {
        List<Excursion> excursions = new ArrayList<>();
        excursions.add(mock(Excursion.class));
        Vacation vacation = mock(Vacation.class);

        when(vacation.getVacationID()).thenReturn(1);
        when(repository.getAssociatedExcursions(1)).thenReturn(excursions);

        assertFalse("Vacation with excursions should return false when deletion is attempted",
                VacationDeleteUtil.deleteVacation(context, vacation, repository));

        // Verify that delete was never called
        verify(repository, never()).delete(vacation);
    }

    @Test
    public void noVacationToDelete() {
        Vacation vacation = mock(Vacation.class);

        when(vacation.getVacationID()).thenReturn(-1);

        assertTrue("Unsaved vacation should return true",
                VacationDeleteUtil.deleteVacation(context, vacation, repository));

        verify(repository, never()).delete(vacation);
    }
}
