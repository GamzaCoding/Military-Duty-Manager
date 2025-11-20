package service.subService;

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

    public void makeDayAsHoliday(Day day, String description) {
        Day holiday = day.settingDescription(description);
        holidayWriter.add(location.getFile(), holiday);
    }

    public void makeDayAsWeekday(Day day) {
        holidayWriter.remove(location.getFile(), day);
    }
}
