package repository.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.model.Duty;
import service.model.Person;
import service.model.day.Day;

public class DutyResultWriter implements ExcelFileWriter {
    private final List<Duty> duties;

    private DutyResultWriter(List<Duty> duties) {
        this.duties = duties;
    }

    public static DutyResultWriter of(List<Duty> duties) {
        return new DutyResultWriter(duties);
    }

    @Override
    public void write(File outputFile) {
        handleIOExceptionDuringWrite(outputFile, this::writeDutiesToExcel);
    }

    private void writeDutiesToExcel(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("당직표 결과");

            // 스타일 정의
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle centerStyle = createCenterStyle(workbook);
            CellStyle weekendStyle = createWeekendStyle(workbook);

            int maxCount = Math.min(duties.size(), 30); // 최대 30개까지만 출력
            int rowIndex = 0;

            // duties를 7개씩 잘라서 처리
            List<SevenDuties> weeklyGroups = groupByWeek(duties.subList(0, maxCount));

            for (SevenDuties sevenDuties : weeklyGroups) {
                // 날짜(요일) 행
                Row dateRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < sevenDuties.size(); i++) {
                    Duty duty = sevenDuties.get(i);
                    Cell cell = dateRow.createCell(i);
                    Day day = duty.getDay();
                    cell.setCellValue(day.toString());

                    // 평일, 휴일 색상 구분
                    if (day.isHoliDay()) {
                        cell.setCellStyle(weekendStyle);
                    } else {
                        cell.setCellStyle(headerStyle);
                    }
                }

                // 이름(계급 + 이름) 행
                Row personRow = sheet.createRow(rowIndex);
                for (int i = 0; i < sevenDuties.size(); i++) {
                    Duty duty = sevenDuties.get(i);
                    Cell cell = personRow.createCell(i);
                    Person person = duty.getPerson();
                    cell.setCellValue(person.getRankAndName());
                    cell.setCellStyle(centerStyle);
                }

                // 한 주 단위로 한 줄 띄우기 (가독성)
                rowIndex++;
            }

            // 셀 크기 보정
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, (int) (currentWidth * 1.3)); // 한글 보정 (30% 여유)
            }

            // 셀 높이 보정
            for(Row row : sheet) {
                row.setHeightInPoints(40);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    // 헤더용 스타일 (파란색 배경 + 굵은 글씨 + 중앙 정렬)
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setThinBorder(style);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    // 일반 중앙정렬 스타일 (테두리만 있음)
    private CellStyle createCenterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setThinBorder(style);
        return style;
    }

    // 주말(토, 일) 스타일 — 붉은 톤 배경
    private CellStyle createWeekendStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setThinBorder(style);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    // 테두리 설정 공통 함수
    private void setThinBorder(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    // duties를 7개씩 잘라서 주차별 그룹으로 나눈다.
    private List<SevenDuties> groupByWeek(List<Duty> list) {
        List<SevenDuties> groups = new java.util.ArrayList<>();
        for (int i = 0; i < list.size(); i += 7) {
            int end = Math.min(i + 7, list.size());
            groups.add(new SevenDuties(list.subList(i, end)));
        }
        return groups;
    }

    private <T> void handleIOExceptionDuringWrite(T input, IOFunctionForWrite<T> ioFunction) {
        try {
            ioFunction.accept(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일에 값을 입력하는 도중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private record SevenDuties(List<Duty> duties) {
        public Duty get(int index) {
                return duties.get(index);
            }
        public int size() {
                return duties.size();
            }
        }
}
