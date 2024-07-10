package com.example.studybuddy;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

// Service that will run in the background and determine if an app needs to be blocked
public class AppLockService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public AppLockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        handler = new Handler();
        // The criteria for showing a blocked screen is checked according to the defined delays
        runnable = () -> {
            String app = getForegroundApp();
            boolean hourlyBlock = checkHourlyBlock();
            if((AppInfo.alwaysBlock || hourlyBlock) && AppInfo.appInfoHashMap.containsKey(app) && AppInfo.appInfoHashMap.get(app).isBlocked()){
                showBlockScreen();
            }
            handler.postDelayed(runnable, 10000);
        };

        handler.postDelayed(runnable, 15000);
    }

    // checks if there is any scheduled blocks at the specific time
    private boolean checkHourlyBlock() {
        LocalTime currTime = LocalTime.now();
        List<Event> events = Event.eventsList.stream().filter(event -> event.getDate().equals(LocalDate.now())
                && currTime.isAfter(event.getFromTime()) && currTime.isBefore(event.getToTime())).collect(Collectors.toList());
        return events.stream().anyMatch(Event::isBlock);

    }

    // Once the service is removed, sto running the check
    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
    }

    // Determines what method is currently in the foreground using usage stats
    public String getForegroundApp() {
        String currentApp = "NULL";
        UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && !appList.isEmpty()) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }

        return currentApp;
    }

    // Shows the blocked screen window to prevent user from viewing blocked app
    public void showBlockScreen(){
        BlockWindow window= new BlockWindow(this);
        window.open();
    }

}