package service.subService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import repository.FileLocation.HolidayFileLocation;
import repository.reader.HolidayReader;
import service.model.day.Day;
import service.model.day.DayType;
import service.model.day.Days;

public class DutyDayService {
    private final HolidayReader holidayReader;
    private final HolidayFileLocation location;

    public DutyDayService() {
        this.holidayReader = new HolidayReader();
        this.location = new HolidayFileLocation();
    }

    /**
     * 원하는 기간(시작일, 마지막일)을 입력받아 휴일(법정공휴일, 전투휴무 등)을 고려하여 당직을 위한 Days 를 반환한다.
     */
    public Days makeDutyDays(LocalDate startDate, LocalDate endDate) throws IOException {
        Days holidays = findHolidayFromTo(startDate, endDate);
        List<Day> dutyDays = Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .map(date -> {
                    Day day = Day.makeDay(date);
                    if (holidays.isContain(day)) {
                        day.changeDayType(DayType.HOLIDAY);
                    }
                    return day;
                })
                .toList();

        return Days.of(dutyDays);
    }


    private Days findHolidayFromTo(LocalDate startDate, LocalDate endDate) throws IOException {
        if (isTwoYears(startDate, endDate)) {
            Days StartDateYearHolidays = holidayReader.readHolidays(location.getFile(),
                    String.valueOf(startDate.getYear()));
            Days endDateYearHolidays = holidayReader.readHolidays(location.getFile(),
                    String.valueOf(endDate.getYear()));
            return StartDateYearHolidays.merge(endDateYearHolidays);
        }
        return holidayReader.readHolidays(location.getFile(), String.valueOf(startDate.getYear()));
    }

    private boolean isTwoYears(LocalDate startDate, LocalDate endDate) {
        return startDate.getYear() != endDate.getYear();
    }
}
