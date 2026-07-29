package com.example.vacationplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Excursion;

import java.util.List;

public class ExcursionViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Excursion>> allExcursions;

    public ExcursionViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allExcursions = repository.getAllExcursionsLiveData();
    }

    public LiveData<List<Excursion>> getAllExcursions() {
        return allExcursions;
    }

    public LiveData<List<Excursion>> getAssociatedExcursions(int vacationID) {
        return repository.getAssociatedExcursionsLiveData(vacationID);
    }

    public void insert(Excursion excursion) {
        repository.insert(excursion);
    }

    public void update(Excursion excursion) {
        repository.update(excursion);
    }

    public void delete(Excursion excursion) {
        repository.delete(excursion);
    }
}
