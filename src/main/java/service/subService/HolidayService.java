package service.subService;

import java.io.IOException;
import repository.writer.HolidayWriter;
import repository.FileLocation.HolidayFileLocation;
import service.model.day.Day;

public class HolidayService {
    private final HolidayWriter holidayWriter;
    private final HolidayFileLocation location;

    public HolidayService() {
        this.holidayWriter = new HolidayWriter();
        this.location = new HolidayFileLocation();
    }

    public void makeDayAsHoliday(Day day) throws IOException {
        holidayWriter.remove(location.getFile(), day);
    }

    public void makeDayAsWeekday(Day day) throws IOException {
        holidayWriter.add(location.getFile(), day);
    }
}
