package com.greatman.money.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.greatman.money.R;
import com.greatman.money.model.Goal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.VH> {

    public interface Listener {
        void onEdit(Goal g);
        void onDelete(Goal g);
    }

    private List<Goal> items;
    private Listener listener;
    private NumberFormat currencyFormat;

    public GoalAdapter(Listener l) {
        listener = l;
        try{
            Locale ng = new Locale("en","NG");
            currencyFormat = NumberFormat.getCurrencyInstance(ng);
            currencyFormat.setCurrency(Currency.getInstance("NGN"));
        }catch(Exception e){
            currencyFormat = NumberFormat.getCurrencyInstance();
        }
    }

    public void setItems(List<Goal> data) {
        items = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Goal g = items.get(position);
        holder.title.setText(g.name + (g.emergency ? " (Emergency)" : ""));
        holder.subtitle.setText("Current: " + currencyFormat.format(g.current) + " / Target: " + currencyFormat.format(g.target));

        holder.itemView.setOnClickListener(v -> { if (listener!=null) listener.onEdit(g); });
        holder.itemView.setOnLongClickListener(v -> { if (listener!=null) listener.onDelete(g); return true; });
    }

    @Override
    public int getItemCount() { return items==null?0:items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        VH(View v) {
            super(v);
            title = v.findViewById(android.R.id.text1);
            subtitle = v.findViewById(android.R.id.text2);
        }
    }
}
