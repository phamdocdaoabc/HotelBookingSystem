package com.hotel.model.enity;

import com.hotel.model.enums.PaymentStatus;

import java.time.LocalDateTime;

public class Customer {
    private int id;
    private String name; //Tên
    private String idCard; //CMND
    private String roomNumber; //Tên phòng
    private LocalDateTime bookingDate; // Ngày Đặt
    private String phone;
    private String email;
    private LocalDateTime CheckOut;
    private double totalPayment;
    private PaymentStatus paymentStatus;


    public Customer(int id, String name, String idCard, String roomNumber, LocalDateTime bookingDate, String phone, String email, LocalDateTime checkOut, Double totalPayment, PaymentStatus paymentStatus) {
        this.id = id;
        this.name = name;
        this.idCard = idCard;
        this.roomNumber = roomNumber;
        this.bookingDate = bookingDate;
        this.phone = phone;
        this.email = email;
        CheckOut = checkOut;
        this.totalPayment = totalPayment;
        this.paymentStatus = paymentStatus;
    }


    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        CheckOut = checkOut;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", bookingDate=" + bookingDate +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", CheckOut=" + CheckOut +
                ", totalPayment=" + totalPayment +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
