package service.model.day;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

public class Day implements Comparable<Day> {

    public static final String DEFAULT_DESCRIPTION = "설명 없음(기본값)";

    private final LocalDate localDate;
    private final DayOfWeek dayOfWeek;
    private final DayType dayType;
    private final String description;

    private Day(LocalDate localDate, DayType dayType, String description) {
        this.localDate = localDate;
        this.dayOfWeek = localDate.getDayOfWeek();
        this.dayType = dayType;
        this.description = description;
    }

    public static Day from(LocalDate current) {
        DayOfWeek dayOfWeek = current.getDayOfWeek();
        DayType dayType = distinguishBasicDayType(dayOfWeek);
        return Day.of(current, dayType);
    }

    public static Day of(LocalDate localDate, DayType dayType) {
        return new Day(localDate, dayType, DEFAULT_DESCRIPTION);
    }

    public static Day of(LocalDate localDate, DayType dayType, String description) {
        return new Day(localDate, dayType, description);
    }

    public Day convertHoliday() {
        return Day.of(localDate, DayType.HOLIDAY);
    }

    public Day settingDescription(String description) {
        return Day.of(localDate, dayType, description);
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

    public boolean isSameDayType(Day otherDay) {
        return dayType == otherDay.getDayType();
    }

    public DayType getDayType () {
        return dayType;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDayOfWeekName() {
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }

    public int getYear() {
        return localDate.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Day day = (Day) o;
        return Objects.equals(localDate, day.localDate) && dayOfWeek == day.dayOfWeek
                && dayType == day.dayType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDate, dayOfWeek, dayType);
    }

    @Override
    public String toString() {
        int year = localDate.getYear() % 100;
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        String formatted = String.format("'%02d.%2d.%2d.", year, month, day);
        return formatted + "(" + getDayOfWeekName() + ")";
    }

    @Override
    public int compareTo(Day otherDay) {
        return this.localDate.compareTo(otherDay.getLocalDate());
    }
}
