package com.greatman.money;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FrameLayout container;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// Inflate container programmatically to keep layout simple
        container = findViewById(R.id.container);
        // Add the HomeFragment by default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
        }

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment f = null;
            if (item.getItemId() == R.id.nav_home) f = new HomeFragment();
            else if (item.getItemId() == R.id.nav_money) f = new MoneyFragment();
            else if (item.getItemId() == R.id.nav_goals) f = new GoalsFragment();
            else if (item.getItemId() == R.id.nav_reports) f = new ReportsFragment();

            if (f != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, f)
                        .commit();
                return true;
            }
            return false;
        });
    }
}
