package repository.writer.sampleData;

import java.time.LocalDate;
import java.util.List;
import service.model.day.Day;
import service.model.day.DayType;

public final class Sample {

    public static final Day SAMPLE_HOLIDAY = Day.of(date(2025, 1, 1), DayType.HOLIDAY, "새해 첫날");
    public static final List<String> HOLIDAY_CATEGORY = List.of("날짜", "요일", "명칭");
    public static final String SAMPLE_SHEET_NAME = "2025년";

    private Sample() {
    }

    private static LocalDate date(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }
}

