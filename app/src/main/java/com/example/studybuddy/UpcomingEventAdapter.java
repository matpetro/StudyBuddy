package com.example.studybuddy;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.switchmaterial.SwitchMaterial;

// allows us to use a custom component to represent the apps on the phone with a list view
public class UpcomingEventAdapter extends ArrayAdapter<Event> {
    public UpcomingEventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.upcoming_cell, null);
        }

        Event event = getItem(position);
        TextView eventName = convertView.findViewById(R.id.upEventNameTV);
        TextView eventDate = convertView.findViewById(R.id.upEventDateTV);
        TextView eventTime = convertView.findViewById(R.id.upEventTimeTV);
        LinearLayout eventLayout = convertView.findViewById(R.id.upEventLayout);

        eventName.setText(event.getName());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d yyyy");
        eventDate.setText(event.getDate().format(dateFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        eventTime.setText(event.getFromTime().format(timeFormatter));

        eventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EventInfoActivity.class);
                intent.putExtra("selectedEvent", event);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}

