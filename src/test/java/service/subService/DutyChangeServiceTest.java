package service.subService;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import service.model.day.Day;
import service.model.day.Days;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;
import service.model.person.Persons;

class DutyChangeServiceTest {

    @Test
    void 상호_당직_교환_기능() throws IOException {
        // given
        DutyDayService dutyDayService = new DutyDayService();
        Days days = dutyDayService
                .makeDutyDays(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

        List<Person> weekPeople = List.of(
                Person.from(1, "대령", "태조", null, null),
                Person.from(2, "중령", "정종", null, null),
                Person.from(3, "소령", "태종", null, null),
                Person.from(4, "대위", "세종", null, null),
                Person.from(5, "중위", "문종", null, null)

        );
        List<Person> holiPeople = List.of(
                Person.from(1, "gold", "Faker", null, null),
                Person.from(2, "silver", "Oner", null, null)
        );
        Persons weekPersons = Persons.of(weekPeople);
        Persons holiPersons = Persons.of(holiPeople);

        DutyOrderService dutyOrderService = new DutyOrderService();
        Duties duties = dutyOrderService.makeResultDuty(days, weekPersons, holiPersons);

        Duty dutyTo = Duty.of(
                Day.from(LocalDate.of(2025,11,1)),
                Person.from(1, "gold", "Faker", null, null)
        );

        Duty dutyFrom = Duty.of(
                Day.from(LocalDate.of(2025,11,2)),
                Person.from(2, "silver", "Oner", null, null)
        );

        // when
        DutyChangeService dutyChangeService = new DutyChangeService();
        Duties changedDuties = dutyChangeService.changeDutyBothSides(duties, dutyTo, dutyFrom);

        // then
        changedDuties.getDuties().stream()
                .map(Duty::toString)
                .forEach(System.out::println);
    }

    @Test
    void 단일_당직_교환_기능() throws IOException {
        // given
        DutyDayService dutyDayService = new DutyDayService();
        Days days = dutyDayService
                .makeDutyDays(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

        List<Person> weekPeople = List.of(
                Person.from(1, "대령", "태조", null, null),
                Person.from(2, "중령", "정종", null, null),
                Person.from(3, "소령", "태종", null, null),
                Person.from(4, "대위", "세종", null, null),
                Person.from(5, "중위", "문종", null, null)

        );
        List<Person> holiPeople = List.of(
                Person.from(1, "gold", "Faker", null, null),
                Person.from(2, "silver", "Oner", null, null)
        );
        Persons weekPersons = Persons.of(weekPeople);
        Persons holiPersons = Persons.of(holiPeople);

        DutyOrderService dutyOrderService = new DutyOrderService();
        Duties duties = dutyOrderService.makeResultDuty(days, weekPersons, holiPersons);

        Duty dutyTo = Duty.of(
                Day.from(LocalDate.of(2025,11,1)),
                Person.from(1, "gold", "Faker", null, null)
        );

        Person targetPerson = Person.from(5, "중위", "문종", null, null);

        // when
        DutyChangeService dutyChangeService = new DutyChangeService();
        Duties changedDuties = dutyChangeService.changeDutyOneSide(duties, dutyTo, targetPerson);

        // then
        changedDuties.getDuties().stream()
                .map(Duty::toString)
                .forEach(System.out::println);
    }

    @Test
    void 평일_휴일_당직자끼리_당직_교환_시도_시_예외_발생() throws IOException {
        // given
        DutyDayService dutyDayService = new DutyDayService();
        Days days = dutyDayService
                .makeDutyDays(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

        List<Person> weekPeople = List.of(
                Person.from(1, "대령", "태조", null, null),
                Person.from(2, "중령", "정종", null, null),
                Person.from(3, "소령", "태종", null, null),
                Person.from(4, "대위", "세종", null, null),
                Person.from(5, "중위", "문종", null, null)

        );
        List<Person> holiPeople = List.of(
                Person.from(1, "gold", "Faker", null, null),
                Person.from(2, "silver", "Oner", null, null)
        );
        Persons weekPersons = Persons.of(weekPeople);
        Persons holiPersons = Persons.of(holiPeople);

        DutyOrderService dutyOrderService = new DutyOrderService();
        Duties duties = dutyOrderService.makeResultDuty(days, weekPersons, holiPersons);

        Duty dutyTo = Duty.of(
                Day.from(LocalDate.of(2025,11,1)),
                Person.from(1, "gold", "Faker", null, null)
        );

        Duty dutyFrom = Duty.of(
                Day.from(LocalDate.of(2025,11,3)),
                Person.from(1, "대령", "태조", null, null)
        );

        // when
        DutyChangeService dutyChangeService = new DutyChangeService();
        // then

        assertThatThrownBy(() -> dutyChangeService.changeDutyBothSides(duties, dutyTo, dutyFrom))
                .isInstanceOf(IllegalStateException.class);
    }
}