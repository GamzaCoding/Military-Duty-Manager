package service.subService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import repository.reader.ExcelFileReader;
import repository.FileLocation.ResultFileLocation;
import service.model.person.Persons;

class FinalResultWriteServiceTest {

    @Test
    void 평일_휴일_당직순서_플러스_당직결과_한_엑셀에_출력() throws IOException {
        // given
        ExcelFileReader excelFileReader = new ExcelFileReader();
        String filePath = "/Users/seok/openMission/당직자 순서(입력 데이터).xlsx";
        File file = new File(filePath);
        Persons holidayPersons = excelFileReader.readHolidayPersons(file);
        Persons weekdayPersons = excelFileReader.readWeekdayPersons(file);

        LocalDate startDate = LocalDate.of(2025, 11, 11); // 외부 입력 값
        LocalDate endDate = LocalDate.of(2026, 1, 30); // 외부 입력 값


        ResultFileLocation resultFileLocation = new ResultFileLocation();
        File location = resultFileLocation.getLocation();

        // when
        FinalResultService finalResultWriterService = new FinalResultService(location);
        finalResultWriterService.writeFinalResult(startDate, endDate, weekdayPersons, holidayPersons);
        // then
    }

}