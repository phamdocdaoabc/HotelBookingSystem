package com.hotel.utils;

import com.hotel.model.enity.Customer;
import com.hotel.model.enity.Room;
import com.hotel.model.enums.PaymentStatus;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporterCustomer {
    public static void exportRoomsToExcel(List<Customer> customers, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customers");

        // Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Họ Tên", "CCCD/CMND", "Số Phòng", "Ngày Đặt", "Điện Thoại", "Email", "Ngày Trả", "Tổng Tiền", "Thanh Toán"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data rows
        int rowNum = 1;
        for (Customer customer : customers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(customer.getId());
            row.createCell(1).setCellValue(customer.getName());
            row.createCell(2).setCellValue(customer.getIdCard());
            row.createCell(3).setCellValue(customer.getRoomNumber());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = customer.getBookingDate().format(formatter);
            row.createCell(4).setCellValue(formattedDate);

            row.createCell(5).setCellValue(customer.getPhone());
            row.createCell(6).setCellValue(customer.getEmail());

            if (customer.getCheckOut() != null) {
                String formattedCheckOut = customer.getCheckOut().format(formatter);
                row.createCell(7).setCellValue(formattedCheckOut);
            } else {
                row.createCell(7).setCellValue("null");
            }

            row.createCell(8).setCellValue(customer.getTotalPayment());
            row.createCell(9).setCellValue((customer.getPaymentStatus() == PaymentStatus.PAID ? "Đã thanh toán" : "Chưa thanh toán"));
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
