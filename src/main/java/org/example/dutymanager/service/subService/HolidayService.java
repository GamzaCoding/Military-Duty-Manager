package org.example.dutymanager.service.subService;

import java.io.File;
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

    public void makeDayAsHoliday(Day day, String description) {
        Day holiday = day.settingDescription(description);
        holidayWriter.add(location.getFile(), holiday);
    }

    public void makeDayAsWeekday(Day day) {
        holidayWriter.remove(location.getFile(), day);
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
}
