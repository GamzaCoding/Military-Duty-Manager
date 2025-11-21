package org.example.dutymanager.repository.sampleData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.example.dutymanager.service.model.day.Day;
import org.example.dutymanager.service.model.day.DayType;

public class SampleHolidays {
    public final List<Day> sampleHolidays;

    public SampleHolidays() {
        this.sampleHolidays = SampleHolidays.init();
    }

    private static List<Day> init() {
        List<Day> list = new ArrayList<>();
        list.add(Day.of(date(2025, 1, 1), DayType.HOLIDAY, "새해 첫날"));
        list.add(Day.of(date(2025, 1, 28), DayType.HOLIDAY, "설 연휴"));
        list.add(Day.of(date(2025, 1, 29), DayType.HOLIDAY, "설 연휴"));
        list.add(Day.of(date(2025, 1, 30), DayType.HOLIDAY, "설 연휴"));
        list.add(Day.of(date(2025, 3, 1), DayType.HOLIDAY, "삼일절"));
        list.add(Day.of(date(2025, 3, 3), DayType.HOLIDAY, "삼일절 대체휴무일"));
        list.add(Day.of(date(2025, 5, 5), DayType.HOLIDAY, "어린이날, 부처님 오신 날"));
        list.add(Day.of(date(2025, 5, 6), DayType.HOLIDAY, "어린이날 대체휴무일"));
        list.add(Day.of(date(2025, 6, 6), DayType.HOLIDAY, "현충일"));
        list.add(Day.of(date(2025, 8, 15), DayType.HOLIDAY, "광복절"));
        list.add(Day.of(date(2025, 10, 3), DayType.HOLIDAY, "개천절"));
        list.add(Day.of(date(2025, 10, 5), DayType.HOLIDAY, "추석 연휴"));
        list.add(Day.of(date(2025, 10, 6), DayType.HOLIDAY, "추석 연휴"));
        list.add(Day.of(date(2025, 10, 7), DayType.HOLIDAY, "추석 연휴"));
        list.add(Day.of(date(2025, 10, 8), DayType.HOLIDAY, "추석 대체휴휴일"));
        list.add(Day.of(date(2025, 10, 9), DayType.HOLIDAY, "한글날"));
        list.add(Day.of(date(2025, 12, 25), DayType.HOLIDAY, "크리스마스"));
        list.add(Day.of(date(2026, 1, 1), DayType.HOLIDAY, "새해 첫날"));
        list.add(Day.of(date(2026, 2, 16), DayType.HOLIDAY, "설 연휴"));
        list.add(Day.of(date(2026, 2, 17), DayType.HOLIDAY, "설 연휴"));
        list.add(Day.of(date(2026, 2, 18), DayType.HOLIDAY, "설 연휴"));
        list.add(Day.of(date(2026, 3, 1), DayType.HOLIDAY, "삼일절"));
        list.add(Day.of(date(2026, 3, 2), DayType.HOLIDAY, "삼일절 대체휴무일"));
        list.add(Day.of(date(2026, 5, 5), DayType.HOLIDAY, "어린이날"));
        list.add(Day.of(date(2026, 5, 24), DayType.HOLIDAY, "부처님 오신 날"));
        list.add(Day.of(date(2026, 5, 25), DayType.HOLIDAY, "부처님 오신 날 대체휴무일"));
        list.add(Day.of(date(2026, 6, 3), DayType.HOLIDAY, "지방선거일"));
        list.add(Day.of(date(2026, 6, 6), DayType.HOLIDAY, "현충일"));
        list.add(Day.of(date(2026, 8, 15), DayType.HOLIDAY, "광복절"));
        list.add(Day.of(date(2026, 8, 17), DayType.HOLIDAY, "광복절 대체휴무일"));
        list.add(Day.of(date(2026, 9, 24), DayType.HOLIDAY, "추석 연휴"));
        list.add(Day.of(date(2026, 9, 25), DayType.HOLIDAY, "추석 연휴"));
        list.add(Day.of(date(2026, 9, 26), DayType.HOLIDAY, "추석 연휴"));
        list.add(Day.of(date(2026, 10, 3), DayType.HOLIDAY, "개천절"));
        list.add(Day.of(date(2026, 10, 5), DayType.HOLIDAY, "개천절 대체휴무일"));
        list.add(Day.of(date(2026, 10, 9), DayType.HOLIDAY, "한글날"));
        list.add(Day.of(date(2026, 12, 25), DayType.HOLIDAY, "크리스마스"));
        return list;
    }

    public List<String> getSampleYears() {
        return sampleHolidays.stream()
                .map(Day::getYear)
                .collect(Collectors.toSet())
                .stream()
                .map(String::valueOf)
                .toList();
    }

    public List<Day> getSampleHolidays() {
        return sampleHolidays;
    }

    private static LocalDate date(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }
}
