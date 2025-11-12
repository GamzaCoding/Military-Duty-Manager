package service.model.day;

import java.util.List;

public class Days {
    private final List<Day> days;

    private Days(List<Day> days) {
        this.days = days;
    }

    public static Days of(List<Day> days) {
        return new Days(days);
    }

    public List<Day> getDays() {
        return days;
    }
}
