package controller;

import java.io.File;
import java.util.List;
import repository.ExcelFileReader;
import service.model.Person;

public class TestController {
    public static void main(String[] args) {

        ExcelFileReader excelFileReader = new ExcelFileReader();
        String filePath = "/Users/seok/openMission/당직자 순서(입력 데이터).xlsx";
        File file = new File(filePath);
        List<Person> soldiers = excelFileReader.readPersons(file);

        soldiers.forEach(System.out::println);
    }
}
