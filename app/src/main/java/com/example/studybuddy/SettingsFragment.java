package com.example.studybuddy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;
import java.util.stream.Collectors;

public class SettingsFragment extends Fragment {

    ListView listView;
    Context context;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();
        listView = view.findViewById(R.id.appListView);
        SwitchMaterial switchButton = view.findViewById(R.id.keep_blocked_switch);
        switchButton.setChecked(AppInfo.alwaysBlock); // set the status as we stored it
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> AppInfo.alwaysBlock = isChecked);
        getAllApps();
    }

    public void getAllApps() {
        // TODO if does not work, may need to add permissions (QUERY_ALL_PACKAGES permissions)
        // filter out the system applications
        if (AppInfo.appInfoHashMap.isEmpty()){
            List<ApplicationInfo> allApps = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA).stream()
                    .filter(appInfo -> ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || appInfo.packageName.equals("com.google.android.youtube")) && !(appInfo.packageName.equals("com.example.studybuddy")))
                    .collect(Collectors.toList());

            for (int ind = 0; ind < allApps.size(); ind++){
                ApplicationInfo appInfo = allApps.get(ind);
                AppInfo conciseAppInfo = new AppInfo(appInfo.loadLabel(context.getPackageManager()).toString(), appInfo.packageName, appInfo.loadIcon(context.getPackageManager()), false);
                AppInfo.appInfoHashMap.put(appInfo.packageName, conciseAppInfo);
            }
        }

        AppAdapter appAdapter = new AppAdapter(context, R.layout.app_cell);
        listView.setAdapter(appAdapter);
    }
}