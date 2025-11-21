package repository.writer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dutymanager.repository.FileLocation.ResultFileLocation;
import org.example.dutymanager.repository.writer.subWriter.DutyTableWriter;
import org.example.dutymanager.service.model.day.Day;
import org.example.dutymanager.service.model.duty.Duties;
import org.example.dutymanager.service.model.duty.Duty;
import org.example.dutymanager.service.model.person.Person;
import org.junit.jupiter.api.Test;

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

        Day day0 = Day.from(LocalDate.of(2025, 11, 1));
        Day day1 = Day.from(LocalDate.of(2025, 11, 2));
        Day day2 = Day.from(LocalDate.of(2025, 3, 3));
        Day day3 = Day.from(LocalDate.of(2025, 11, 4));
        Day day4 = Day.from(LocalDate.of(2025, 11, 30));
        Day day5 = Day.from(LocalDate.of(2025, 7, 31));
        Day day6 = Day.from(LocalDate.of(2025, 11, 11));

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
        }
    }

}