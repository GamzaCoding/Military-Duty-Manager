package service.model.duty;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import service.model.day.Day;
import service.model.person.Person;
import service.model.person.Persons;

public class Duties {
    private final List<Duty> duties;

    private Duties(List<Duty> duties) {
        this.duties = duties;
    }

    // 이 메서드 없앨 생각도 해야한다.
    public static Duties of(List<Duty> duties) {
         return new Duties(duties);
    }

    public static Duties makeResultDuty(LocalDate startDate, LocalDate endDate, Persons weekPersons, Persons holidayPersons) {
        if (weekPersons.isEmpty() || holidayPersons.isEmpty()) {
            throw new IllegalStateException("평일 또는 휴일 당직자 목록이 비었습니다.");
        }

        // position 기준 정렬 (당직 순서)
        List<Person> sortedWeekPersons = weekPersons.getPersons()
                .stream()
                .sorted(Comparator.comparingInt(Person::position))
                .toList();

        List<Person> sortedHolidayPersons = holidayPersons.getPersons()
                .stream()
                .sorted(Comparator.comparingInt(Person::position))
                .toList();

        // 평일 / 휴일 각각의 순서 인덱스 (독립적 순환)
        int[] weekIndex = {0};
        int[] holidayIndex = {0};

        List<Duty> dutiesResult = Stream
                .iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .map(date -> {
                    Day day = Day.makeDay(date);
                    Person dutyPerson;

                    if (day.isHoliday()) {
                        dutyPerson = sortedHolidayPersons.get(holidayIndex[0] % sortedHolidayPersons.size());
                        holidayIndex[0]++;
                    } else {
                        dutyPerson = sortedWeekPersons.get(weekIndex[0] % sortedWeekPersons.size());
                        weekIndex[0]++;
                    }

                    return Duty.of(day, dutyPerson);
                })
                .toList();

        return Duties.of(dutiesResult);
    }


    // 이거 duty안으로 옮겨볼까?
    private static Duty makeDuty(LocalDate startDate, LocalDate current, Persons weekPersons, Persons holidayPersons) {
        Day day = Day.makeDay(current);

        if (day.isHoliday()) {
            int dutyOrderIndex = calculateDutyOrderIndex(startDate, current, holidayPersons);
            Person holidayPerson = holidayPersons.getPerson(dutyOrderIndex);
            return Duty.of(day, holidayPerson);
        }

        int dutyOrderIndex = calculateDutyOrderIndex(startDate, current, weekPersons);
        Person weekPerson = weekPersons.getPerson(dutyOrderIndex);
        return Duty.of(day, weekPerson);
    }

    private static int calculateDutyOrderIndex(LocalDate startDate, LocalDate current, Persons persons) {
        return (int) ChronoUnit.DAYS.between(startDate, current) % persons.size();
    }

    public List<Duty> getDuties() {
        return duties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        duties.forEach(duty -> sb.append(duty.toString()));

        return sb.toString();
    }
}
