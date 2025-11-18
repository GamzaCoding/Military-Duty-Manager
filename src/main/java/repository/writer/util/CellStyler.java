package repository.writer.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyler {

    private final Workbook workbook;

    public CellStyler(Workbook workbook) {
        this.workbook = workbook;
    }

    public CellStyle holidayHeaderStyle() {
        return style()
                .background(IndexedColors.LIGHT_CORNFLOWER_BLUE)
                .alignCenter()
                .borderThin()
                .fontBold(16, "굴림")
                .build();
    }

    public CellStyle holidayBodyStyle() {
        return style()
                .alignCenter()
                .borderThin()
                .fontBold(15, "굴림")
                .build();
    }

    private CellStyleBuilder style() {
        return new CellStyleBuilder(workbook);
    }

    private static class CellStyleBuilder {
        private final CellStyle style;
        private final Font font;

        CellStyleBuilder(Workbook workbook) {
            this.style = workbook.createCellStyle();
            this.font = workbook.createFont();
        }

        CellStyleBuilder background(IndexedColors color) {
            style.setFillForegroundColor(color.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            return this;
        }

        CellStyleBuilder alignCenter() {
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            return this;
        }

        CellStyleBuilder borderThin() {
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            return this;
        }

        CellStyleBuilder fontBold(int point, String fontName) {
            font.setBold(true);
            font.setFontHeightInPoints((short) point);
            font.setFontName(fontName);
            return this;
        }

        CellStyle build() {
            style.setFont(font);
            return style;
        }
    }
}
