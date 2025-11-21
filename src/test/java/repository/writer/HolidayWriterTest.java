package repository.writer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.example.dutymanager.repository.FileLocation.HolidayFileLocation;
import org.example.dutymanager.repository.writer.HolidayWriter;
import org.example.dutymanager.service.model.day.Day;
import org.example.dutymanager.service.model.day.DayType;
import org.junit.jupiter.api.Test;


class HolidayWriterTest {

    @Test
    void 휴일_추가_기능() throws IOException {
        // given
        HolidayFileLocation legalHolidayLocation = new HolidayFileLocation();
        File location = legalHolidayLocation.getFile();
        Day day = Day.of(LocalDate.of(2025, 11,11), DayType.HOLIDAY, "전투휴무");
        HolidayWriter legalHolidayWriter = new HolidayWriter();

        // when, then
        legalHolidayWriter.add(location, day);
        legalHolidayWriter.remove(location, day);
    }

    @Test
    void 휴일_삭제_기능() throws IOException {
        // given
        HolidayFileLocation legalHolidayLocation = new HolidayFileLocation();
        File location = legalHolidayLocation.getFile();
        Day day = Day.of(LocalDate.of(2025, 3,1), DayType.HOLIDAY);
        HolidayWriter legalHolidayWriter = new HolidayWriter();

        // when, then
        legalHolidayWriter.remove(location, day);
        legalHolidayWriter.add(location, day);
    }
}