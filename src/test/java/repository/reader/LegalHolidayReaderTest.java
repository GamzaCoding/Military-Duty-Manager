package repository.reader;

import java.io.File;
import org.junit.jupiter.api.Test;
import repository.reader.location.LegalHolidayLocation;
import service.model.day.Day;
import service.model.day.Days;

class LegalHolidayReaderTest {

    @Test
    void 법정공휴일_데이터_엑셀파일을_읽어오는_기능() {
        // given
        LegalHolidayReader legalHolidayReader = new LegalHolidayReader();
        String location = LegalHolidayLocation.LOCATION;
        File file = new File(location);

        // when
        Days days = legalHolidayReader.readLegalHolidays(file, "2025년");

        // then
        days.getDays().stream()
                .map(Day::toString)
                .forEach(System.out::println);
    }
}