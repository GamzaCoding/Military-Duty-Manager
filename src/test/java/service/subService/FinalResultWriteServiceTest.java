package service.subService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.example.dutymanager.repository.reader.ExcelFileReader;
import org.example.dutymanager.service.MainService;
import org.junit.jupiter.api.Test;

class FinalResultWriteServiceTest {

    @Test
    void 평일_휴일_당직순서_플러스_당직결과_한_엑셀에_출력() throws IOException {
        // given
        ExcelFileReader excelFileReader = new ExcelFileReader();
        String filePath = "/Users/seok/Desktop/당직자 순서(입력 데이터).xlsx";
        File inputFile = new File(filePath);

        LocalDate startDate = LocalDate.of(2025, 11, 11); // 외부 입력 값
        LocalDate endDate = LocalDate.of(2026, 1, 30); // 외부 입력 값

        // when
        MainService mainService = new MainService();
        mainService.calculateResult(startDate, endDate, inputFile);
        // then
    }
}
