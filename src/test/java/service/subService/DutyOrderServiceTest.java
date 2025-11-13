package service.subService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import service.model.day.Days;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;
import service.model.person.Persons;

class DutyOrderServiceTest {

    @Test
    void 연속_당직_시_강제로_평일별_휴일별_다음_당직자와_변경되는_기능() throws IOException {

        // given
        DutyDayService dutyDayService = new DutyDayService();
        Days days = dutyDayService
                .makeDutyDays(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

        List<Person> weekPeople = List.of(
                Person.from(1, "대령", "태조", null, null),
                Person.from(2, "중령", "정종", null, null)

        );
        List<Person> holiPeople = List.of(
                Person.from(1, "대령", "태조", null, null),
                Person.from(2, "중령", "정종", null, null)
        );
        Persons weekPersons = Persons.of(weekPeople);
        Persons holiPersons = Persons.of(holiPeople);

        // when
        Duties duties = DutyOrderService.makeResultDuty(days, weekPersons, holiPersons);

        // then
        duties.getDuties().stream()
                .map(Duty::toString)
                .forEach(System.out::println);
    }
}