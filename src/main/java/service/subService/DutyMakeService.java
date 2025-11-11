package service.subService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;
import service.model.day.Day;
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

        if (persons.isEmpty()) {
            throw new IllegalStateException("당직자 목록이 비어 있습니다.");
        }

        List<Duty> duties = Stream
                .iterate(startDate, current -> !current.isAfter(endDate), current -> current.plusDays(1))
                .map(current -> makeDuty(startDate, current, persons))
                .toList();

        return Duties.of(duties);
    }

    private Duty makeDuty(LocalDate startDate, LocalDate current, Persons persons) {
        Day day = Day.makeDay(current);
        int dutyOrderIndex = calculateDutyOrderIndex(startDate, current, persons);
        Person person = persons.getPerson(dutyOrderIndex);

        return Duty.of(day, person);
    }

    private int calculateDutyOrderIndex(LocalDate startDate, LocalDate current, Persons persons) {
        return (int) ChronoUnit.DAYS.between(startDate, current) % persons.size();
    }
}
