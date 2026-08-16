package com.greatman.money.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.greatman.money.model.Transaction;
import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllDesc();

    @Insert
    long insert(Transaction t);

    @Delete
    void delete(Transaction t);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    LiveData<Double> totalByType(String type);
}
