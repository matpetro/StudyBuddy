package com.example.studybuddy;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                String app = getForegroundApp();
                boolean hourlyBlock = checkHourlyBlock();
                if((AppInfo.alwaysBlock || hourlyBlock) && AppInfo.appInfoHashMap.containsKey(app) && AppInfo.appInfoHashMap.get(app).isBlocked()){
                    showBlockScreen();
                }
                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    private boolean checkHourlyBlock() {
        LocalTime currTime = LocalTime.now();
        List<Event> events = Event.eventsList.stream().filter(event -> event.getDate().equals(LocalDate.now())
                && currTime.isAfter(event.getFromTime()) && currTime.isBefore(event.getToTime())).collect(Collectors.toList());
        return events.stream().anyMatch(Event::isBlock);

    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

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
    public void showBlockScreen(){
        BlockWindow window= new BlockWindow(this);
        window.open();
    }

}