package com.greatman.money.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.greatman.money.model.Transaction;
import com.greatman.money.repo.TransactionRepository;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {
    private TransactionRepository repo;
    private LiveData<List<Transaction>> allDesc;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        repo = new TransactionRepository(application);
        allDesc = repo.getAllDesc();
    }

    public LiveData<List<Transaction>> getAllDesc() { return allDesc; }
    public void insert(Transaction t) { repo.insert(t); }
    public void delete(Transaction t) { repo.delete(t); }
    public LiveData<Double> totalByType(String type) { return repo.totalByType(type); }
}
