package service.model.day;

public enum Week {
    Monday("월"),
    Tuesday("화"),
    Wednesday("수"),
    Thursday("목"),
    Friday("금"),
    Saturday("토"),
    Sunday("일");

    private final String weekName;

    Week(String weekName) {
        this.weekName = weekName;
    }

    public String getWeekName() {
        return weekName;
    }
}
