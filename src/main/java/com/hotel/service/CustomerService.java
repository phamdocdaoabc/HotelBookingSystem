package com.hotel.service;

import com.hotel.dao.CustomerDAO;

import com.hotel.model.enity.Customer;

import com.hotel.model.enity.Room;
import com.hotel.model.enums.PaymentStatus;
import com.hotel.utils.LoggerUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class CustomerService {
    private final CustomerDAO customerDAO;

    private final RoomService roomService;


    public CustomerService(CustomerDAO customerDAO, RoomService roomService) {
        this.customerDAO = customerDAO;
        this.roomService = roomService;
    }

    // add customer and check in room
    public void addCustomer(Customer customer){
        try {
            if(customer.getName() == null || customer.getName().isEmpty()){
                throw new IllegalArgumentException("Vui lòng điền tên để đặt phòng!");
            }
            if(customer.getIdCard() == null || customer.getIdCard().isEmpty()){
                throw new IllegalArgumentException("Số CMND/CCCD không được để trống");
            }
            if(customer.getIdCard().length() < 9 ){
                throw new IllegalArgumentException("Số CMND/CCCD không được nhỏ hơn 9 số");
            }
            if(customer.getPhone() == null || customer.getPhone().isEmpty()){
                throw new IllegalArgumentException("Số điện thoại không được để trống");
            }
            if(customer.getPhone().length() < 10 ){
                throw new IllegalArgumentException("Số ĐT không được nhỏ hơn 10 số");
            }
            if(customer.getRoomNumber() == null || customer.getRoomNumber().isEmpty()){
                throw new IllegalArgumentException("Số phòng muốn đặt không được để trống");
            }

            // Set trạng thái mặc định
            customer.setBookingDate(LocalDateTime.now()); // Auto set ngày đặt
            customer.setPaymentStatus(PaymentStatus.UNPAID);

            // Check số phòng có tồn tại
            boolean result = roomService.checkOnRoom(customer.getRoomNumber());
            if(result){
                // Call DAO
                customerDAO.addCustomer(customer);
                System.out.println("Đặt phòng thành công!");
                // Log file
                LoggerUtil.log("Đặt phòng: " +customer.getRoomNumber() + ", khách hàng: " +customer.getName() +", CMND/CCCD: "+customer.getIdCard());
            }
        }catch (Exception e){
            System.out.println("Lỗi khi đặt phòng: " + e.getMessage());
        }
    }

    // update Customer
    public void updateCustomer(Customer customer){
        try{
            int result = customerDAO.updateCustomer(customer);
            if (result > 0) {
                System.out.println("Cập nhật thông tin thành công!");
            } else {
                System.out.println("Khách hàng không tồn tại.");
            }
        }catch (Exception e){
            System.out.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
        }
    }

    // Check out
    public void checkOutCustomer(String idCard){
        try{
            Customer customer = customerDAO.findCustomerByIdCard(idCard);
            if(customer == null){
                throw new NullPointerException("Không tìm thấy khách hàng tương ứng!");
            }
            Room room = roomService.getRoomByNumber(customer.getRoomNumber());
            if(customer.getCheckOut() != null && customer.getTotalPayment() != null){
                throw new IllegalStateException("Khách hàng không có phòng nào chưa trả!");
            }
            customer.setCheckOut(LocalDateTime.now());
            long days = ChronoUnit.DAYS.between(customer.getBookingDate(), customer.getCheckOut());
            days = Math.max(1, days);
            System.out.println("Số ngày đã ở: "+days);
            customer.setTotalPayment(room.getPriceNight()*days);
            // update
            updateCustomer(customer);
            room.setBooked(false);
            roomService.updateRoom(room);
            System.out.println("Trả phòng thành công. Vui lòng thanh toán sớm nhất có thể.");
            // Log file
            LoggerUtil.log("Trả phòng: " + customer.getRoomNumber() +", ID: " +customer.getId() +
                    ", tổng ngày ở: " +days+ ", Tổng tiền: "+customer.getTotalPayment());
        }catch (Exception e){
            System.out.println("Lỗi khi check out customer: "+e);
        }
    }

    // Get all list customer
    public List<Customer> getAllCustomer(){
        try{
            var customerList = customerDAO.getAllCustomers();
            return customerList;
        }catch (Exception e){
            System.out.println("Lỗi lấy list all: "+e.getMessage());
            return null;
        }
    }

    // Check customer payment
    public Customer checkPayment(String idCard){
        try{
            Customer customer = customerDAO.findCustomerByIdCard(idCard);
            if(customer == null){
                throw new NullPointerException("Không tìm thấy khách hàng tương ứng!");
            }
            if(customer.getPaymentStatus() == PaymentStatus.PAID){
                throw new IllegalArgumentException("Khách hàng không có phòng nào chưa thanh toán!");
            }
            return customer;
        }catch (Exception e){
            System.out.println("Lỗi khi check customer payment: "+e);
            return  null;
        }
    }

    // findCustomaerById
    public Customer findCustomaerById(int id){
        try{
            Customer customer = customerDAO.findCustomerById(id);
            if(customer == null){
                throw new NullPointerException("Không tìm thấy khách hàng tương ứng!");
            }
            return customer;
        }catch (Exception e){
            System.out.println("Lỗi tìm khách hàng id: "+e);
            return null;
        }
    }

}
