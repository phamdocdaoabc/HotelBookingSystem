package com.hotel.model.enity;

public class Room {
    private String roomNumber; // Tên phòng
    private String type; // Đơn, Đôi, VIP
    private boolean isBooked; // Trạng thái phòng
    private double priceNight; // giá phòng qua đêm
    private String description; // Note
    private int floor; // Tầng số


    public Room(String roomNumber, String type, boolean isBooked, double priceNight, String description, int floor) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.isBooked = isBooked;
        this.priceNight = priceNight;
        this.description = description;
        this.floor = floor;
    }

    public Room() {
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public double getPriceNight() {
        return priceNight;
    }

    public void setPriceNight(double priceNight) {
        this.priceNight = priceNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", type='" + type + '\'' +
                ", isBooked=" + isBooked +
                ", priceNight=" + priceNight +
                ", description='" + description + '\'' +
                ", floor=" + floor +
                '}';
    }
}
