package com.example.studybuddy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Event implements Serializable
{
    public static ArrayList<Event> eventsList = new ArrayList<Event>() {
        @Override
        public boolean add(Event mt) {
            super.add(mt);
            this.sort((event1, event2) -> {
                int dateComparison = event1.getDate().compareTo(event2.getDate());
                if (dateComparison == 0) {
                    return event1.getFromTime().compareTo(event2.getFromTime());
                } else {
                    return dateComparison;
                }
            });
            return true;
        }
    };

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return block == event.block &&
                Objects.equals(name, event.name) &&
                Objects.equals(date, event.date) &&
                Objects.equals(fromTime, event.fromTime) &&
                Objects.equals(toTime, event.toTime) &&
                Objects.equals(descr, event.descr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, fromTime, toTime, descr, block);
    }
}
