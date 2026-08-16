package com.greatman.money.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.greatman.money.model.Goal;
import java.util.List;

@Dao
public interface GoalDao {
    @Query("SELECT * FROM goals")
    LiveData<List<Goal>> getAll();

    @Insert
    long insert(Goal g);

    @Delete
    void delete(Goal g);

    @Query("SELECT SUM(current) FROM goals WHERE emergency = :emergency")
    LiveData<Double> sumCurrentByEmergency(boolean emergency);
}
