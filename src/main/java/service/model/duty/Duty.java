package service.model.duty;

import java.util.Objects;
import service.model.day.Day;
import service.model.person.Person;

public class Duty {
    private final Day day;
    private Person person;

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

    public void setPerson(Person other) {
        person = other;
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
