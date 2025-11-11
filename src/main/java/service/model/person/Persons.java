package service.model.person;

import java.util.List;

public class Persons {
    private final List<Person> persons;

    private Persons(List<Person> persons) {
        this.persons = persons;
    }

    public static Persons of(List<Person> persons) {
        return new Persons(persons);
    }

    public Person getPerson(int index) {
        return persons.get(index);
    }

    public int size() {
        return persons.size();
    }

    public boolean isEmpty() {
        return persons.isEmpty();
    }
}
