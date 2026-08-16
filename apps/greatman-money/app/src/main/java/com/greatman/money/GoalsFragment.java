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
import com.greatman.money.viewmodel.GoalViewModel;
import com.greatman.money.model.Goal;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Currency;

public class GoalsFragment extends Fragment {
    GoalViewModel gvm;
    RecyclerView rv;
    TextView empty;

    public GoalsFragment() { super(); }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goals, container, false);
        Button btn = v.findViewById(R.id.btnAddGoal);
        rv = v.findViewById(R.id.rvGoals);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        empty = new TextView(getContext());
        rv.setAdapter(new androidx.recyclerview.widget.RecyclerView.Adapter() {
            // simple inline adapter: we'll use a simple TextView per goal for brevity
            @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p, int viewType) {
                TextView tv = new TextView(p.getContext());
                tv.setPadding(12,12,12,12);
                return new RecyclerView.ViewHolder(tv){};
            }
            @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            }
            @Override public int getItemCount() { return 0; }
        });

        try{
            Locale ng = new Locale("en","NG");
            NumberFormat.getCurrencyInstance(ng).setCurrency(Currency.getInstance("NGN"));
        }catch(Exception ignored){}

        gvm = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);
        gvm.getAll().observe(getViewLifecycleOwner(), this::onGoals);

        btn.setOnClickListener(x -> showAddGoal());
        return v;
    }

    private void onGoals(List<Goal> list) {
        // simple rendering: replace adapter with a basic list adapter
        if (list == null || list.isEmpty()) {
            // show empty
            TextView tv = new TextView(getContext());
            tv.setText("No goals yet");
            ((ViewGroup) rv.getParent()).removeView(rv);
            ((ViewGroup) getView()).addView(tv);
            return;
        }
        // replace adapter with a simple adapter that displays goal name and amounts
        rv.setAdapter(new androidx.recyclerview.widget.RecyclerView.Adapter() {
            @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p, int viewType) {
                TextView tv = new TextView(p.getContext());
                tv.setPadding(12,12,12,12);
                return new RecyclerView.ViewHolder(tv){};
            }
            @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Goal g = list.get(position);
                ((TextView)holder.itemView).setText(g.name + "\n" + "Current: " + g.current + " / Target: " + g.target + (g.emergency ? " (Emergency)" : ""));
            }
            @Override public int getItemCount() { return list.size(); }
        });
    }

    private void showAddGoal() {
        LinearLayout f = new LinearLayout(getContext());
        f.setOrientation(LinearLayout.VERTICAL);
        EditText name = new EditText(getContext()); name.setHint("Goal name");
        EditText target = new EditText(getContext()); target.setHint("Target amount");
        target.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText current = new EditText(getContext()); current.setHint("Current amount");
        current.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        CheckBox em = new CheckBox(getContext()); em.setText("Emergency Fund");
        f.addView(name); f.addView(target); f.addView(current); f.addView(em);

        new AlertDialog.Builder(getContext()).setTitle("Add Goal").setView(f)
                .setPositiveButton("Save", (d,w) -> {
                    try {
                        String t = target.getText().toString().trim();
                        if (t.isEmpty()) { Toast.makeText(getContext(), "Enter target amount", Toast.LENGTH_SHORT).show(); return; }
                        double tval = Double.parseDouble(t);
                        double cval = current.getText().toString().trim().isEmpty() ? 0 : Double.parseDouble(current.getText().toString().trim());
                        String nm = name.getText().toString().trim();
                        if (nm.isEmpty()) nm = "Savings Goal";
                        Goal g = new Goal(nm, tval, cval, em.isChecked());
                        gvm.insert(g);
                    } catch (NumberFormatException nfe) {
                        Toast.makeText(getContext(), "Enter numeric amounts", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", null).show();
    }
}
