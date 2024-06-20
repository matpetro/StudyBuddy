package com.example.studybuddy;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // grabbing our calender view by ID
        CalendarView calendarView = findViewById(R.id.calendarView);
        // when user touches a date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //System.out.println(year + " " + month + " " + dayOfMonth);
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                int weekOfYear = selectedDate.get(Calendar.WEEK_OF_YEAR);
                //System.out.println("week of year: "+ weekOfYear);
                selectedDate.clear();
                selectedDate.set(Calendar.WEEK_OF_YEAR, weekOfYear);
                selectedDate.set(Calendar.YEAR, year);
                // Set the first day of the week to Sunday
                selectedDate.setFirstDayOfWeek(Calendar.SUNDAY);
                // Get the last day of the week
                Calendar lastDayOfWeek = (Calendar) selectedDate.clone();
                lastDayOfWeek.add(Calendar.DAY_OF_WEEK, 7);
                // Extract day, month, and year from first day of the week
                int[] firstDay = {selectedDate.get(Calendar.DAY_OF_MONTH), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.YEAR)};
                int[] lastDay = {lastDayOfWeek.get(Calendar.DAY_OF_MONTH), lastDayOfWeek.get(Calendar.MONTH) + 1, lastDayOfWeek.get(Calendar.YEAR)};
                int[] selectedDay = {dayOfMonth, month + 1, year};
                weeklyAction(firstDay, lastDay, selectedDay);
            }
        });

        checkPermissions();
        startService(new Intent(this, AppLockService.class));
    }

    public void weeklyAction(int[] startOfWeek, int[] endOfWeek, int[] selectedDay){
        // define that we want to switch activities
        Intent intent = new Intent(this, WeeklyViewActivity.class);
        // add the extra info we want the next activity to have access to
        intent.putExtra("startOfWeek", startOfWeek);
        intent.putExtra("endOfWeek", endOfWeek);
        intent.putExtra("selectedDay", selectedDay);
        startActivity(intent);
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

    public void viewBlockedApps(View view) {
        Intent intent = new Intent(this, AppSelectActivity.class);
        startActivity(intent);
    }
}