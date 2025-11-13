package repository.writer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import repository.FileLocation.HolidayFileLocation;
import service.model.day.Day;
import service.model.day.DayType;
import service.model.day.WeekType;

class HolidayWriterTest {

    @Test
    void 휴일_추가_기능() throws IOException {
        // given
        HolidayFileLocation legalHolidayLocation = new HolidayFileLocation();
        File location = legalHolidayLocation.getFile();
        Day day = Day.of(LocalDate.of(2025, 11,11), WeekType.TUESDAY, DayType.HOLIDAY);
        day.setDescription("전투휴무");
        HolidayWriter legalHolidayWriter = new HolidayWriter();

        // when, then
        legalHolidayWriter.add(location, day);
    }

    @Test
    void 휴일_삭제_기능() throws IOException {
        // given
        HolidayFileLocation legalHolidayLocation = new HolidayFileLocation();
        File location = legalHolidayLocation.getFile();
        Day day = Day.of(LocalDate.of(2025, 3,1), WeekType.SATURDAY, DayType.HOLIDAY);
        HolidayWriter legalHolidayWriter = new HolidayWriter();

        // when, then
        legalHolidayWriter.remove(location, day);
    }
}