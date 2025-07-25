package com.hotel;

import com.hotel.dao.CustomerDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.model.enity.Customer;
import com.hotel.model.enity.Room;
import com.hotel.model.enums.PaymentStatus;
import com.hotel.service.CustomerService;
import com.hotel.service.RoomService;
import com.hotel.utils.*;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        // Kết nối DB
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Hệ thống sẵn sàng!");
        } else {
            System.out.println("Không thể kết nối Database!");
            return;
        }

        RoomDAO roomDAO = new RoomDAO(conn);
        RoomService roomService = new RoomService(roomDAO);
        CustomerDAO customerDAO = new CustomerDAO(conn);
        CustomerService customerService = new CustomerService(customerDAO, roomService);

        int choice;
        do {
            showMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: // Xem danh sách phòng
                        System.out.println("\n===== DANH SÁCH PHÒNG =====");

                    // In dòng tiêu đề
                        System.out.println("+------------+----------------+--------------+-------------+");
                        System.out.printf("| %-10s | %-14s | %-12s | %-11s |\n",
                                "Room No", "Type", "Status", "Price (VND)");
                        System.out.println("+------------+----------------+--------------+-------------+");

                    // In từng phòng
                        for (Room room : roomService.getAllRooms()) {
                            String status = room.isBooked() ? "Đã đặt" : "Chưa đặt";
                            String formattedPrice = String.format("%,d", (int) room.getPriceNight()); // định dạng có dấu phẩy
                            System.out.printf("| %-10s | %-14s | %-12s | %-11s |\n",
                                    room.getRoomNumber(),
                                    room.getType(),
                                    status,
                                    formattedPrice);
                        }

                        System.out.println("+------------+----------------+--------------+-------------+");

                        showExport();
                        int exportChoice = Integer.parseInt(scanner.nextLine());
                        if (exportChoice == 1) {
                            System.out.println("Export danh sách ra Excel...");
                            String folderPath = "src/file"; // Tạo folder trong src
                            File folder = new File(folderPath);
                            if (!folder.exists()) {
                                boolean created = folder.mkdirs(); // Tạo folder
                                if (created) {
                                    System.out.println("Tạo thư mục src/file thành công.");
                                } else {
                                    System.out.println("Không thể tạo thư mục src/file.");
                                }
                            }
                            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                            String fileName = "listAllRooms_" + timestamp + ".xlsx";
                            String filePath = folderPath + "/" + fileName;

                            List<Room> rooms = roomService.getAllRooms();
                            ExcelExporterRoom.exportRoomsToExcel(rooms, filePath);
                        }
                        break;

                    case 2: // CRUD phòng
                        showCrudRoom();
                        int crudChoice = Integer.parseInt(scanner.nextLine());
                        switch (crudChoice) {
                            case 1: // Thêm phòng
                                addRoom(scanner, roomService);
                                break;
                            case 2: // Xoá phòng
                                deleteRoom(scanner, roomService);
                                break;
                            case 3: // Cập nhật phòng
                                updateRoom(scanner, roomService);
                                break;
                            case 4: // Thoát CRUD
                                break;
                            default:
                                System.out.println("Lựa chọn không hợp lệ!");
                        }
                        break;

                    case 3: // Xem phòng trống
                        System.out.println("\n===== Danh sách phòng trống =====");
                        // In dòng tiêu đề
                        System.out.println("+------------+----------------+--------------+-------------+");
                        System.out.printf("| %-10s | %-14s | %-12s | %-11s |\n",
                                "Room No", "Type", "Status", "Price (VND)");
                        System.out.println("+------------+----------------+--------------+-------------+");

                        for (Room room : roomService.getAllRooms()) {
                            String status = room.isBooked() ? "Đã đặt" : "Chưa đặt";
                            String formattedPrice = String.format("%,d", (int) room.getPriceNight()); // định dạng có dấu phẩy
                            if (!room.isBooked()) {
                                System.out.printf("| %-10s | %-14s | %-12s | %-11s |\n",
                                        room.getRoomNumber(),
                                        room.getType(),
                                        status,
                                        formattedPrice);
                            }
                        }

                        System.out.println("+------------+----------------+--------------+-------------+");
                        showExport();
                        int emptyExportChoice = Integer.parseInt(scanner.nextLine());
                        if (emptyExportChoice == 1) {
                            System.out.println("Export danh sách phòng trống ra Excel...");

                            String folderPath = "src/file"; // Tạo folder trong src
                            File folder = new File(folderPath);
                            if (!folder.exists()) {
                                boolean created = folder.mkdirs(); // Tạo folder
                                if (created) {
                                    System.out.println("Tạo thư mục src/file thành công.");
                                } else {
                                    System.out.println("Không thể tạo thư mục src/file.");
                                }
                            }
                            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                            String fileName = "listEmptyRooms_" + timestamp + ".xlsx";
                            String filePath = folderPath + "/" + fileName;

                            List<Room> emptyRooms = new ArrayList<>();
                            for (Room room : roomService.getAllRooms()) {
                                if (!room.isBooked()) {
                                    emptyRooms.add(room);
                                }
                            }
                            ExcelExporterRoom.exportRoomsToExcel(emptyRooms,filePath);
                        }
                        break;

                    case 4: // Đặt phòng
                        System.out.println("Đặt phòng...");
                        bookingRoom(scanner, customerService);
                        break;

                    case 5: // Trả phòng
                        System.out.println("Trả phòng...");
                        checkoutRoom(scanner, customerService);
                        break;

                    case 6: // Thanh toán
                        System.out.println("Kiểm tra thanh toán...");
                        boolean result = checkPayments(scanner, customerService);
                        if(result){
                            acceptPayment();
                            int acceptPaymentChoice = Integer.parseInt(scanner.nextLine());
                            if (acceptPaymentChoice == 1) {
                                System.out.println("Thanh toán...");
                                payments(scanner, customerService);
                            }else {
                                break;
                            }
                        }
                        break;

                    case 7: // Import danh sách phòng từ Excel
                        System.out.println("Import danh sách phòng từ Excel...");
                        System.out.print("Nhập đường dẫn file excel: ");
                        String filePath1 = scanner.nextLine(); //"src/file/rooms.xlsx"
                        ExcelImporter importer = new ExcelImporter(roomService);
                        importer.importRoomsFromExcel(filePath1);
                        break;

                    case 8: // Xem danh sách khách hàng
                        System.out.println("\n===== Danh sách khách hàng =====");
                        System.out.println("+----+----------------------+--------------+------------+---------------------+------------+-------------------------+---------------------+---------------+------------------+");
                        System.out.printf("| %-2s | %-20s | %-12s | %-10s | %-19s | %-10s | %-23s | %-19s | %-13s | %-16s |\n",
                                "ID", "Name", "ID Card", "Room No", "Booking Date", "Phone", "Email", "Check Out", "Total (VND)", "Status");
                        System.out.println("+----+----------------------+--------------+------------+---------------------+------------+-------------------------+---------------------+---------------+------------------+");
                        for (Customer customer : customerService.getAllCustomer()) {
                            String checkOut = customer.getCheckOut() != null
                                    ? customer.getCheckOut().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                                    : "N/A";

                            String bookingDate = customer.getBookingDate() != null
                                    ? customer.getBookingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                                    : "N/A";

                            String formattedTotal = String.format("%,d", (int) customer.getTotalPayment());

                            System.out.printf("| %-2d | %-20s | %-12s | %-10s | %-19s | %-10s | %-23s | %-19s | %-13s | %-16s |\n",
                                    customer.getId(),
                                    customer.getName(),
                                    customer.getIdCard(),
                                    customer.getRoomNumber(),
                                    bookingDate,
                                    customer.getPhone(),
                                    customer.getEmail(),
                                    checkOut,
                                    formattedTotal,
                                    customer.getPaymentStatus());
                        }

                        System.out.println("+----+----------------------+--------------+------------+---------------------+------------+-------------------------+---------------------+---------------+------------------+");
                        showExport();
                        int customerExportChoice = Integer.parseInt(scanner.nextLine());
                        if (customerExportChoice == 1) {
                            System.out.println("Export danh sách khách hàng ra Excel...");
                            String folderPath = "src/file"; // Tạo folder trong src
                            File folder = new File(folderPath);
                            if (!folder.exists()) {
                                boolean created = folder.mkdirs(); // Tạo folder
                                if (created) {
                                    System.out.println("Tạo thư mục src/file thành công.");
                                } else {
                                    System.out.println("Không thể tạo thư mục src/file.");
                                }
                            }
                            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                            String fileName = "listAllCustomer_" + timestamp + ".xlsx";
                            String filePath = folderPath + "/" + fileName;

                            List<Customer> customers = customerService.getAllCustomer();
                            ExcelExporterCustomer.exportRoomsToExcel(customers, filePath);
                        }
                        break;
                    case 9:
                        // Lịch sử hoạt động
                        System.out.println("");
                        break;

                    case 10:
                        System.out.println("👋 Thoát chương trình. Cảm ơn bạn đã sử dụng!");
                        break;

                    default:
                        System.out.println("⚠️ Lựa chọn không hợp lệ. Vui lòng thử lại.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số hợp lệ.");
                choice = -1; // Để tiếp tục vòng lặp
            } catch (Exception e) {
                System.out.println("Lỗi hệ thống: " + e.getMessage());
                choice = -1;
            }

        } while (choice != 10);

        try {
            conn.close();
            System.out.println("Đã đóng kết nối Database.");
        } catch (Exception e) {
            System.out.println("Lỗi khi đóng kết nối DB: " + e.getMessage());
        }
    }

    private static void showMenu() {
        System.out.println("\n===== HỆ THỐNG ĐẶT PHÒNG KHÁCH SẠN =====");
        System.out.println("1. Xem danh sách phòng");
        System.out.println("2. CRUD phòng");
        System.out.println("3. Xem danh sách phòng trống");
        System.out.println("4. Đặt phòng");
        System.out.println("5. Trả phòng");
        System.out.println("6. Thanh Toán");
        System.out.println("7. Import danh sách phòng từ Excel");
        System.out.println("8. Xem danh sách khách hàng");
        System.out.println("9. Xem lịch sử hoạt động hệ thống");
        System.out.println("10. Thoát");
        System.out.print("Chọn chức năng: ");
    }

    private static void showExport(){
        System.out.println("1. Export danh sách ra Excel");
        System.out.println("2. Thoát ra option đầu");
        System.out.print("Chọn chức năng: ");
    }
     private static void showCrudRoom(){
         System.out.println("\n===== CRUD Phòng =====");
        System.out.println("1. Thêm phòng");
        System.out.println("2. Xóa phòng");
        System.out.println("3. Cập nhật phòng");
        System.out.println("4. Thoát");
        System.out.print("Chọn chức năng: ");
     }

    private static void addRoom(Scanner scanner, RoomService roomService) throws Exception {
        System.out.print("Nhập số phòng: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Nhập loại phòng: ");
        String type = scanner.nextLine();
        System.out.print("Phòng đã đặt chưa? (true/false): ");
        boolean isBooked = Boolean.parseBoolean(scanner.nextLine());
        System.out.print("Giá mỗi đêm: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Mô tả phòng: ");
        String description = scanner.nextLine();
        System.out.print("Tầng: ");
        int floor = Integer.parseInt(scanner.nextLine());

        Room room = new Room(roomNumber, type, isBooked, price, description, floor);
        roomService.addRoom(room);
    }

    private static void deleteRoom(Scanner scanner, RoomService roomService) throws Exception {
        System.out.print("Nhập số phòng cần xoá: ");
        String roomNumber = scanner.nextLine();
        roomService.deleteRoom(roomNumber);
    }

    private static void updateRoom(Scanner scanner, RoomService roomService) throws Exception {
        System.out.print("Nhập số phòng cần cập nhật: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Nhập loại phòng mới: ");
        String type = scanner.nextLine();
        System.out.print("Phòng đã đặt chưa? (true/false): ");
        boolean isBooked = Boolean.parseBoolean(scanner.nextLine());
        System.out.print("Giá mới mỗi đêm: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Mô tả mới: ");
        String description = scanner.nextLine();
        System.out.print("Tầng mới: ");
        int floor = Integer.parseInt(scanner.nextLine());

        Room room = new Room(roomNumber, type, isBooked, price, description, floor);
        roomService.updateRoom(room);
    }

    private static void bookingRoom(Scanner scanner, CustomerService customerService){
        System.out.print("Nhập Tên của bạn: ");
        String name = scanner.nextLine();
        System.out.print("Nhập CMND/CCCD của bạn(lớn hơn 9 số): ");
        String idCard= scanner.nextLine();
        System.out.print("Nhập phòng bạn muốn thuê: ");
        String roomNumber= scanner.nextLine();
        System.out.print("Nhập số điện thoại của bạn(10 số tối thiểu): ");
        String phone= scanner.nextLine();
        System.out.print("Nhập email của bạn: ");
        String email= scanner.nextLine();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setIdCard(idCard);
        customer.setRoomNumber(roomNumber);
        customer.setPhone(phone);
        customer.setEmail(email);
        customerService.addCustomer(customer);
    }
    private static void checkoutRoom(Scanner scanner, CustomerService customerService){
        // Nhập thông tin khách hàng
        System.out.print("Nhập tên khách hàng: ");
        String name = scanner.nextLine();
        System.out.print("Nhập CMND/CCCD: ");
        String idCard = scanner.nextLine();
        // Tìm khách hàng
        customerService.checkOutCustomer(idCard);
    }
    private static boolean checkPayments(Scanner scanner, CustomerService customerService){
        // Nhập thông tin khách hàng
        System.out.print("Nhập tên khách hàng: ");
        String name = scanner.nextLine();
        System.out.print("Nhập CMND/CCCD: ");
        String idCard = scanner.nextLine();
        Customer customer = customerService.checkPayment(idCard);
        if(customer != null){
            System.out.println("\n===== Thông tin bạn cần thanh toán: =====");
            System.out.println("\n" + customer.getId() + " | " + customer.getName() + " | " + customer.getIdCard() + " | "
                    + customer.getRoomNumber() + " | " + customer.getBookingDate() + " | " + customer.getPhone() + " | "
                    + customer.getEmail() + " | " + customer.getCheckOut() + " | " + customer.getTotalPayment() + " VND" + " | "
                    + customer.getPaymentStatus());
            return true;
        }
        return false;
    }
    private static void acceptPayment(){
        System.out.println("--------------------------");
        System.out.println("1. Tôi muốn thanh toán");
        System.out.println("2. Thoát ra option");
        System.out.print("Chọn chức năng: ");
    }

    private static void payments(Scanner scanner, CustomerService customerService){
        // Nhập thông tin thanh toán
        System.out.print("Nhập Id cần thanh toán: ");
        int id = scanner.nextInt();
        System.out.print("Nhập số tiền cần thanh toán: ");
        double totalPayment = scanner.nextDouble();
        Customer customer = customerService.findCustomaerById(id);
        if(customer != null){
            if(totalPayment == customer.getTotalPayment()){
                customer.setPaymentStatus(PaymentStatus.PAID);
                customerService.updateCustomer(customer);
                System.out.println("Thanh toán thành công");
                LoggerUtil.log("Thanh toán cho khách: "+customer.getName()+", phòng: "
                        +customer.getRoomNumber()+", ID: "+customer.getId()+ ", tổng tiền: "+customer.getTotalPayment());
            }else {
                System.out.println("Nhập tiền chưa đủ! Vui lòng thanh toán lại");
            }
        }
    }
}