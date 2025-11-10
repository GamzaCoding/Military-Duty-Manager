package service.model.day;

import java.time.LocalDateTime;

public class Day {
    private final LocalDateTime localDateTime;
    private final DayType dayType;
    private final Week weekName;

    public Day(LocalDateTime localDateTime, DayType dayType, Week weekName) {
        this.localDateTime = localDateTime;
        this.dayType = dayType;
        this.weekName = weekName;
    }
}
