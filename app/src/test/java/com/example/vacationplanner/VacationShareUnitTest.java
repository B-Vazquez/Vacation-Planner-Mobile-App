package com.example.vacationplanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.utilities.VacationShareUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link VacationShareUtil}.
 * Uses Robolectric to handle Android dependencies like Toast and Log.
 */
@RunWith(RobolectricTestRunner.class)
public class VacationShareUnitTest {
    private Context context;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        // Robolectric provides a context that handles Toast and Log without crashing
        context = RuntimeEnvironment.getApplication();
    }

    @Test
    public void invalid_VacationShare() {
        List<Excursion> excursions = new ArrayList<>();

        assertNull("Empty title should return null",
                VacationShareUtil.shareVacationDetails(context, "", "Marriott", "12/01/26", "12/03/26", excursions));
        assertNull("Empty hotel should return null",
                VacationShareUtil.shareVacationDetails(context, "Florida", "", "12/01/26", "12/03/26", excursions));
        assertNull("Empty start date should return null",
                VacationShareUtil.shareVacationDetails(context, "Florida", "Marriott", "", "12/03/26", excursions));
        assertNull("Empty end date should return null",
                VacationShareUtil.shareVacationDetails(context, "Florida", "Marriott", "12/01/26", "", excursions));
    }

    @Test
    public void valid_VacationShare() {
        List<Excursion> excursions = new ArrayList<>();
        StringBuilder result = VacationShareUtil.shareVacationDetails(context, "Florida", "Marriott", "12/01/26", "12/03/26", excursions);

        assertNotNull("Valid input should not return null", result);
        assertFalse("Valid input should return non-empty StringBuilder", result.toString().isEmpty());
    }
}
