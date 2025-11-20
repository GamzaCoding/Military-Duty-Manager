package org.example.dutymanager.service.model.duty;

import java.util.Objects;
import org.example.dutymanager.service.model.day.Day;
import org.example.dutymanager.service.model.person.Person;


public class Duty {
    private final Day day;
    private final Person person;

    private Duty(Day day, Person person) {
        this.day = day;
        this.person = person;
    }

    public static Duty of(Day day, Person person) {
        return new Duty(day, person);
    }

    public Day getDay() {
        return day;
    }

    public Person getPerson() {
        return person;
    }

    public boolean isSameDayType(Duty duty) {
        return day.isSameDayType(duty.getDay());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Duty duty = (Duty) o;
        return Objects.equals(day, duty.day) && Objects.equals(person, duty.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, person);
    }

    @Override
    public String toString() {
        return "Duty{" +
                "day=" + day +
                ", person=" + person +
                '}';
    }
}
