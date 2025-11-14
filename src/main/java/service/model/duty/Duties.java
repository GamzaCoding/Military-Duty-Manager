package service.model.duty;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    public int size() {
        return duties.size();
    }

    public Duty findDuty(Duty targetDuty) {
        int index = duties.indexOf(targetDuty);
        return duties.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        duties.forEach(duty -> sb.append(duty.toString()));

        return sb.toString();
    }
}
