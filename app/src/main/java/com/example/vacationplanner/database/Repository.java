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
import java.util.ArrayList;
import android.util.Log;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Repository class that abstracts access to multiple data sources.
 * It provides a clean API for data access to the rest of the application.
 */
public class Repository {
    private final ExcursionDAO mExcursionDAO;
    private final VacationDAO mVacationDAO;
    private final UserDAO mUserDAO;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Constructor for the Repository.
     * @param application The application context used to initialize the database.
     */
    public Repository(Application application){
        VacationDatabase db = VacationDatabase.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
        mUserDAO = db.userDAO();
    }

    /**
     * Retrieves all vacations from the database.
     * Note: This blocks the current thread. Use {@link #getAllVacationsLiveData()} for non-blocking UI updates.
     * @return A list of all vacations.
     */
    public List<Vacation> getAllVacations(){
        Future<List<Vacation>> future = databaseExecutor.submit(mVacationDAO::getAllVacations);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Repository", "Error fetching all vacations", e);
            return new ArrayList<>();
        }
    }

    /**
     * Returns an observable LiveData list of all vacations.
     * @return LiveData containing the list of vacations.
     */
    public LiveData<List<Vacation>> getAllVacationsLiveData() {
        return mVacationDAO.getAllVacationsLiveData();
    }

    /**
     * Inserts a vacation into the database asynchronously.
     * @param vacation The vacation to insert.
     */
    public void insert(Vacation vacation){
        databaseExecutor.execute(() -> mVacationDAO.insert(vacation));
    }

    /**
     * Updates an existing vacation in the database asynchronously.
     * @param vacation The vacation to update.
     */
    public void update(Vacation vacation){
        databaseExecutor.execute(() -> mVacationDAO.update(vacation));
    }

    /**
     * Deletes a vacation from the database asynchronously.
     * @param vacation The vacation to delete.
     */
    public void delete(Vacation vacation){
        databaseExecutor.execute(() -> mVacationDAO.delete(vacation));
    }

    /**
     * Retrieves all excursions from the database.
     * Note: This blocks the current thread.
     * @return A list of all excursions.
     */
    public List<Excursion> getAllExcursions(){
        Future<List<Excursion>> future = databaseExecutor.submit(mExcursionDAO::getAllExcursions);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Repository", "Error fetching all excursions", e);
            return new ArrayList<>();
        }
    }

    /**
     * Returns an observable LiveData list of all excursions.
     * @return LiveData containing the list of excursions.
     */
    public LiveData<List<Excursion>> getAllExcursionsLiveData() {
        return mExcursionDAO.getAllExcursionsLiveData();
    }

    /**
     * Retrieves excursions associated with a specific vacation.
     * Note: This blocks the current thread.
     * @param vacationID The ID of the vacation.
     * @return A list of associated excursions.
     */
    public List<Excursion> getAssociatedExcursions(int vacationID){
        Future<List<Excursion>> future = databaseExecutor.submit(() -> mExcursionDAO.getAssociatedExcursions(vacationID));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Repository", "Error fetching associated excursions", e);
            return new ArrayList<>();
        }
    }

    /**
     * Returns an observable LiveData list of excursions associated with a specific vacation.
     * @param vacationID The ID of the vacation.
     * @return LiveData containing the list of associated excursions.
     */
    public LiveData<List<Excursion>> getAssociatedExcursionsLiveData(int vacationID) {
        return mExcursionDAO.getAssociatedExcursionsLiveData(vacationID);
    }

    /**
     * Inserts an excursion into the database asynchronously.
     * @param excursion The excursion to insert.
     */
    public void insert(Excursion excursion){
        databaseExecutor.execute(() -> mExcursionDAO.insert(excursion));
    }

    /**
     * Updates an existing excursion in the database asynchronously.
     * @param excursion The excursion to update.
     */
    public void update(Excursion excursion){
        databaseExecutor.execute(() -> mExcursionDAO.update(excursion));
    }

    /**
     * Deletes an excursion from the database asynchronously.
     * @param excursion The excursion to delete.
     */
    public void delete(Excursion excursion){
        databaseExecutor.execute(() -> mExcursionDAO.delete(excursion));
    }

    /**
     * Inserts a user into the database asynchronously.
     * @param user The user to insert.
     */
    public void insert(User user){
        databaseExecutor.execute(() -> mUserDAO.insert(user));
    }

    /**
     * Updates an existing user in the database asynchronously.
     * @param user The user to update.
     */
    public void update(User user){
        databaseExecutor.execute(() -> mUserDAO.update(user));
    }

    /**
     * Deletes a user from the database asynchronously.
     * @param user The user to delete.
     */
    public void delete(User user){
        databaseExecutor.execute(() -> mUserDAO.delete(user));
    }

    /**
     * Retrieves all users from the database.
     * Note: This blocks the current thread.
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        Future<List<User>> future = databaseExecutor.submit(mUserDAO::getAllUsers);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Repository", "Error fetching all users", e);
            return new ArrayList<>();
        }
    }
}

