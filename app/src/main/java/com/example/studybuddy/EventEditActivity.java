package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameET, descET;
    private TextView eventDateTV;
    private Spinner startTimeSpinner, endTimeSpinner;

    private LocalDate selectedDate;
    private SwitchMaterial switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editEvent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidgets();
        setUpSpinners();
        Intent intent = getIntent();
        int[] selectedDateInts = intent.getIntArrayExtra("selectedDate");
        selectedDate = LocalDate.of(selectedDateInts[2], selectedDateInts[1], selectedDateInts[0]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        eventDateTV.setText(String.format("Date: %s", selectedDate.format(formatter)));
    }
    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        String desc = descET.getText().toString();
        boolean blockApps = switchButton.isChecked();
        LocalTime startTime = stringToLocalTime((String) startTimeSpinner.getSelectedItem());
        LocalTime endTime = stringToLocalTime((String) endTimeSpinner.getSelectedItem());

        if (startTime.isAfter(endTime)){
            Toast.makeText(this, "End Time can not be before Start Time!", Toast.LENGTH_SHORT).show();
        } else if (eventName.isEmpty()){
            Toast.makeText(this, "Event Name Can not be empty", Toast.LENGTH_SHORT).show();
        } else {
            Event newEvent = new Event(eventName, selectedDate, startTime, endTime, desc, blockApps);
            Event.eventsList.add(newEvent);
            finish();
        }
    }
    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        descET = findViewById(R.id.eventDescET);
        eventDateTV = findViewById(R.id.eventDateTV);
        startTimeSpinner = findViewById(R.id.startTimeSpinner);
        endTimeSpinner = findViewById(R.id.endTimeSpinner);
        switchButton = findViewById(R.id.eventSwitch);
    }

    private void setUpSpinners(){
        List<String> times = generateTimes();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeSpinner.setAdapter(adapter);
        endTimeSpinner.setAdapter(adapter);

        // Set Default spinner values to be related to the current time
        LocalTime currentTime = LocalTime.now();
        LocalTime hourOnly = currentTime.withMinute(0);
        LocalTime hourAfter = hourOnly.plusHours(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startHourString = hourOnly.format(formatter);
        String endHourString = hourAfter.format(formatter);

        startTimeSpinner.setSelection(times.indexOf(startHourString));
        endTimeSpinner.setSelection(times.indexOf(endHourString));
    }

    private List<String> generateTimes() {
        List<String> times = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        for (int i = 0; i < 24; i++) {
            times.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
        return times;
    }

    private LocalTime stringToLocalTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(timeString, formatter);
    }


}