package repository.writer.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CellSizeSetter {
    public static final double CORRECTION_VALUE = 1.3;
    public static final int ROW_HEIGHT = 24;

    public static void applyBasicColumWidth(Sheet sheet, int columIndex) {
        sheet.autoSizeColumn(columIndex);
        int currentWidth = sheet.getColumnWidth(columIndex);
        sheet.setColumnWidth(columIndex, (int) (currentWidth * CORRECTION_VALUE));
    }

    public static void applyBasicRowHeight(Sheet sheet) {
        for (Row row : sheet) {
            row.setHeightInPoints(ROW_HEIGHT);
        }
    }
}
