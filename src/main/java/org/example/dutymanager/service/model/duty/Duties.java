package org.example.dutymanager.service.model.duty;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.example.dutymanager.service.model.day.Day;
import org.example.dutymanager.service.model.person.Person;

public class Duties {
    private final Map<Day, Person> duties;

    private Duties(List<Duty> duties) {
        Map<Day, Person> result = new TreeMap<>();
        duties.forEach(duty -> result.put(duty.getDay(), duty.getPerson()));
        this.duties = result;
    }

    public static Duties of(List<Duty> duties) {
        return new Duties(duties);
    }

    public void removeDuty(Duty duty) {
        duties.remove(duty.getDay());
    }

    public void addDuty(Duty duty) {
        duties.put(duty.getDay(), duty.getPerson());
    }

    public List<Duty> getDuties() {
        return duties.entrySet().stream()
                .map(entry -> Duty.of(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<Duty> subList(int startIndex, int endIndex) {
        return getDuties().subList(startIndex, endIndex);
    }

    public int size() {
        return duties.size();
    }
}
