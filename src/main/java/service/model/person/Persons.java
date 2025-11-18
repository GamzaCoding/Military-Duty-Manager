package service.model.person;

import java.util.Comparator;
import java.util.List;

public class Persons {
    private final List<Person> persons;

    private Persons(List<Person> persons) {
        this.persons = List.copyOf(persons);
    }

    public static Persons of(List<Person> persons) {
        return new Persons(persons);
    }

    public int size() {
        return persons.size();
    }

    public boolean isEmpty() {
        return persons.isEmpty();
    }

    public Person getPerson(int index) {
        return persons.get(index);
    }

    public List<Person> getPersons() {
        return List.copyOf(persons);
    }

    public Persons gerSortedPersons() {
        List<Person> sortedPersons = persons.stream()
                .sorted(Comparator.comparingInt(Person::order))
                .toList();
        return new Persons(sortedPersons);
    }
}
