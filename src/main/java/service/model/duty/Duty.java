package service.model.duty;

import service.model.day.Day;
import service.model.person.Person;

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

    @Override
    public String toString() {
        return "Duty{" +
                "day=" + day +
                ", person=" + person +
                '}';
    }
}
