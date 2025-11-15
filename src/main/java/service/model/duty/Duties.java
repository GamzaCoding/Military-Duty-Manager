package service.model.duty;

import java.util.List;

public class Duties {
    private final List<Duty> duties;

    private Duties(List<Duty> duties) {
        this.duties = duties;
    }

    // 이 메서드 없앨 생각도 해야한다.
    public static Duties of(List<Duty> duties) {
        return new Duties(duties);
    }

    public List<Duty> getDuties() {
        return List.copyOf(duties);
    }

    public int size() {
        return duties.size();
    }

    public Duty findDuty(Duty targetDuty) {
        int index = duties.indexOf(targetDuty);
        return duties.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        duties.forEach(duty -> sb.append(duty.toString()));
        return sb.toString();
    }
}
