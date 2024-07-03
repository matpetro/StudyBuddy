package com.example.studybuddy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

// allows us to use a custom component to represent the apps on the phone with a list view
public class AppAdapter extends ArrayAdapter<ApplicationInfo> {
    private List<AppInfo> appsList;
    private Context context;

    public AppAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        this.appsList = new ArrayList<>(AppInfo.appInfoHashMap.values());
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.app_cell, null);
        }

        AppInfo data = appsList.get(position);
        if (null != data) {
            TextView appName = view.findViewById(R.id.app_name);
            ImageView iconView = view.findViewById(R.id.app_icon);

            SwitchMaterial switchButton = view.findViewById(R.id.switch_app);
            switchButton.setTag(position); // set the tag so we can identify the correct row in the listener
            switchButton.setChecked(data.isBlocked()); // set the status as we stored it
            switchButton.setOnCheckedChangeListener(mListener); // set the listener

            appName.setText(data.getName());
            iconView.setImageDrawable(data.getLogo());
        }
        return view;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // when an app switch is pressed, update its blocked status
            AppInfo data = appsList.get((Integer)buttonView.getTag());
            data.setBlocked(isChecked);
        }
    };
}
