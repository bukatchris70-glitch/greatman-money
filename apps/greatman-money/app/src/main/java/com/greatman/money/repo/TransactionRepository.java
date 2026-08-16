package com.greatman.money.repo;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.greatman.money.db.AppDatabase;
import com.greatman.money.db.TransactionDao;
import com.greatman.money.model.Transaction;
import java.util.List;
import android.os.AsyncTask;

public class TransactionRepository {
    private TransactionDao dao;
    private LiveData<List<Transaction>> allDesc;

    public TransactionRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        dao = db.transactionDao();
        allDesc = dao.getAllDesc();
    }

    public LiveData<List<Transaction>> getAllDesc() { return allDesc; }

    public void insert(final Transaction t) {
        AsyncTask.execute(() -> dao.insert(t));
    }

    public void delete(final Transaction t) {
        AsyncTask.execute(() -> dao.delete(t));
    }

    public LiveData<Double> totalByType(String type) { return dao.totalByType(type); }
}
