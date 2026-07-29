package com.example.vacationplanner.database;

import android.app.Application;

import com.example.vacationplanner.dao.ExcursionDAO;
import com.example.vacationplanner.dao.UserDAO;
import com.example.vacationplanner.dao.VacationDAO;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.User;
import com.example.vacationplanner.entities.Vacation;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Repository {
    private ExcursionDAO mExcursionDAO;
    private VacationDAO mVacationDAO;
    private UserDAO mUserDAO;

    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabase db = VacationDatabase.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
        mUserDAO = db.userDAO();
    }

    public List<Vacation> getAllVacations(){
        Future<List<Vacation>> future = databaseExecutor.submit(() -> mVacationDAO.getAllVacations());
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Vacation>> getAllVacationsLiveData() {
        return mVacationDAO.getAllVacationsLiveData();
    }

    public void insert(Vacation vacation){
        Future<?> future = databaseExecutor.submit(() -> mVacationDAO.insert(vacation));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Vacation vacation){
        Future<?> future = databaseExecutor.submit(() -> mVacationDAO.update(vacation));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Vacation vacation){
        Future<?> future = databaseExecutor.submit(() -> mVacationDAO.delete(vacation));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Excursion> getAllExcursions(){
        Future<List<Excursion>> future = databaseExecutor.submit(() -> mExcursionDAO.getAllExcursions());
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Excursion>> getAllExcursionsLiveData() {
        return mExcursionDAO.getAllExcursionsLiveData();
    }

    public List<Excursion> getAssociatedExcursions(int vacationID){
        Future<List<Excursion>> future = databaseExecutor.submit(() -> mExcursionDAO.getAssociatedExcursions(vacationID));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Excursion>> getAssociatedExcursionsLiveData(int vacationID) {
        return mExcursionDAO.getAssociatedExcursionsLiveData(vacationID);
    }

    public void insert(Excursion excursion){
        Future<?> future = databaseExecutor.submit(() -> mExcursionDAO.insert(excursion));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Excursion excursion){
        Future<?> future = databaseExecutor.submit(() -> mExcursionDAO.update(excursion));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Excursion excursion){
        Future<?> future = databaseExecutor.submit(() -> mExcursionDAO.delete(excursion));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(User user){
        Future<?> future = databaseExecutor.submit(() -> mUserDAO.insert(user));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(User user){
        Future<?> future = databaseExecutor.submit(() -> mUserDAO.update(user));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(User user){
        Future<?> future = databaseExecutor.submit(() -> mUserDAO.delete(user));
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        Future<List<User>> future = databaseExecutor.submit(() -> mUserDAO.getAllUsers());
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
