package com.greatman.money.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.greatman.money.model.Goal;
import com.greatman.money.repo.GoalRepository;
import java.util.List;

public class GoalViewModel extends AndroidViewModel {
    private GoalRepository repo;
    private LiveData<List<Goal>> all;

    public GoalViewModel(@NonNull Application application) {
        super(application);
        repo = new GoalRepository(application);
        all = repo.getAll();
    }

    public LiveData<List<Goal>> getAll() { return all; }
    public void insert(Goal g) { repo.insert(g); }
    public void delete(Goal g) { repo.delete(g); }
    public LiveData<Double> sumCurrentByEmergency(boolean emergency) { return repo.sumCurrentByEmergency(emergency); }
}
