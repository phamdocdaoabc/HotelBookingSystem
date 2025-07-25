package com.hotel.utils;

import com.hotel.model.enity.Room;
import com.hotel.service.RoomService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class ExcelImporter {
    private RoomService roomService;

    public ExcelImporter(RoomService roomService) {
        this.roomService = roomService;
    }

    public void importRoomsFromExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua dòng header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                String roomNumber = row.getCell(0).getStringCellValue();
                String type = row.getCell(1).getStringCellValue();

                String bookedStatus = row.getCell(2).getStringCellValue().trim();
                boolean isBooked = bookedStatus.equals("Đã đặt");

                double pricePerNight = row.getCell(3).getNumericCellValue();
                String description = row.getCell(4).getStringCellValue();
                int floor = (int) row.getCell(5).getNumericCellValue();

                Room room = new Room(roomNumber, type, isBooked, pricePerNight, description, floor);

                // Add to DB
                roomService.addRoom(room);
                System.out.println("Imported room: " + roomNumber);
            }
            System.out.println("Import hoàn tất!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi import file Excel: " + e.getMessage());
        }
    }
}
