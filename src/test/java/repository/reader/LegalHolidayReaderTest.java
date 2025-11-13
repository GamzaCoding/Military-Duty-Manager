package repository.reader;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import repository.FileLocation.LegalHolidayFileLocation;
import service.model.day.Day;
import service.model.day.Days;

class LegalHolidayReaderTest {

    @Test
    void 법정공휴일_데이터_엑셀파일을_읽어오는_기능() throws IOException {
        // given
        LegalHolidayReader legalHolidayReader = new LegalHolidayReader();
        LegalHolidayFileLocation location = new LegalHolidayFileLocation();

        // when
        Days days = legalHolidayReader.readLegalHolidays(location.getLocation(), "2025년");

        // then
        days.getDays().stream()
                .map(Day::toString)
                .forEach(System.out::println);
    }
}