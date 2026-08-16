package com.greatman.money;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.*;
import android.widget.TextView;
import com.greatman.money.viewmodel.TransactionViewModel;
import com.greatman.money.viewmodel.GoalViewModel;
import java.util.List;
import com.greatman.money.model.Transaction;
import com.greatman.money.model.Goal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Currency;

public class ReportsFragment extends Fragment {
    TransactionViewModel tvm;
    GoalViewModel gvm;
    TextView tvIncome, tvExpense, tvBalance, tvSavings, tvEmergency;
    NumberFormat currencyFormat;

    public ReportsFragment() { super(); }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reports, container, false);
        tvIncome = v.findViewById(R.id.tvTotalIncome);
        tvExpense = v.findViewById(R.id.tvTotalExpense);
        tvBalance = v.findViewById(R.id.tvBalance);
        tvSavings = v.findViewById(R.id.tvSavings);
        tvEmergency = v.findViewById(R.id.tvEmergency);

        try{
            Locale ng = new Locale("en","NG");
            currencyFormat = NumberFormat.getCurrencyInstance(ng);
            currencyFormat.setCurrency(Currency.getInstance("NGN"));
        }catch(Exception e){ currencyFormat = NumberFormat.getCurrencyInstance(); }

        tvm = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        gvm = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);

        tvm.getAllDesc().observe(getViewLifecycleOwner(), this::onTxs);
        gvm.sumCurrentByEmergency(false).observe(getViewLifecycleOwner(), v2 -> {
            double s = v2 == null ? 0 : v2;
            tvSavings.setText("Savings: " + currencyFormat.format(s));
        });
        gvm.sumCurrentByEmergency(true).observe(getViewLifecycleOwner(), v3 -> {
            double e = v3 == null ? 0 : v3;
            tvEmergency.setText("Emergency fund: " + currencyFormat.format(e));
        });

        return v;
    }

    private void onTxs(List<Transaction> list) {
        double income = 0, expense = 0;
        if (list != null) {
            for (Transaction t : list) {
                if ("income".equals(t.type)) income += t.amount;
                else if ("expense".equals(t.type)) expense += t.amount;
            }
        }
        tvIncome.setText("Total income: " + currencyFormat.format(income));
        tvExpense.setText("Total expenses: " + currencyFormat.format(expense));
        tvBalance.setText("Available balance: " + currencyFormat.format(income - expense));
    }
}
