package service.subService;

import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import service.model.day.Day;
import service.model.day.Days;

class DutyDayServiceTest {

    @Test
    void 공휴일을_고려한_당직일_휴무일_확인() throws IOException {
        // given
        DutyDayService dutyDayService = new DutyDayService();
        LocalDate startDate = LocalDate.of(2025, 8, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 27);

        // when
        Days days = dutyDayService.makeDutyDays(startDate, endDate);

        // then

        days.getDays()
                .stream().filter(Day::isHoliday)
                .forEach(System.out::println);
    }
}