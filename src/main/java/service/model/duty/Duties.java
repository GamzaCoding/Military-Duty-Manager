package service.model.duty;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    // 시작 날짜, 종료 날짜를 입력하면 persons의 순서에 맞게 당직 객체를 생성해주는 서비스
    public static Duties makeDuties(LocalDate startDate, LocalDate endDate, Persons persons) {

        if (persons.isEmpty()) {
            throw new IllegalStateException("당직자 목록이 비어 있습니다.");
        }

        List<Duty> duties = Stream
                .iterate(startDate, current -> !current.isAfter(endDate), current -> current.plusDays(1))
                .map(current -> makeDuty(startDate, current, persons))
                .toList();

        return Duties.of(duties);
    }

    // 이거 duty안으로 옮겨볼까?
    private static Duty makeDuty(LocalDate startDate, LocalDate current, Persons persons) {
        Day day = Day.makeDay(current);
        int dutyOrderIndex = calculateDutyOrderIndex(startDate, current, persons);
        Person person = persons.getPerson(dutyOrderIndex);

        return Duty.of(day, person);
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
