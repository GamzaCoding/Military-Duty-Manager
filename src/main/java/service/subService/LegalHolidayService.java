package service.subService;

import java.io.IOException;
import repository.reader.LegalHolidayReader;
import repository.writer.LegalHolidayWriter;
import repository.FileLocation.LegalHolidayFileLocation;
import service.model.day.Day;

public class LegalHolidayService {

    private final LegalHolidayReader holidayReader;
    private final LegalHolidayWriter holidayWriter;
    private final LegalHolidayFileLocation location;

    public LegalHolidayService() {
        this.holidayReader = new LegalHolidayReader();
        this.holidayWriter = new LegalHolidayWriter();
        this.location = new LegalHolidayFileLocation();
    }

    /**
     * holidays에 휴일을 찾아서 삭제하는 기능
     */
    public void makeDayAsHoliday(Day day) throws IOException {
        holidayWriter.remove(location.getLocation(), day);
    }

    /**
     * holidays에 평일 데이터를 집어 넣는 기능
     */
    public void makeDayAsWeekday(Day day) throws IOException {
        holidayWriter.add(location.getLocation(), day);
    }
}
