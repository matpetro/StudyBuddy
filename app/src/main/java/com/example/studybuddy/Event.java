package com.example.studybuddy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            int eventStartHour = event.fromTime.getHour();
            int eventEndHour = event.toTime.getHour();
            int cellHour = time.getHour();
            if(event.getDate().equals(date) && cellHour >= eventStartHour  && cellHour < eventEndHour)
                events.add(event);
        }

        return events;
    }


    private String name;
    private LocalDate date;
    private LocalTime fromTime;
    private LocalTime toTime;
    private String descr;
    private boolean block;

    public Event(String name, LocalDate date, LocalTime fromTime, LocalTime toTime, String descr, boolean block)
    {
        this.name = name;
        this.date = date;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.descr = descr;
        this.block = block;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }
}
