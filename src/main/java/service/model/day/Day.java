package service.model.day;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

public class Day {

    public static final String DEFAULT_DESCRIPTION = "설명 없음(기본값)";

    private final LocalDate localDate;
    private final WeekType weekName;
    private final DayType dayType;
    private final String description;

    private Day(LocalDate localDate, WeekType weekTypeName, DayType dayType, String description) {
        this.localDate = localDate;
        this.weekName = weekTypeName;
        this.dayType = dayType;
        this.description = description;
    }

    public static Day from(LocalDate current) {
        DayOfWeek dayOfWeek = current.getDayOfWeek();
        WeekType weekType = WeekType.from(dayOfWeek);
        DayType dayType = distinguishBasicDayType(dayOfWeek);
        return Day.of(current, weekType, dayType);
    }
    public static Day of(LocalDate localDate, WeekType weekTypeName, DayType dayType) {
        return new Day(localDate, weekTypeName, dayType, DEFAULT_DESCRIPTION);
    }

    public static Day of(LocalDate localDate, WeekType weekTypeName, DayType dayType, String description) {
        return new Day(localDate, weekTypeName, dayType, description);
    }

    public Day convertHoliday() {
        return Day.of(localDate, weekName, DayType.HOLIDAY);
    }

    public Day settingDescription(String description) {
        return Day.of(localDate, weekName, dayType, description);
    }

    private static DayType distinguishBasicDayType(DayOfWeek dayOfWeek) {
        if (dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY) {
            return DayType.HOLIDAY;
        }
        return DayType.WEEKDAY;
    }

    public boolean isHoliday() {
        return dayType == DayType.HOLIDAY;
    }

    public DayType getDayType () {
        return dayType;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public WeekType getWeekName() {
        return weekName;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return localDate.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Day day = (Day) o;
        return Objects.equals(localDate, day.localDate) && weekName == day.weekName
                && dayType == day.dayType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDate, weekName, dayType);
    }

    @Override
    public String toString() {
        int year = localDate.getYear() % 100;
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        String formatted = String.format("'%02d.%2d.%2d.", year, month, day);
        return formatted + "(" + weekName.weekName() + ")";
    }
}
