package com.greatman.money;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.greatman.money.model.Transaction;
import com.greatman.money.model.Goal;

public class Utils {

    // Export transactions and goals as JSON to external files and return file URIs
    @Nullable
    public static Uri exportData(Context ctx, List<Transaction> txs, List<Goal> goals) {
        try {
            JSONObject root = new JSONObject();
            JSONArray at = new JSONArray();
            if (txs != null) for (Transaction t : txs) {
                JSONObject o = new JSONObject();
                o.put("type", t.type);
                o.put("amount", t.amount);
                o.put("category", t.category);
                o.put("date", t.date);
                at.put(o);
            }
            JSONArray ag = new JSONArray();
            if (goals != null) for (Goal g : goals) {
                JSONObject o = new JSONObject();
                o.put("name", g.name);
                o.put("target", g.target);
                o.put("current", g.current);
                o.put("emergency", g.emergency);
                ag.put(o);
            }
            root.put("transactions", at);
            root.put("goals", ag);

            String fname = "greatman_export_" + System.currentTimeMillis() + ".json";
            File dir = ctx.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (dir == null) dir = ctx.getFilesDir();
            File out = new File(dir, fname);
            try (FileOutputStream fos = new FileOutputStream(out)) {
                fos.write(root.toString(2).getBytes());
            }
            return Uri.fromFile(out);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
