package org.example.dutymanager.service.subService;

import java.io.File;
import java.time.LocalDate;
import org.example.dutymanager.repository.FileLocation.HolidayFileLocation;
import org.example.dutymanager.repository.writer.HolidayWriter;
import org.example.dutymanager.service.model.day.Day;

public class HolidayService {
    private final HolidayWriter holidayWriter;
    private final HolidayFileLocation location;

    public HolidayService() {
        this.holidayWriter = new HolidayWriter();
        this.location = new HolidayFileLocation();
    }

    public void makeDayAsHoliday(LocalDate targetDate, String description) {
        Day targetDay = Day.from(targetDate);
        Day holiday = targetDay.settingDescription(description);
        holidayWriter.add(location.getFile(), holiday);
    }

    public void makeDayAsWeekday(LocalDate targetDate) {
        Day targetDay = Day.from(targetDate);
        holidayWriter.remove(location.getFile(), targetDay);
    }

    public boolean alreadyDBExist() {
        File file = location.getFile();
        return file.exists() && file.isFile();
    }

    public void changeDBName() {
        location.renameFile(location.getFile());
    }

    public void createHolidayDB() {
        holidayWriter.write(location.getFile());
    }

    public File getDB() {
        return location.getFile();
    }
}
