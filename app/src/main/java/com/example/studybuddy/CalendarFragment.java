package com.example.studybuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarFragment extends Fragment {

    private Context context;
    private View v;
    private DayScrollDatePicker mPicker;
    private ListView hourListView;
    private LocalDate currentSelectedDate;
    private LocalDate currentStartDate;
    private LocalDate currentEndDate;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();
        v = view;
        view.findViewById(R.id.addNewEventButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEventAction(v);
            }
        });
        view.findViewById(R.id.calendarBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackInCalender(v);
            }
        });
        view.findViewById(R.id.calendarForward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardCalender(v);
            }
        });
        setUpPicker();
        daySelected();
    }

    @Override
    public void onResume() {
        super.onResume();
        setHourAdapter();
    }

    private void setUpPicker(){
        // get the info from this intent
        int[] startDate;
        int[] endDate;
        int[] chosenDate;
        if (getArguments() != null && getArguments().containsKey("startOfWeek")
                && getArguments().containsKey("endOfWeek") && getArguments().containsKey("selectedDay")){
            System.out.println("GOT THE PASSED ITEMS CORRECTLY");
            // get the info from this intent
            startDate = getArguments().getIntArray("startOfWeek");
            endDate = getArguments().getIntArray("endOfWeek");
            chosenDate = getArguments().getIntArray("selectedDay");
        } else {
            Calendar selectedDate = Calendar.getInstance();
            int year = selectedDate.get(Calendar.YEAR);
            int month = selectedDate.get(Calendar.MONTH);
            int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

            int weekOfYear = selectedDate.get(Calendar.WEEK_OF_YEAR);
            //System.out.println("week of year: "+ weekOfYear);
            selectedDate.clear();
            selectedDate.set(Calendar.WEEK_OF_YEAR, weekOfYear);
            selectedDate.set(Calendar.YEAR, year);
            // Set the first day of the week to Sunday
            selectedDate.setFirstDayOfWeek(Calendar.SUNDAY);
            // Get the last day of the week
            Calendar lastDayOfWeek = (Calendar) selectedDate.clone();
            lastDayOfWeek.add(Calendar.DAY_OF_WEEK, 7);
            // Extract day, month, and year from first day of the week
            startDate = new int[]{selectedDate.get(Calendar.DAY_OF_MONTH), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.YEAR)};
            endDate = new int[]{lastDayOfWeek.get(Calendar.DAY_OF_MONTH), lastDayOfWeek.get(Calendar.MONTH) + 1, lastDayOfWeek.get(Calendar.YEAR)};
            chosenDate = new int[]{dayOfMonth, month + 1, year};
        }

        mPicker = v.findViewById(R.id.day_date_picker);
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
        hourListView = v.findViewById(R.id.hourListView);
        HourAdapter hourAdapter = new HourAdapter(context.getApplicationContext(), hourEventList());
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
        Intent intent = new Intent(context, EventEditActivity.class);
        intent.putExtra("selectedDate", new int[]{currentSelectedDate.getDayOfMonth(), currentSelectedDate.getMonthValue(), currentSelectedDate.getYear()});
        startActivity(intent);
    }
}