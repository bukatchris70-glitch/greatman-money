package com.greatman.money.repo;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.greatman.money.db.AppDatabase;
import com.greatman.money.db.GoalDao;
import com.greatman.money.model.Goal;
import java.util.List;
import android.os.AsyncTask;

public class GoalRepository {
    private GoalDao dao;
    private LiveData<List<Goal>> all;

    public GoalRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        dao = db.goalDao();
        all = dao.getAll();
    }

    public LiveData<List<Goal>> getAll() { return all; }

    public void insert(final Goal g) {
        AsyncTask.execute(() -> dao.insert(g));
    }

    public void delete(final Goal g) {
        AsyncTask.execute(() -> dao.delete(g));
    }

    public LiveData<Double> sumCurrentByEmergency(boolean emergency) { return dao.sumCurrentByEmergency(emergency); }
}
