package com.example.studybuddy;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.switchmaterial.SwitchMaterial;

// allows us to use a custom component to represent the apps on the phone with a list view
public class AppAdapter extends ArrayAdapter<ApplicationInfo> {
    private List<ApplicationInfo> appsList = null;
    private Context context;
    private PackageManager packageManager;
    private ArrayList<Boolean> checkList = new ArrayList<>();

    public AppAdapter(Context context, int textViewResourceId,
                              List<ApplicationInfo> appsList) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.appsList = appsList;
        packageManager = context.getPackageManager();

        for (int i = 0; i < appsList.size(); i++) {
            checkList.add(false);
        }
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
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

        ApplicationInfo data = appsList.get(position);
        if (null != data) {
            TextView appName = view.findViewById(R.id.app_name);
            ImageView iconView = view.findViewById(R.id.app_icon);

            SwitchMaterial switchButton = view.findViewById(R.id.switch_app);
            switchButton.setTag(position); // set the tag so we can identify the correct row in the listener
            switchButton.setChecked(checkList.get(position)); // set the status as we stored it
            switchButton.setOnCheckedChangeListener(mListener); // set the listener

            appName.setText(data.loadLabel(packageManager));
            iconView.setImageDrawable(data.loadIcon(packageManager));
        }
        return view;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkList.set((Integer)buttonView.getTag(),isChecked); // get the tag so we know the row and store the status
        }
    };
}
