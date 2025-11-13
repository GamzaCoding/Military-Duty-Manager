package service.model.person;

import java.util.Comparator;
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

    // 이거 반드시 수정해야 하는 로직임... 일단 이정도만 하자.
    public int indexOf(Person targetPerson) {
        if (persons.contains(targetPerson)) {
            return persons.indexOf(targetPerson);
        }
        return 0;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public Persons gerSortedPersons() {
        List<Person> sortedPersons = persons.stream()
                .sorted(Comparator.comparingInt(Person::position))
                .toList();
        return new Persons(sortedPersons);
    }
}
