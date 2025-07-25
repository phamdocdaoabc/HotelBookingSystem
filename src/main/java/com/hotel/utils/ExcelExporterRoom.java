package com.hotel.utils;

import com.hotel.model.enity.Room;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporterRoom {
    public static void exportRoomsToExcel(List<Room> rooms, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Rooms");

        // Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Room Number", "Type", "Is Booked", "Price Per Night", "Description", "Floor"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data rows
        int rowNum = 1;
        for (Room room : rooms) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(room.getRoomNumber());
            row.createCell(1).setCellValue(room.getType());
            row.createCell(2).setCellValue(room.isBooked() ? "Đã đặt" : "Chưa đặt");
            row.createCell(3).setCellValue(room.getPriceNight());
            row.createCell(4).setCellValue(room.getDescription());
            row.createCell(5).setCellValue(room.getFloor());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            System.out.println("Xuất danh sách phòng ra Excel thành công: " + filePath);
        } catch (IOException e) {
            System.out.println("Lỗi khi xuất file Excel: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                // Ignore
                System.out.println("Lỗi: "+e);
            }
        }
    }
}
