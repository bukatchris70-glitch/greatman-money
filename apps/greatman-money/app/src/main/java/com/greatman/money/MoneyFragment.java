package com.greatman.money;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.greatman.money.ui.TransactionAdapter;
import com.greatman.money.viewmodel.TransactionViewModel;
import com.greatman.money.model.Transaction;
import java.util.List;

public class MoneyFragment extends Fragment {
    TransactionViewModel tvm;
    TransactionAdapter adapter;

    public MoneyFragment() { super(); }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_money, container, false);
        RecyclerView rv = v.findViewById(R.id.rvAll);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter(t -> confirmDelete(t));
        rv.setAdapter(adapter);

        tvm = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        tvm.getAllDesc().observe(getViewLifecycleOwner(), this::onTxs);
        return v;
    }

    private void onTxs(List<Transaction> list) {
        adapter.setItems(list);
    }

    private void confirmDelete(Transaction t) {
        new AlertDialog.Builder(getContext()).setTitle("Delete transaction").setMessage("Delete this transaction?")
                .setPositiveButton("Delete", (d,w) -> tvm.delete(t))
                .setNegativeButton("Cancel", null).show();
    }
}
