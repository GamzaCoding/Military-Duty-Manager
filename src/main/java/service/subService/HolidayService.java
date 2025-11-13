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

    /**
     * holidays에 휴일을 찾아서 삭제하는 기능
     */
    public void makeDayAsHoliday(Day day) throws IOException {
        holidayWriter.remove(location.getFile(), day);
    }

    /**
     * holidays에 평일 데이터를 집어 넣는 기능
     */
    public void makeDayAsWeekday(Day day) throws IOException {
        holidayWriter.add(location.getFile(), day);
    }
}
