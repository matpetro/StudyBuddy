package com.example.studybuddy;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

// Service that will run in the background and determine if an app needs to be blocked
public class AppLockService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    private boolean isBlockWindowOpen = false;
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
            System.out.println(app);
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
        System.out.println("Service was destroyed");
        handler.removeCallbacks(runnable);
    }

    // Determines what method is currently in the foreground using usage stats
    public String getForegroundApp() {
        String packageNameByUsageStats = "NULL";
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        // look at events in past hour and determine the most recent move to foreground
        long start = System.currentTimeMillis() - (3600 * 1000);
        long end = System.currentTimeMillis() + (10 * 1000);
        UsageEvents usageEvents = mUsageStatsManager.queryEvents(start, end);
        UsageEvents.Event event = new UsageEvents.Event();
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                packageNameByUsageStats = event.getPackageName();
            }
        }

        return packageNameByUsageStats;
    }

    // Shows the blocked screen window to prevent user from viewing blocked app
    public void showBlockScreen(){
        if (!isBlockWindowOpen) {  // Check if the BlockWindow is already open
            isBlockWindowOpen = true;
            BlockWindow window = new BlockWindow(this);
            window.open();

            // Set a listener to reset the flag when the BlockWindow is closed
            window.setOnCloseListener(() -> isBlockWindowOpen = false);
        }
    }

}