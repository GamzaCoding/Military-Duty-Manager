package repository.reader;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import repository.FileLocation.HolidayFileLocation;
import service.model.day.Day;
import service.model.day.Days;

class LegalHolidayReaderTest {

    @Test
    void 법정공휴일_데이터_엑셀파일을_읽어오는_기능() throws IOException {
        // given
        HolidayReader legalHolidayReader = new HolidayReader();
        HolidayFileLocation location = new HolidayFileLocation();

        // when
        Days days = legalHolidayReader.readHolidays(location.getFile(), "2025");

        // then
        days.getDays().stream()
                .map(Day::toString)
                .forEach(System.out::println);
    }
}