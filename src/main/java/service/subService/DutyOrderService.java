package service.subService;

import java.util.Comparator;
import java.util.List;
import service.model.day.Days;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;
import service.model.person.Persons;

public class DutyOrderService {

    // 평일, 휴일에 대한 당직 순서를 받아서 엑셀 파일에 하나의 당직표를 작성하는 서비스
    public static Duties makeResultDuty(Days days, Persons weekPersons, Persons holidayPersons) {
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

        // 평일, 휴일 각각의 순서 인덱스 (독립적 순환)
        int[] weekIndex = {0};
        int[] holidayIndex = {0};

        List<Duty> dutiesResult = days.getDays().stream()
                .map(day -> {
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
}
