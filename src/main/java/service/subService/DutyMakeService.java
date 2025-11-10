package service.subService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import service.model.day.Day;
import service.model.day.DayType;
import service.model.day.WeekType;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;
import service.model.person.Persons;

public class DutyMakeService {
    private final Persons persons;

    public DutyMakeService(Persons persons) {
        this.persons = persons;
    }

    // 시작 날짜, 종료 날짜를 입력하면 persons의 순서에 맞게 당직 객체를 생성해주는 서비스
    public Duties makeDuties(LocalDate startDate, LocalDate endDate) {
            List<Person> personList = persons.getPersons();
        if (personList.isEmpty()) {
            throw new IllegalStateException("당직자 목록이 비어 있습니다.");
        }

        List<Duty> duties = Stream
                .iterate(startDate, current -> !current.isAfter(endDate), date -> date.plusDays(1))
                .map(current -> {
                    DayOfWeek dayOfWeek = current.getDayOfWeek();
                    WeekType weekType = WeekType.from(dayOfWeek);
                    DayType dayType = distinguishDayType(dayOfWeek);
                    Day day = Day.of(current, weekType, dayType);

                    int personIndex = (int) ChronoUnit.DAYS.between(startDate, current) % personList.size();
                    Person person = personList.get(personIndex);

                    return Duty.of(day, person);
                })
                .toList();

        return Duties.of(duties);
    }

    private DayType distinguishDayType(DayOfWeek dayOfWeek) {
        if (dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY) {
            return DayType.HOLIDAY;
        }
        return DayType.WEEKDAY;
    }
}
