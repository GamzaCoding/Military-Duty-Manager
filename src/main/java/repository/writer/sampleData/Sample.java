package repository.writer.sampleData;

import java.time.LocalDate;
import java.util.List;
import service.model.day.Day;
import service.model.day.DayType;

public final class Sample {

    public static final Day SAMPLE_HOLIDAY = Day.of(date(2025, 1, 1), DayType.HOLIDAY, "새해 첫날");
    public static final List<String> HOLIDAY_CATEGORY = List.of("날짜", "요일", "명칭");
    public static final List<String> DUTY_ORDER_CATEGORY = List.of("순번", "계급", "이름", "전입일(예정일 포함)", "전출일(예정일 포함)");
    public static final String HOLIDAY_SAMPLE_SHEET = "2025년";
    public static final String WEEKDAY_DUTY_ORDER_SHEET = "당직자 순서(평일)";
    public static final String HOLIDAY_DUTY_ORDER_SHEET = "당직자 순서(휴일)";

    private Sample() {
    }

    private static LocalDate date(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }
}

