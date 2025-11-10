package service.model.day;

import java.time.LocalDate;

public class Day {
    private final LocalDate localDate;
    private final WeekType weekTypeName;
    private final DayType dayType;

    public Day(LocalDate localDate, WeekType weekTypeName, DayType dayType) {
        this.localDate = localDate;
        this.weekTypeName = weekTypeName;
        this.dayType = dayType;
    }

    public boolean isHoliDay() {
        return dayType.equals(DayType.HoliDay);
    }

    @Override
    public String toString() {
        return localDate + "(" + weekTypeName.getWeekName() + ")";
    }
}
