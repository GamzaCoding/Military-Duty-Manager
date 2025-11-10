package service.model;

import service.model.day.Day;

public class Duty {
    private final Day day;
    private final Person person;

    public Duty(Day day, Person person) {
        this.day = day;
        this.person = person;
    }

    public Day getDay() {
        return day;
    }

    public Person getPerson() {
        return person;
    }
}
