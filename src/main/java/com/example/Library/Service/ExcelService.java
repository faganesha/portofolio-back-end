package com.example.Library.Service;

import com.example.Library.Mapper.Response.BorrowmentResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private BorrowmentService borrowmentService;

    @Value("${excel.path}")
    private String path;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private XSSFCellStyle borderstyle;

    public ExcelService() {
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Borrowment");
        Row row = sheet.createRow(2);
        Row tittleRow = sheet.createRow(0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:E1"));

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setBold(true);
        font.setFontHeight(16);
        font.setColor(IndexedColors.WHITE.index);

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);

        style.setAlignment(HorizontalAlignment.CENTER);

        style.setFont(font);
        style.setBorderBottom(borderstyle.BORDER_MEDIUM);
        style.setBorderTop(borderstyle.BORDER_MEDIUM);
        style.setBorderRight(borderstyle.BORDER_MEDIUM);
        style.setBorderLeft(borderstyle.BORDER_MEDIUM);

        createCell(tittleRow, 0, "Borrowment List", style);
        createCell(row, 0, "Member", style);
        createCell(row, 1, "Book", style);
        createCell(row, 2, "Borrowed Time", style);
        createCell(row, 3, "Returned Time", style);
        createCell(row, 4, "Status", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Date) {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(Date from, Date until) {
        int rowCount = 3;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        style.setBorderBottom(borderstyle.BORDER_MEDIUM);
        style.setBorderTop(borderstyle.BORDER_MEDIUM);
        style.setBorderRight(borderstyle.BORDER_MEDIUM);
        style.setBorderLeft(borderstyle.BORDER_MEDIUM);

        List<BorrowmentResponse> borrowmentList = borrowmentService.showAll(from, until);

        for (BorrowmentResponse borrowment : borrowmentList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, borrowment.getMember().getName(), style);
            createCell(row, columnCount++, borrowment.getBook().getTittle(), style);
            createCell(row, columnCount++, borrowment.getCreatedAt(), style);
            createCell(row, columnCount++, borrowment.getDueDate(), style);
            createCell(row, columnCount++, borrowment.getReturnFlag(), style);
        }
    }

    public void export(Date from, Date until) throws IOException {
        writeHeaderLine();
        writeDataLines(from, until);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        File currDir = new File(path);
        System.out.println(path);
        String path = currDir.getAbsolutePath();
        String fileLocation = path + " Borrowment" + timestamp.getTime() + ".xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }
}
