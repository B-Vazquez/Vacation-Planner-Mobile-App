package com.example.vacationplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationplanner.entities.Excursion;

import java.util.List;

import androidx.lifecycle.LiveData;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("Select * from EXCURSIONS ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    @Query("Select * from EXCURSIONS ORDER BY excursionID ASC")
    LiveData<List<Excursion>> getAllExcursionsLiveData();

    @Query("Select * from EXCURSIONS WHERE vacationID=:vacation ORDER BY excursionID ASC ")
    List<Excursion> getAssociatedExcursions(int vacation);

    @Query("Select * from EXCURSIONS WHERE vacationID=:vacation ORDER BY excursionID ASC ")
    LiveData<List<Excursion>> getAssociatedExcursionsLiveData(int vacation);
}
