package repository.writer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import repository.writer.location.LegalHolidayLocation;
import service.model.day.Day;
import service.model.day.DayType;
import service.model.day.WeekType;

class LegalHolidayWriterTest {

    @Test
    void 휴일_추가_기능() throws IOException {
        // given
        LegalHolidayLocation legalHolidayLocation = new LegalHolidayLocation();
        File location = legalHolidayLocation.getLocation();
        Day day = Day.of(LocalDate.of(2025, 11,11), WeekType.TUESDAY, DayType.HOLIDAY);
        day.setDescription("전투휴무");
        LegalHolidayWriter legalHolidayWriter = new LegalHolidayWriter();

        // when, then
        legalHolidayWriter.add(location, day);
    }

    @Test
    void 휴일_삭제_기능() throws IOException {
        // given
        LegalHolidayLocation legalHolidayLocation = new LegalHolidayLocation();
        File location = legalHolidayLocation.getLocation();
        Day day = Day.of(LocalDate.of(2025, 3,1), WeekType.SATURDAY, DayType.HOLIDAY);
        LegalHolidayWriter legalHolidayWriter = new LegalHolidayWriter();

        // when, then
        legalHolidayWriter.remove(location, day);
    }
}