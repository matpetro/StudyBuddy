package com.example.studybuddy;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class WeeklyViewActivity extends AppCompatActivity {
    private DayScrollDatePicker mPicker;
    private ListView hourListView;
    private LocalDate currentSelectedDate;
    private LocalDate currentStartDate;
    private LocalDate currentEndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weekly_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.weeklyView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpPicker();
        daySelected();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setHourAdapter();
    }

    private void setUpPicker(){
        // get the info from this intent
        Intent intent = getIntent();
        int[] startDate = intent.getIntArrayExtra("startOfWeek");
        int[] endDate = intent.getIntArrayExtra("endOfWeek");
        int[] chosenDate = intent.getIntArrayExtra("selectedDay");

        mPicker = findViewById(R.id.day_date_picker);
        mPicker.setStartDate(startDate[0],startDate[1],startDate[2]);
        mPicker.setEndDate(endDate[0], endDate[1], endDate[2]);
        currentStartDate = LocalDate.of(startDate[2], startDate[1], startDate[0]);
        currentEndDate = LocalDate.of(endDate[2], endDate[1], endDate[0]);
        currentSelectedDate = LocalDate.of(chosenDate[2], chosenDate[1], chosenDate[0]);
    }

    private void daySelected() {
        OnDateSelectedListener listener = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                if (date != null) {
                    currentSelectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    setHourAdapter();
                }
            }
        };
        mPicker.getSelectedDate(listener);
    }

    public void backToMonthlyView(View view) {
        finish();
    }

    public void forwardCalender(View view) {
        currentStartDate = currentStartDate.plusDays(7);
        currentEndDate = currentEndDate.plusDays(7);
        currentSelectedDate = currentSelectedDate.plusDays(7);

        mPicker.setStartDate(currentStartDate.getDayOfMonth(), currentStartDate.getMonthValue(), currentStartDate.getYear());
        mPicker.setEndDate(currentEndDate.getDayOfMonth(), currentEndDate.getMonthValue(), currentEndDate.getYear());
        setHourAdapter();
    }

    public void goBackInCalender(View view) {
        currentStartDate = currentStartDate.minusDays(7);
        currentEndDate = currentEndDate.minusDays(7);
        currentSelectedDate = currentSelectedDate.minusDays(7);

        mPicker.setStartDate(currentStartDate.getDayOfMonth(), currentStartDate.getMonthValue(), currentStartDate.getYear());
        mPicker.setEndDate(currentEndDate.getDayOfMonth(), currentEndDate.getMonthValue(), currentEndDate.getYear());
        setHourAdapter();
    }

    private void setHourAdapter()
    {
        hourListView = findViewById(R.id.hourListView);
        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList()
    {
        ArrayList<HourEvent> list = new ArrayList<>();

        for(int hour = 0; hour < 24; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Event> events = Event.eventsForDateAndTime(currentSelectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
        }

        return list;
    }

    public void newEventAction(View view)
    {
        Intent intent = new Intent(this, EventEditActivity.class);
        intent.putExtra("selectedDate", new int[]{currentSelectedDate.getDayOfMonth(), currentSelectedDate.getMonthValue(), currentSelectedDate.getYear()});
        startActivity(intent);
    }
}