package repository.reader;

import java.io.File;
import org.example.dutymanager.repository.reader.ExcelFileReader;
import org.example.dutymanager.service.model.duty.Duties;
import org.junit.jupiter.api.Test;

class ExcelFileReaderTest {

    @Test
    void 최종_당직_결과_읽어오기() {
        ExcelFileReader excelFileReader = new ExcelFileReader();
        File file = new File("/Users/seok/Desktop/당직결과/2025년/11월/당직표_2025년_11월_17일_19시_22분.xlsx");
        Duties duties = excelFileReader.readReulstDuties(file);
        duties.getDuties().forEach(System.out::println);
    }
}