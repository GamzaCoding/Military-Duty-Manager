package repository.writer;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import service.model.Duty;
import service.model.Person;
import service.model.day.Day;
import service.model.day.DayType;
import service.model.day.WeekType;

class DutyResultWriterTest {

    @Test
    void 당직표_7개_입력_테스트() throws IOException {
        // given
        Person person0 = Person.from(null, "대위", "최대위", null, null);
        Person person1 = Person.from(null, "중위", "최중위", null, null);
        Person person2 = Person.from(null, "소위", "최소위", null, null);
        Person person3 = Person.from(null, "대령", "최대령", null, null);
        Person person4 = Person.from(null, "중령", "최중령", null, null);
        Person person5 = Person.from(null, "소령", "최소령", null, null);
        Person person6 = Person.from(null, "준위", "최준위", null, null);

        Day day0 = new Day(LocalDate.of(2025, 11, 1), WeekType.Monday, DayType.WeekDay);
        Day day1 = new Day(LocalDate.of(2025, 11, 2), WeekType.Tuesday, DayType.WeekDay);
        Day day2 = new Day(LocalDate.of(2025, 11, 3), WeekType.Wednesday, DayType.WeekDay);
        Day day3 = new Day(LocalDate.of(2025, 11, 4), WeekType.Thursday, DayType.WeekDay);
        Day day4 = new Day(LocalDate.of(2025, 11, 5), WeekType.Friday, DayType.WeekDay);
        Day day5 = new Day(LocalDate.of(2025, 11, 6), WeekType.Saturday, DayType.WeekDay);
        Day day6 = new Day(LocalDate.of(2025, 11, 7), WeekType.Sunday, DayType.WeekDay);

        List<Duty> duties = List.of(new Duty(day0, person0),
                new Duty(day1, person1),
                new Duty(day2, person2),
                new Duty(day3, person3),
                new Duty(day4, person4),
                new Duty(day5, person5),
                new Duty(day6, person6)
        );

        ExcelFileWriter dutyResultWriter = DutyResultWriter.of(duties);
        ResultFileLocation resultFileLocation = new ResultFileLocation();
        File outputFile = resultFileLocation.getLocation();

        // when

        dutyResultWriter.write(outputFile);

        // then
    }

}