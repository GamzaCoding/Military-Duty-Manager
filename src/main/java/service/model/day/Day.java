package service.model.day;

import java.time.LocalDate;

public class Day {
    private final LocalDate localDate;
    private final Week weekName;
    private final DayType dayType;

    public Day(LocalDate localDate, Week weekName, DayType dayType) {
        this.localDate = localDate;
        this.weekName = weekName;
        this.dayType = dayType;
    }

    public boolean isHoliDay() {
        return dayType.equals(DayType.HoliDay);
    }

    @Override
    public String toString() {
        return localDate + "(" + weekName.getWeekName() + ")";
    }
}
