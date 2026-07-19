package com.example.vacationplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vacationplanner.dao.ExcursionDAO;
import com.example.vacationplanner.dao.UserDAO;
import com.example.vacationplanner.dao.VacationDAO;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.User;
import com.example.vacationplanner.entities.Vacation;

@Database(entities = {Excursion.class, Vacation.class, User.class}, version = 5, exportSchema = false)
public abstract class VacationDatabase extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    public abstract UserDAO userDAO();
    private static volatile VacationDatabase INSTANCE;

    static VacationDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (VacationDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),VacationDatabase.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
