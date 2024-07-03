package com.example.studybuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.format.DateTimeFormatter;

public class EventInfoActivity extends AppCompatActivity {
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.eventInfoPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(30, systemBars.top, 30, systemBars.bottom);
            return insets;
        });
        event = (Event) getIntent().getSerializableExtra("selectedEvent");
        setUpView();
    }

    private void setUpView() {
        TextView name = findViewById(R.id.eventInfoNameTV);
        TextView date = findViewById(R.id.eventInfoDateTV);
        TextView time = findViewById(R.id.eventInfoTimeTV);
        TextView desc = findViewById(R.id.eventInfoDescTV);
        TextView blocked = findViewById(R.id.eventInfoBlockedTV);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        name.setText(event.getName());
        date.setText(event.getDate().format(dateFormatter));
        time.setText(String.format("From %s To %s", event.getFromTime().format(timeFormatter), event.getToTime().format(timeFormatter)));
        desc.setText(event.getDescr());
        String blockedOrNot = event.isBlock() ? " " : " not ";
        blocked.setText(String.format("Apps are%sblocked during this time", blockedOrNot));
    }


    public void deleteEvent(View view) {
        System.out.println(Event.eventsList);
        Event.eventsList.remove(event);
        System.out.println(Event.eventsList);
        finish();
    }
}