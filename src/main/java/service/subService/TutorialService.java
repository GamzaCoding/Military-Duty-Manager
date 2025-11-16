package service.subService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import repository.FileLocation.HolidayFileLocation;
import repository.FileLocation.TutorialFileLocation;
import repository.writer.HolidayWriter;
import repository.writer.TutorialWriter;
import service.model.person.Person;
import service.model.person.Persons;

public class TutorialService {

    public TutorialService() {
    }

    public void createWeekdayHoliDayDutyOrderFile() throws IOException {

        Person firstSamplePersonOfWeekday = Person.from(1, "대위", "최해군", LocalDate.of(2023, 11, 17), LocalDate.of(2026, 2, 1));
        Person secondSamplePersonOfWeekday = Person.from(2, "중위", "김왕건", LocalDate.of(2024, 2, 1), LocalDate.of(2026, 1, 11));
        Person thirdSamplePersonOfWeekday = Person.from(3, "중위(진)", "박진해", LocalDate.of(2025, 7, 6), LocalDate.of(9999, 12, 30));
        Person fourthSamplePersonOfWeekday = Person.from(4, "소위", "이평택", LocalDate.of(2025, 8, 4), LocalDate.of(2026, 6, 29));
        Persons weekdayPersons = Persons.of(
                List.of(firstSamplePersonOfWeekday, secondSamplePersonOfWeekday, thirdSamplePersonOfWeekday, fourthSamplePersonOfWeekday));

        Person firstSamplePersonOfHoliday = Person.from(1, "상사", "대서양", LocalDate.of(2023, 11, 17), LocalDate.of(2026, 2, 1));
        Person secondSamplePersonOfHoliday = Person.from(2, "중사", "태평양", LocalDate.of(2024, 2, 1), LocalDate.of(2026, 1, 11));
        Person thirdSamplePersonOfHoliday = Person.from(3, "중사(진)", "대한민국", LocalDate.of(2025, 7, 6), LocalDate.of(9999, 12, 30));
        Person fourthSamplePersonOfHoliday = Person.from(4, "하사", "동해", LocalDate.of(2025, 8, 4), LocalDate.of(2026, 6, 29));
        Persons holidayPersons = Persons.of(
                List.of(firstSamplePersonOfHoliday, secondSamplePersonOfHoliday, thirdSamplePersonOfHoliday, fourthSamplePersonOfHoliday));

        TutorialWriter tutorialWriter = TutorialWriter.of(weekdayPersons, holidayPersons);
        TutorialFileLocation tutorialFileLocation = new TutorialFileLocation();
        File tutorialFile = tutorialFileLocation.getFile();
        tutorialWriter.write(tutorialFile);

        HolidayFileLocation holidayFileLocation = new HolidayFileLocation();
        File holidayFile = holidayFileLocation.getFile();
        HolidayWriter holidayWriter = new HolidayWriter();
        holidayWriter.write(holidayFile);
    }
}
