package com.greatman.money.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String type; // "income" or "expense"
    public double amount;
    public String category;
    public long date;

    public Transaction(String type, double amount, String category, long date) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
}
