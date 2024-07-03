package com.example.studybuddy;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Set initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, new HomeFragment())
                    .commit();
        }

        // Set listener for navigation item selection
        bottomNavigationView.setOnItemSelectedListener(item -> switchFragment(item.getItemId()));

        checkPermissions();
        startService(new Intent(this, AppLockService.class));
    }

    protected void highlightCalenderItem(){
        bottomNavigationView.setOnItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(R.id.nav_calendar);
        bottomNavigationView.setOnItemSelectedListener(item -> switchFragment(item.getItemId()));
    }

    private void checkPermissions(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usm.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, cal.getTimeInMillis(), System.currentTimeMillis());
        // check if any usage in the past year
        if (queryUsageStats.isEmpty()) {
            System.out.println("The user may not allow the access to apps usage.");
            Intent intent = new Intent(this, UsagePermissionActivity.class);
            startActivity(intent);
        } else if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(this, PopupPermissionActivity.class);
            startActivity(intent);
        }

    }

    private boolean switchFragment(int itemId) {
        Fragment selectedFragment = null;

        if (itemId == R.id.nav_home){
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_calendar){
            selectedFragment = new CalendarFragment();
        }else if (itemId == R.id.nav_settings){
            selectedFragment = new SettingsFragment();
        }

        // Perform the fragment transaction
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, selectedFragment)
                    .commit();
            return true;
        }

        return false;
    }
}