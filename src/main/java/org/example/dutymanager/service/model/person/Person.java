package org.example.dutymanager.service.model.person;

import java.time.LocalDate;
import java.util.Objects;

public class Person {
    private final Integer order;
    private final String rank;
    private final String name;
    private final LocalDate moveInDate;
    private final LocalDate moveOutDate;

    private Person(Integer order, String rank, String name, LocalDate moveInDate, LocalDate moveOutDate) {
        this.order = order;
        this.rank = rank;
        this.name = name;
        this.moveInDate = moveInDate;
        this.moveOutDate = moveOutDate;
    }

    public static Person from(Integer order, String rank, String name, LocalDate moveInDate, LocalDate moveOutDate) {
        return new Person(order, rank, name, moveInDate, moveOutDate);
    }

    public Integer order() {
        return order;
    }

    public String rank() {
        return rank;
    }

    public String name() {
        return name;
    }

    public LocalDate moveInDate() {
        return moveInDate;
    }

    public LocalDate moveOutDate() {
        return moveOutDate;
    }

    public String getRankAndName() {
        return String.format("%s %s", rank, name);
    }

    @Override
    public String toString() {
        return String.format("순위: %d, 계급: %s, 이름: %s, 전입일: %s, 전출일: %s", order, rank, name, moveInDate, moveOutDate);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(rank, person.rank)
                && Objects.equals(name, person.name)
                && Objects.equals(moveInDate, person.moveInDate)
                && Objects.equals(moveOutDate, person.moveOutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, name, moveInDate, moveOutDate);
    }
}
