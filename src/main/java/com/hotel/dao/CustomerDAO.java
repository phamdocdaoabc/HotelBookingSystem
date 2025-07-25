package com.hotel.dao;

import com.hotel.model.enity.Customer;
import com.hotel.model.enity.Room;
import com.hotel.model.enums.PaymentStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private final Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    // Add customer
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (name, id_card, room_number, booking_date, phone_number, email, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getIdCard());
            stmt.setString(3, customer.getRoomNumber());
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(5, customer.getPhone());
            stmt.setString(6, customer.getEmail());
            stmt.setString(7, customer.getPaymentStatus().name());
            stmt.executeUpdate();
        }
    }

    public Customer findCustomerByIdCard(String idCard) throws SQLException{
        String sql = "SELECT * FROM customers WHERE id_card = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idCard);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Mapping dữ liệu từ ResultSet sang Customer
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setIdCard(rs.getString("id_card"));
                customer.setRoomNumber(rs.getString("room_number"));
                customer.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                customer.setPhone(rs.getString("phone_number"));
                customer.setEmail(rs.getString("email"));

                Timestamp checkOutTimestamp = rs.getTimestamp("check_out_date");
                LocalDateTime checkOutDate = null;
                if (checkOutTimestamp != null) {
                    checkOutDate = checkOutTimestamp.toLocalDateTime();
                }

                customer.setCheckOut(checkOutDate);
                customer.setTotalPayment(rs.getDouble("total_payment"));
                customer.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
                return customer;
            } else {
                return null; // Không tìm thấy khách hàng
            }
        }
    }

    public Customer findCustomerById(int id) throws SQLException{
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Mapping dữ liệu từ ResultSet sang Customer
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setIdCard(rs.getString("id_card"));
                customer.setRoomNumber(rs.getString("room_number"));
                customer.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                customer.setPhone(rs.getString("phone_number"));
                customer.setEmail(rs.getString("email"));

                Timestamp checkOutTimestamp = rs.getTimestamp("check_out_date");
                LocalDateTime checkOutDate = null;
                if (checkOutTimestamp != null) {
                    checkOutDate = checkOutTimestamp.toLocalDateTime();
                }

                customer.setCheckOut(checkOutDate);
                customer.setTotalPayment(rs.getDouble("total_payment"));
                customer.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
                return customer;
            } else {
                return null; // Không tìm thấy khách hàng
            }
        }
    }

    // Update Customer
    public int updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET name=?, id_card=?, room_number=?, booking_date=?, phone_number=?, email=?, check_out_date=?, total_payment=?, payment_status=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getIdCard());
            stmt.setString(3, customer.getRoomNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(customer.getBookingDate()));
            stmt.setString(5, customer.getPhone());
            stmt.setString(6, customer.getEmail());
            stmt.setTimestamp(7, Timestamp.valueOf(customer.getCheckOut()));
            stmt.setDouble(8, customer.getTotalPayment());
            stmt.setString(9, customer.getPaymentStatus().name());
            stmt.setInt(10, customer.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected;
        }
    }

    // Get All Customer
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Lấy dữ liệu từ ResultSet
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String idCard = rs.getString("id_card");
                String roomNumber = rs.getString("room_number");

                // booking_date (DATETIME → LocalDateTime)
                LocalDateTime bookingDate = rs.getTimestamp("booking_date").toLocalDateTime();

                String phoneNumber = rs.getString("phone_number");
                String email = rs.getString("email");

                // check_out_date có thể NULL
                Timestamp checkOutTimestamp = rs.getTimestamp("check_out_date");
                LocalDateTime checkOutDate = null;
                if (checkOutTimestamp != null) {
                    checkOutDate = checkOutTimestamp.toLocalDateTime();
                }

                double totalPayment = rs.getDouble("total_payment");

                // payment_status: String → Enum
                String paymentStatusStr = rs.getString("payment_status");
                PaymentStatus paymentStatus = PaymentStatus.valueOf(paymentStatusStr);

                // Tạo Customer object
                Customer customer = new Customer(
                        id, name, idCard, roomNumber,
                        bookingDate, phoneNumber, email,
                        checkOutDate, totalPayment, paymentStatus
                );
                customers.add(customer);
            }
        }
        return customers;
    }
}
