package service.model.day;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Day {

    private final LocalDate localDate;
    private final WeekType weekTypeName;
    private DayType dayType;
    private String description = "설명 없음(기본값)";

    private Day(LocalDate localDate, WeekType weekTypeName, DayType dayType) {
        this.localDate = localDate;
        this.weekTypeName = weekTypeName;
        this.dayType = dayType;
    }

    public static Day makeDay(LocalDate current) {
        DayOfWeek dayOfWeek = current.getDayOfWeek();
        WeekType weekType = WeekType.from(dayOfWeek);
        DayType dayType = distinguishDayType(dayOfWeek);
        return Day.of(current, weekType, dayType);
    }

    private static DayType distinguishDayType(DayOfWeek dayOfWeek) {
        if (dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY) {
            return DayType.HOLIDAY;
        }
        return DayType.WEEKDAY;
    }

    // 이거 private로 닫을 생각도 해야한다. 아니면 없애던가
    public static Day of(LocalDate localDate, WeekType weekTypeName, DayType dayType) {
        return new Day(localDate, weekTypeName, dayType);
    }

    public boolean isHoliday() {
        return dayType == DayType.HOLIDAY;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public WeekType getWeekTypeName() {
        return weekTypeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getYear() {
        return String.valueOf(localDate.getYear());
    }

    public boolean isSameDate(Day otherDay) {
        return localDate.equals(otherDay.getLocalDate());
    }

    @Override
    public String toString() {
        int year = localDate.getYear() % 100;
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        String formatted = String.format("'%02d.%2d.%2d.", year, month, day);
        return formatted + "(" + weekTypeName.weekName() + ")";
    }

    public void changeDayType(DayType dayType) {
        this.dayType = dayType;
    }
}
