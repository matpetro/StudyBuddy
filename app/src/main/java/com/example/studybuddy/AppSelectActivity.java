package com.example.studybuddy;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

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
        SwitchMaterial switchButton = findViewById(R.id.keep_blocked_switch);
        switchButton.setChecked(AppInfo.alwaysBlock); // set the status as we stored it
        switchButton.setOnCheckedChangeListener(new SwitchMaterial.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppInfo.alwaysBlock = isChecked;
            }
        });
        getAllApps();
    }

    public void getAllApps() {
        // TODO if does not work, may need to add permissions (QUERY_ALL_PACKAGES permissions)
        // filter out the system applications
        if (AppInfo.appInfoHashMap.isEmpty()){
            List<ApplicationInfo> allApps = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA).stream()
                    .filter(appInfo -> ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || appInfo.packageName.equals("com.google.android.youtube")) && !(appInfo.packageName.equals("com.example.studybuddy")))
                    .collect(Collectors.toList());

            for (int ind = 0; ind < allApps.size(); ind++){
                ApplicationInfo appInfo = allApps.get(ind);
                AppInfo conciseAppInfo = new AppInfo(appInfo.loadLabel(this.getPackageManager()).toString(), appInfo.packageName, appInfo.loadIcon(this.getPackageManager()), false);
                AppInfo.appInfoHashMap.put(appInfo.packageName, conciseAppInfo);
            }
        }

        AppAdapter appAdapter = new AppAdapter(this, R.layout.app_cell);
        listView.setAdapter(appAdapter);
    }

    public void backToMain(View view) {
        finish();
    }
}