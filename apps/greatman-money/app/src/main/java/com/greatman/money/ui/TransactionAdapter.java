package com.greatman.money.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.greatman.money.R;
import com.greatman.money.model.Transaction;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;
import java.util.Currency;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.VH> {

    public interface Listener { void onLongClick(Transaction t); }

    private List<Transaction> items;
    private Listener listener;
    private NumberFormat currencyFormat;

    public TransactionAdapter(Listener l) {
        listener = l;
        try{
            Locale ng = new Locale("en","NG");
            currencyFormat = NumberFormat.getCurrencyInstance(ng);
            currencyFormat.setCurrency(Currency.getInstance("NGN"));
        }catch(Exception e){
            currencyFormat = NumberFormat.getCurrencyInstance();
        }
    }

    public void setItems(List<Transaction> data) {
        items = data;
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Transaction t = items.get(position);
        holder.tvCategoryDate.setText(t.category + " • " + new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(t.date==0?new java.util.Date():new java.util.Date(t.date)));
        String amt = (t.type.equals("income") ? "+" : "−") + currencyFormat.format(t.amount);
        holder.tvAmount.setText(amt);
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onLongClick(t);
            return true;
        });
    }

    @Override
    public int getItemCount() { return items==null?0:items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvCategoryDate, tvAmount;
        VH(View v) {
            super(v);
            tvCategoryDate = v.findViewById(R.id.tvCategoryDate);
            tvAmount = v.findViewById(R.id.tvAmount);
        }
    }
}
