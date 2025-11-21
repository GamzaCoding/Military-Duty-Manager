package repository.reader;

import java.io.IOException;
import org.example.dutymanager.repository.FileLocation.HolidayFileLocation;
import org.example.dutymanager.repository.reader.HolidayReader;
import org.example.dutymanager.service.model.day.Day;
import org.example.dutymanager.service.model.day.Days;
import org.junit.jupiter.api.Test;

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