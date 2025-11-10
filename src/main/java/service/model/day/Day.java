package service.model.day;

import java.time.LocalDate;

public class Day {

    private final LocalDate localDate;
    private final WeekType weekTypeName;
    private final DayType dayType;

    private Day(LocalDate localDate, WeekType weekTypeName, DayType dayType) {
        this.localDate = localDate;
        this.weekTypeName = weekTypeName;
        this.dayType = dayType;
    }

    public static Day of(LocalDate localDate, WeekType weekTypeName, DayType dayType) {
        return new Day(localDate, weekTypeName, dayType);
    }

    public boolean isHoliDay() {
        return dayType == DayType.HOLIDAY;
    }

    @Override
    public String toString() {
        int year = localDate.getYear() % 100;
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        String formatted = String.format("'%02d.%2d.%2d.", year, month, day);
        return formatted + "(" + weekTypeName.getWeekName() + ")";
    }
}
