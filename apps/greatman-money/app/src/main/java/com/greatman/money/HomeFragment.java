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
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Currency;

public class HomeFragment extends Fragment {

    TransactionViewModel tvm;
    TransactionAdapter adapter;
    TextView tvBalance;
    NumberFormat currencyFormat;

    public HomeFragment() { super(); }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        tvBalance = v.findViewById(R.id.tvBalance);
        Button bInc = v.findViewById(R.id.btnAddIncome);
        Button bExp = v.findViewById(R.id.btnAddExpense);
        RecyclerView rv = v.findViewById(R.id.rvRecent);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter(t -> confirmDelete(t));
        rv.setAdapter(adapter);

        try{
            Locale ng = new Locale("en","NG");
            currencyFormat = NumberFormat.getCurrencyInstance(ng);
            currencyFormat.setCurrency(Currency.getInstance("NGN"));
        }catch(Exception e){
            currencyFormat = NumberFormat.getCurrencyInstance();
        }

        tvm = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        tvm.getAllDesc().observe(getViewLifecycleOwner(), this::onTxs);

        bInc.setOnClickListener(x -> showTransactionDialog(true));
        bExp.setOnClickListener(x -> showTransactionDialog(false));
        return v;
    }

    private void onTxs(List<com.greatman.money.model.Transaction> list) {
        adapter.setItems(list);
        double income = 0, expense = 0;
        if (list != null) {
            for (com.greatman.money.model.Transaction t : list) {
                if ("income".equals(t.type)) income += t.amount;
                else if ("expense".equals(t.type)) expense += t.amount;
            }
        }
        tvBalance.setText(currencyFormat.format(income - expense));
    }

    private void showTransactionDialog(boolean income) {
        LinearLayout f = new LinearLayout(getContext());
        f.setOrientation(LinearLayout.VERTICAL);
        final EditText amount = new EditText(getContext());
        amount.setHint("Amount (₦)");
        amount.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        final EditText category = new EditText(getContext());
        category.setHint("Category");
        f.addView(amount);
        f.addView(category);

        new AlertDialog.Builder(getContext())
                .setTitle(income ? "Add Income" : "Add Expense")
                .setView(f)
                .setPositiveButton("Save", (d, w) -> {
                    try {
                        String s = amount.getText().toString().trim();
                        if (s.isEmpty()) { Toast.makeText(getContext(), "Enter amount", Toast.LENGTH_SHORT).show(); return; }
                        double a = Double.parseDouble(s);
                        if (a <= 0) { Toast.makeText(getContext(), "Enter valid amount", Toast.LENGTH_SHORT).show(); return; }
                        String cat = category.getText().toString().trim();
                        if (cat.isEmpty()) cat = "General";
                        Transaction t = new Transaction(income ? "income" : "expense", a, cat, System.currentTimeMillis());
                        tvm.insert(t);
                    } catch (NumberFormatException nfe) {
                        Toast.makeText(getContext(), "Enter numeric amount", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    private void confirmDelete(Transaction t) {
        new AlertDialog.Builder(getContext()).setTitle("Delete transaction").setMessage("Delete this transaction?")
                .setPositiveButton("Delete", (d,w) -> tvm.delete(t))
                .setNegativeButton("Cancel", null).show();
    }
}
