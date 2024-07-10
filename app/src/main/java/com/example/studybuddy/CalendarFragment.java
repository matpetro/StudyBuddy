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

// fragment of the main activity that deals with the daily calender
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
        view.findViewById(R.id.addNewEventButton).setOnClickListener(this::newEventAction);
        view.findViewById(R.id.calendarBack).setOnClickListener(this::goBackInCalender);
        view.findViewById(R.id.calendarForward).setOnClickListener(this::forwardCalender);
        setUpPicker();
        daySelected();
    }

    // The on resume ensures that whenever a modification took place the list views will be correctly updated
    @Override
    public void onResume() {
        super.onResume();
        setHourAdapter();
    }

    // Sets up the picker so that the correct dates are displaying
    private void setUpPicker(){
        // gets the info from the main fragment if there is any, if not just uses the present date
        int[] startDate;
        int[] endDate;
        int[] chosenDate;
        if (getArguments() != null && getArguments().containsKey("startOfWeek")
                && getArguments().containsKey("endOfWeek") && getArguments().containsKey("selectedDay")){
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

    // Changes the list view to show any scheduled events for the selected date
    private void daySelected() {
        OnDateSelectedListener listener = date -> {
            if (date != null) {
                currentSelectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                setHourAdapter();
            }
        };
        mPicker.getSelectedDate(listener);
    }

    // Used to forward the calender by a weeks
    public void forwardCalender(View view) {
        currentStartDate = currentStartDate.plusDays(7);
        currentEndDate = currentEndDate.plusDays(7);
        currentSelectedDate = currentSelectedDate.plusDays(7);

        mPicker.setStartDate(currentStartDate.getDayOfMonth(), currentStartDate.getMonthValue(), currentStartDate.getYear());
        mPicker.setEndDate(currentEndDate.getDayOfMonth(), currentEndDate.getMonthValue(), currentEndDate.getYear());
        setHourAdapter();
    }

    // used to go backwards by a week
    public void goBackInCalender(View view) {
        currentStartDate = currentStartDate.minusDays(7);
        currentEndDate = currentEndDate.minusDays(7);
        currentSelectedDate = currentSelectedDate.minusDays(7);

        mPicker.setStartDate(currentStartDate.getDayOfMonth(), currentStartDate.getMonthValue(), currentStartDate.getYear());
        mPicker.setEndDate(currentEndDate.getDayOfMonth(), currentEndDate.getMonthValue(), currentEndDate.getYear());
        setHourAdapter();
    }

    // Sets the adapter that will have all the hours of the day and any associated events
    private void setHourAdapter()
    {
        hourListView = v.findViewById(R.id.hourListView);
        HourAdapter hourAdapter = new HourAdapter(context.getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    // Gets the events for each hour on the list
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

    // Opens the new event activity
    public void newEventAction(View view)
    {
        Intent intent = new Intent(context, EventEditActivity.class);
        intent.putExtra("selectedDate", new int[]{currentSelectedDate.getDayOfMonth(), currentSelectedDate.getMonthValue(), currentSelectedDate.getYear()});
        startActivity(intent);
    }
}