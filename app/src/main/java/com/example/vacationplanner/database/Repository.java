package com.example.vacationplanner.database;

import android.app.Application;

import com.example.vacationplanner.dao.ExcursionDAO;
import com.example.vacationplanner.dao.UserDAO;
import com.example.vacationplanner.dao.VacationDAO;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.User;
import com.example.vacationplanner.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExcursionDAO mExcursionDAO;
    private VacationDAO mVacationDAO;
    private UserDAO mUserDAO;

    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;
    private List<User> mAllUsers;

    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabase db = VacationDatabase.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
        mUserDAO = db.userDAO();
    }

    public List<Vacation> getAllVacations(){
        databaseExecutor.execute(()->{
            mAllVacations = mVacationDAO.getAllVacations();
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        return mAllVacations;
    }

    public void insert(Vacation vacation){
        databaseExecutor.execute(()->{
            mVacationDAO.insert(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void update(Vacation vacation){
        databaseExecutor.execute(()->{
            mVacationDAO.update(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Vacation vacation){
        databaseExecutor.execute(()->{
            mVacationDAO.delete(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public List<Excursion> getAllExcursions(){
        databaseExecutor.execute(()->{
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        return mAllExcursions;
    }

    public List<Excursion> getAssociatedExcursions(int vacationID){
        databaseExecutor.execute(()->{
            mAllExcursions = mExcursionDAO.getAssociatedExcursions(vacationID);
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        return mAllExcursions;
    }

    public void insert(Excursion excursion){
        databaseExecutor.execute(()->{
            mExcursionDAO.insert(excursion);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void update(Excursion excursion){
        databaseExecutor.execute(()->{
            mExcursionDAO.update(excursion);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Excursion excursion){
        databaseExecutor.execute(()->{
            mExcursionDAO.delete(excursion);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void insert(User user){
        databaseExecutor.execute(()->{
            mUserDAO.insert(user);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void update(User user){
        databaseExecutor.execute(()->{
            mUserDAO.update(user);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void delete(User user){
        databaseExecutor.execute(()->{
            mUserDAO.delete(user);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        databaseExecutor.execute(()->{
            mAllUsers = mUserDAO.getAllUsers();
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        return mAllUsers;
    }
}
