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
import com.greatman.money.ui.GoalAdapter;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Currency;
import android.content.Intent;
import android.net.Uri;
import java.util.ArrayList;

public class GoalsFragment extends Fragment {
    GoalViewModel gvm;
    RecyclerView rv;
    GoalAdapter adapter;

    public GoalsFragment() { super(); }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goals, container, false);
        Button btn = v.findViewById(R.id.btnAddGoal);
        rv = v.findViewById(R.id.rvGoals);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GoalAdapter(new GoalAdapter.Listener() {
            @Override public void onEdit(Goal g) { showEditGoal(g); }
            @Override public void onDelete(Goal g) { confirmDelete(g); }
        });
        rv.setAdapter(adapter);

        gvm = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);
        gvm.getAll().observe(getViewLifecycleOwner(), this::onGoals);

        btn.setOnClickListener(x -> showAddGoal());
        // optional: add an export button in the fragment's parent activity via menu later
        return v;
    }

    private void onGoals(List<Goal> list) {
        adapter.setItems(list);
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

    private void showEditGoal(Goal g) {
        LinearLayout f = new LinearLayout(getContext());
        f.setOrientation(LinearLayout.VERTICAL);
        EditText name = new EditText(getContext()); name.setText(g.name);
        EditText target = new EditText(getContext()); target.setText(String.valueOf(g.target));
        target.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText current = new EditText(getContext()); current.setText(String.valueOf(g.current));
        current.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        CheckBox em = new CheckBox(getContext()); em.setText("Emergency Fund"); em.setChecked(g.emergency);
        f.addView(name); f.addView(target); f.addView(current); f.addView(em);

        new AlertDialog.Builder(getContext()).setTitle("Edit Goal").setView(f)
                .setPositiveButton("Save", (d,w) -> {
                    try {
                        String t = target.getText().toString().trim();
                        if (t.isEmpty()) { Toast.makeText(getContext(), "Enter target amount", Toast.LENGTH_SHORT).show(); return; }
                        double tval = Double.parseDouble(t);
                        double cval = current.getText().toString().trim().isEmpty() ? 0 : Double.parseDouble(current.getText().toString().trim());
                        String nm = name.getText().toString().trim();
                        if (nm.isEmpty()) nm = "Savings Goal";
                        // Update by deleting and reinserting with same id not trivial with Room simple DAO; for now delete and insert replacement
                        Goal ng = new Goal(nm, tval, cval, em.isChecked());
                        // we can't set ID directly (Room manages it), so we delete old and insert new
                        gvm.delete(g);
                        gvm.insert(ng);
                    } catch (NumberFormatException nfe) {
                        Toast.makeText(getContext(), "Enter numeric amounts", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    private void confirmDelete(Goal g) {
        new AlertDialog.Builder(getContext()).setTitle("Delete goal").setMessage("Delete this goal?")
                .setPositiveButton("Delete", (d,w) -> gvm.delete(g))
                .setNegativeButton("Cancel", null).show();
    }

}
