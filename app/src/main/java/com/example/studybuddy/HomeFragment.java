package com.example.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

// Home fragment or home page of the main activity
public class HomeFragment extends Fragment {

    ListView upcomingEventsView;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // Need this on resume to ensure the upcoming events list is updated properly
    @Override
    public void onResume() {
        super.onResume();
        setUpcomingEventAdapter(getView());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // grabbing our calender view by ID
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        // when user touches a date, take them to the daily calender view
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
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
            int[] firstDay = {selectedDate.get(Calendar.DAY_OF_MONTH), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.YEAR)};
            int[] lastDay = {lastDayOfWeek.get(Calendar.DAY_OF_MONTH), lastDayOfWeek.get(Calendar.MONTH) + 1, lastDayOfWeek.get(Calendar.YEAR)};
            int[] selectedDay = {dayOfMonth, month + 1, year};
            weeklyAction(firstDay, lastDay, selectedDay);
        });

        upcomingEventsView = view.findViewById(R.id.upcomingListView);
        setUpcomingEventAdapter(view);
        // set the empty view, which is what is shown when the adapter is empty. Must be defined in xml
        upcomingEventsView.setEmptyView(view.findViewById(R.id.empty_view));
    }

    // takes care of switching from the home fragment to the daily calender fragment, and sending the needed data over between fragments
    public void weeklyAction(int[] startOfWeek, int[] endOfWeek, int[] selectedDay){
        // switch fragments to the daily view
        Fragment calFrag = new CalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putIntArray("startOfWeek", startOfWeek);
        bundle.putIntArray("endOfWeek", endOfWeek);
        bundle.putIntArray("selectedDay", selectedDay);
        // info is sent as part of the fragment bundle
        calFrag.setArguments(bundle);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, calFrag);
        transaction.commit();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.highlightCalenderItem();
        }
    }

    // Takes care of showing all the upcoming events in the upcming events list
    private void setUpcomingEventAdapter(View view)
    {
        UpcomingEventAdapter adapter = new UpcomingEventAdapter(view.getContext(), Event.eventsList.subList(0, Math.min(5, Event.eventsList.size())));
        upcomingEventsView.setAdapter(adapter);
    }

}