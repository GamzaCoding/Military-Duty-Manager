package repository.writer;


import static service.model.day.DayType.*;
import static service.model.day.WeekType.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import repository.writer.location.ResultFileLocation;
import repository.writer.subWriter.DutyTableWriter;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;
import service.model.day.Day;

class DutyTableWriterTest {

    @Test
    void 당직표_정상_출력_테스트() throws IOException {
        // given
        Person person0 = Person.from(null, "대위", "최대위", null, null);
        Person person1 = Person.from(null, "중위", "최중위", null, null);
        Person person2 = Person.from(null, "소위", "최소위", null, null);
        Person person3 = Person.from(null, "대령", "최대령", null, null);
        Person person4 = Person.from(null, "중령", "최중령", null, null);
        Person person5 = Person.from(null, "소령", "최소령", null, null);
        Person person6 = Person.from(null, "준위", "최준위", null, null);

        Day day0 = Day.of(LocalDate.of(2025, 11, 1), MONDAY, WEEKDAY);
        Day day1 = Day.of(LocalDate.of(2025, 11, 2), TUESDAY, WEEKDAY);
        Day day2 = Day.of(LocalDate.of(2025, 3, 3), WEDNESDAY, WEEKDAY);
        Day day3 = Day.of(LocalDate.of(2025, 11, 4), THURSDAY, WEEKDAY);
        Day day4 = Day.of(LocalDate.of(2025, 11, 30), FRIDAY, WEEKDAY);
        Day day5 = Day.of(LocalDate.of(2025, 7, 31), SATURDAY, HOLIDAY);
        Day day6 = Day.of(LocalDate.of(2025, 11, 11), SUNDAY, HOLIDAY);

        List<Duty> duties = List.of(
                Duty.of(day0, person0),
                Duty.of(day1, person1),
                Duty.of(day2, person2),
                Duty.of(day3, person3),
                Duty.of(day4, person4),
                Duty.of(day5, person5),
                Duty.of(day6, person6),
                Duty.of(day0, person0),
                Duty.of(day1, person1),
                Duty.of(day2, person2),
                Duty.of(day3, person3),
                Duty.of(day4, person4),
                Duty.of(day5, person5),
                Duty.of(day6, person6),
                Duty.of(day0, person0),
                Duty.of(day1, person1),
                Duty.of(day2, person2),
                Duty.of(day3, person3),
                Duty.of(day4, person4),
                Duty.of(day5, person5),
                Duty.of(day6, person6),
                Duty.of(day0, person0),
                Duty.of(day1, person1),
                Duty.of(day2, person2),
                Duty.of(day3, person3),
                Duty.of(day4, person4),
                Duty.of(day5, person5),
                Duty.of(day6, person6),
                Duty.of(day1, person1),
                Duty.of(day2, person2),
                Duty.of(day3, person3),
                Duty.of(day4, person4),
                Duty.of(day5, person5),
                Duty.of(day6, person6),
                Duty.of(day0, person0),
                Duty.of(day1, person1),
                Duty.of(day2, person2),
                Duty.of(day3, person3),
                Duty.of(day4, person4),
                Duty.of(day5, person5),
                Duty.of(day6, person6)
        );

        Duties dutiesReal = Duties.of(duties);

        // when, then
        try (Workbook workbook = new XSSFWorkbook()) {
            DutyTableWriter dutyTableWriter = new DutyTableWriter(workbook);
            ResultFileLocation resultFileLocation = new ResultFileLocation();

            dutyTableWriter.writeDutyTable("당직표", dutiesReal);
            dutyTableWriter.saveWorkbook(resultFileLocation.getLocation());
        }
    }

}