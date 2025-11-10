package service.model.duty;

import java.util.List;

public class Duties {
    private final List<Duty> duties;

    private Duties(List<Duty> duties) {
        this.duties = duties;
    }

    public static Duties of(List<Duty> duties) {
         return new Duties(duties);
    }

    public List<Duty> getDuties() {
        return duties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        duties.forEach(duty -> sb.append(duty.toString()));

        return sb.toString();
    }
}
