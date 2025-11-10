package service.model;

import java.time.LocalDate;

public class Person {
    private final Integer position;
    private final String rank;
    private final String name;
    private final LocalDate moveInDate;
    private final LocalDate moveOutDate;

    private Person(Integer position, String rank, String name, LocalDate moveInDate, LocalDate moveOutDate) {
        this.position = position;
        this.rank = rank;
        this.name = name;
        this.moveInDate = moveInDate;
        this.moveOutDate = moveOutDate;
    }

    public static Person from(Integer position, String rank, String name, LocalDate moveInDate, LocalDate moveOutDate) {
        return new Person(position, rank, name, moveInDate, moveOutDate);
    }

    public String getName() { return name; }
    public LocalDate getJoinDate() { return moveInDate; }
    public LocalDate getLeaveDate() { return moveOutDate; }
    public Integer getPosition() { return position; }

    @Override
    public String toString() {
        return String.format("순위: %d, 계급: %s, 이름: %s, 전입일: %s, 전출일: %s", position, rank, name, moveInDate, moveOutDate);
    }
}
