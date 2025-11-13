package service.model.day;

import java.time.DayOfWeek;

public enum WeekType {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일")
    ;

    private final String weekName;

    WeekType(String weekName) {
        this.weekName = weekName;
    }

    public static WeekType from(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> MONDAY;
            case TUESDAY -> TUESDAY;
            case WEDNESDAY -> WEDNESDAY;
            case THURSDAY -> THURSDAY;
            case FRIDAY -> FRIDAY;
            case SATURDAY -> SATURDAY;
            case SUNDAY -> SUNDAY;
        };
    }

    public String weekName() {
        return weekName;
    }
}
