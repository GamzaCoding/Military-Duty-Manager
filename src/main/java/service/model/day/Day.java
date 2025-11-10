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
        return dayType == DayType.HoliDay;
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
