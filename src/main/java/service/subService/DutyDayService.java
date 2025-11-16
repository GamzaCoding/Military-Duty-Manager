package service.subService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import repository.FileLocation.HolidayFileLocation;
import repository.reader.HolidayReader;
import service.model.day.Day;
import service.model.day.Days;

public class DutyDayService {
    private final HolidayReader holidayReader;
    private final HolidayFileLocation location;

    public DutyDayService() {
        this.holidayReader = new HolidayReader();
        this.location = new HolidayFileLocation();
    }

    public Days makeDutyDays(LocalDate startDate, LocalDate endDate) throws IOException {
        Days holidays = findHolidayFromTo(startDate, endDate);
        List<Day> dutyDays = Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .map(localDate -> createDaysWithHolidayStatus(localDate, holidays))
                .toList();
        return Days.of(dutyDays);
    }

    private Day createDaysWithHolidayStatus(LocalDate localDate, Days holidays) {
        Day day = Day.from(localDate);
        if (holidays.isContain(day)) {
            return day.convertHoliday();
        }
        return day;
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
