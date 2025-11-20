package org.example.dutymanager.service.model.day;

import java.util.ArrayList;
import java.util.List;

public class Days {
    private final List<Day> days;

    private Days(List<Day> days) {
        this.days = List.copyOf(days);
    }

    public static Days of(List<Day> days) {
        return new Days(days);
    }

    public boolean contains(Day otherDay){
        return days.stream()
                .anyMatch(day -> day.equals(otherDay));
    }

    public List<Day> getDays() {
        return List.copyOf(days);
    }

    public Days merge(Days other) {
        List<Day> merged = new ArrayList<>(this.days);
        merged.addAll(other.days);

        return Days.of(List.copyOf(merged));
    }
}
