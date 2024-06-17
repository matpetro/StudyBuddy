package com.example.studybuddy;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.stream.Collectors;

public class AppSelectActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_select);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.appListView);
        getAllApps();
    }

    public void getAllApps() {
        // TODO if does not work, may need to add permissions (QUERY_ALL_PACKAGES permissions)
        // filter out the system applications
        List<ApplicationInfo> allApps = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA)
                .stream().filter(appInfo -> (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || appInfo.packageName.equals("com.google.android.youtube")).collect(Collectors.toList());
        AppAdapter appAdapter = new AppAdapter(this, R.layout.app_cell, allApps);
        listView.setAdapter(appAdapter);
    }
}