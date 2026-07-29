package com.example.vacationplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Vacation;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Vacation>> allVacations;

    public VacationViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allVacations = repository.getAllVacationsLiveData();
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }

    public void insert(Vacation vacation) {
        repository.insert(vacation);
    }

    public void update(Vacation vacation) {
        repository.update(vacation);
    }

    public void delete(Vacation vacation) {
        repository.delete(vacation);
    }
}
