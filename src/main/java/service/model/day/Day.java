package service.model.day;

import java.time.LocalDate;

public class Day {
    private final LocalDate localDate;
    private final DayType dayType;
    private final Week weekName;

    public Day(LocalDate localDate, DayType dayType, Week weekName) {
        this.localDate = localDate;
        this.dayType = dayType;
        this.weekName = weekName;
    }

    public boolean isHoliDay() {
        return dayType.equals(DayType.HoliDay);
    }

    @Override
    public String toString() {
        return localDate + "(" + weekName + ")";
    }
}
