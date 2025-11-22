package org.example.dutymanager.service.subService;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import org.example.dutymanager.repository.FileLocation.TutorialFileLocation;
import org.example.dutymanager.repository.writer.TutorialWriter;
import org.example.dutymanager.service.model.person.Person;
import org.example.dutymanager.service.model.person.Persons;

public class TutorialService {
    TutorialFileLocation tutorialFileLocation = new TutorialFileLocation();

    public void generateTutorialFiles() {
        Persons weekdayPersons = createWeekdayPersons();
        Persons holidayPersons = createHolidayPersons();

        TutorialWriter tutorialWriter = TutorialWriter.of(weekdayPersons, holidayPersons);
        tutorialWriter.write(tutorialFileLocation.getFile());
    }

    public File getFile() {
        return tutorialFileLocation.getFile();
    }

    private Persons createWeekdayPersons() {
        return Persons.of(List.of(
                person(1, "대위", "최해군", day(2023,11,17), day(2026,2,1)),
                person(2, "중위", "김왕건", day(2024,2,1), day(2026,1,11)),
                person(3, "중위(진)", "박진해", day(2025,7,6), day(9999,12,30)),
                person(4, "소위", "이평택", day(2025,8,4), day(2026,6,29))
        ));
    }

    private Persons createHolidayPersons() {
        return Persons.of(List.of(
                person(1, "상사", "대서양", day(2023,11,17), day(2026,2,1)),
                person(2, "중사", "태평양", day(2024,2,1), day(2026,1,11)),
                person(3, "중사(진)", "대한민국", day(2025,7,6), day(9999,12,30)),
                person(4, "하사", "동해", day(2025,8,4), day(2026,6,29))
        ));
    }

    private Person person(int id, String rank, String name, LocalDate in, LocalDate out) {
        return Person.from(id, rank, name, in, out);
    }

    private LocalDate day(int y, int m, int d) {
        return LocalDate.of(y, m, d);
    }
}
