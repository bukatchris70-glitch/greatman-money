package com.greatman.money.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class Goal {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public double target;
    public double current;
    public boolean emergency;

    public Goal(String name, double target, double current, boolean emergency) {
        this.name = name;
        this.target = target;
        this.current = current;
        this.emergency = emergency;
    }
}
