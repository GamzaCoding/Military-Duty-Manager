package service.subService;

import java.util.ArrayList;
import java.util.List;
import service.model.day.Day;
import service.model.day.Days;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;
import service.model.person.Persons;

public class DutyOrderService {

    public Duties makeResultDuty(Days days, Persons weekPersons, Persons holidayPersons) {
        validatePersonsIsEmpty(weekPersons, holidayPersons);

        DutyPicker weekPicker = new DutyPicker(weekPersons.gerSortedPersons());
        DutyPicker holidayPicker = new DutyPicker(holidayPersons.gerSortedPersons());

        Person prevPerson = null;
        List<Duty> result = new ArrayList<>();
        for (Day day : days.getDays()) {
            Person resultPerson = assignPersonForDay(day, weekPicker, holidayPicker, prevPerson);
            result.add(Duty.of(day, resultPerson));
            prevPerson = resultPerson;
        }
        return Duties.of(result);
    }

    private Person assignPersonForDay(Day day, DutyPicker weekPicker, DutyPicker holidayPicker, Person prevPerson) {
        if (day.isHoliday()) {
            return holidayPicker.pickAvoiding(prevPerson);
        }
        return weekPicker.pickAvoiding(prevPerson);
    }

    private void validatePersonsIsEmpty(Persons weekPersons, Persons holidayPersons) {
        if (weekPersons.isEmpty() || holidayPersons.isEmpty()) {
            throw new IllegalStateException("평일 또는 휴일 당직자 목록이 비었습니다.");
        }
    }

    private static class DutyPicker {
        private final Persons persons;
        private int orderIndex = 0;

        private DutyPicker(Persons persons) {
            this.persons = persons;
        }

        private Person pickAvoiding(Person prevPerson) {
            int size = persons.size();
            Person currentPerson = pickAvoidingPersonWithManyPerson(prevPerson, size);
            if (currentPerson != null) {
                return currentPerson;
            }
            return pickOnlyOnePerson(size);
        }

        private Person pickOnlyOnePerson(int size) {
            Person onlyOnePerson = persons.getPerson(orderIndex);
            orderIndex = (orderIndex + 1) % size;
            return onlyOnePerson;
        }

        private Person pickAvoidingPersonWithManyPerson(Person prevPerson, int size) {
            for (int i = 0; i < size; i++) {
                int currentIndex = (orderIndex + i) % size;
                Person currentPerson = persons.getPerson(currentIndex);
                if (!currentPerson.equals(prevPerson)) {
                    orderIndex = (currentIndex + 1) % size;
                    return currentPerson;
                }
            }
            return null;
        }
    }
}
