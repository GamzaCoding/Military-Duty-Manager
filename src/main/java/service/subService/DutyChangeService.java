package service.subService;

import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;

public class DutyChangeService {

    public DutyChangeService() {
    }

    public Duties changeDutyBothSides(Duties targetDuties, Duty dutyTo, Duty dutyFrom) {
        validateSameDayType(dutyTo, dutyFrom);

        Duty changedDutyTo = Duty.of(dutyTo.getDay(), dutyFrom.getPerson());
        Duty changedDutyFrom = Duty.of(dutyFrom.getDay(), dutyTo.getPerson());

        targetDuties.removeDuty(dutyTo);
        targetDuties.removeDuty(dutyFrom);
        targetDuties.addDuty(changedDutyTo);
        targetDuties.addDuty(changedDutyFrom);

        return targetDuties;
    }

    public Duties changeDutyOneSide(Duties targetDuties, Duty duty, Person person) {
        Duty changedDuty = Duty.of(duty.getDay(), person);
        targetDuties.removeDuty(duty);
        targetDuties.addDuty(changedDuty);

        return targetDuties;
    }

    private static void validateSameDayType(Duty dutyTo, Duty dutyFrom) {
        if (dutyTo.isSameDayType(dutyFrom)) {
            return;
        }
        throw new IllegalStateException("평일은 평일끼리, 휴일은 휴일끼리 교환이 가능합니다.");
    }
}
