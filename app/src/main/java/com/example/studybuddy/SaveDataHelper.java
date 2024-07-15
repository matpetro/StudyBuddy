package com.example.studybuddy;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SaveDataHelper {

    private static final String SHARED_PREF_KEY = "shared_pref";
    private static final String EVENTS_KEY = "scheduled_events";
    private static final String BLOCKED_APPS_KEY = "blocked_apps";
    // Save the events to local storage using shared preferences
    public static void saveEvents(Context context){
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Special serializers and deserializers were needed for LocalDate and LocalTime as Gson could not handle them
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer());
        Gson gson = gsonBuilder.create();

        // Array had to be copied or it would not be saved properly
        ArrayList<Event> testCopy = new ArrayList<>(Event.eventsList);

        String text = gson.toJson(testCopy);
        // Save to shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EVENTS_KEY, text);
        editor.apply();
    }

    // load the events from local Storage
    public static void loadEvents(Context context){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer());
        Gson gson = gsonBuilder.create();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<Event>>(){}.getType();
        ArrayList<Event> results = gson.fromJson(sharedPreferences.getString(EVENTS_KEY, null), type);
        // need to copy over the results into our event array so that its sort on add functionality is not reset by reassignment
        if (results != null && !results.isEmpty()){
            Event.eventsList.addAll(results);
        }

    }

    public static void saveBlockedApps(Context context){
        Gson gson = new Gson();

        HashMap<String, Boolean> blockedVals = new HashMap<>();
        blockedVals.put("alwaysBlock", AppInfo.alwaysBlock);
        for (Map.Entry<String, AppInfo> entry : AppInfo.appInfoHashMap.entrySet()) {
            String key = entry.getKey();
            boolean value = entry.getValue().isBlocked();
            blockedVals.put(key, value);
        }

        String text = gson.toJson(blockedVals);
        // Save to shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BLOCKED_APPS_KEY, text);
        editor.apply();

    }

    public static HashMap<String, Boolean> loadBlockedApps(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String, Boolean>>(){}.getType();
        HashMap<String, Boolean> results = gson.fromJson(sharedPreferences.getString(BLOCKED_APPS_KEY, null), type);
        if (results != null && !results.isEmpty()){
            return results;
        }
        return null;
    }
}
