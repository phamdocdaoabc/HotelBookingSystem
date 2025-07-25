package com.hotel.service;

import com.hotel.dao.RoomDAO;
import com.hotel.model.enity.Room;

import java.util.List;

public class RoomService {
    private final RoomDAO roomDAO;

    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    // Add Room
    public void addRoom(Room room){
        try {
            if(room.getRoomNumber() == null || room.getRoomNumber().isEmpty()){
                throw new IllegalArgumentException("Số Phòng không được để trống");
            }
            if(room.getPriceNight()<0){
                throw new IllegalArgumentException("Giá phòng không nhỏ hơn 0");
            }
            // Check số phòng có tồn tại
            Room room1 = roomDAO.findRoomByNumber(room.getRoomNumber());
            if(room1 != null){
                throw new IllegalArgumentException("Phòng đã tồn tại, không thể lưu thêm");
            }
            // Call DAO
            roomDAO.addRoom(room);
            System.out.println("Thêm phòng thành công!");
        }catch (Exception e){
            System.out.println("Lỗi khi thêm phòng: " + e.getMessage());
        }
    }

    // Delete Room
    public void deleteRoom(String roomNumber){
        try{
            int result = roomDAO.deleteRoom(roomNumber);
            if (result > 0) {
                System.out.println("Xoá phòng thành công!");
            } else {
                System.out.println("Phòng không tồn tại.");
            }
        }catch (Exception e){
            System.out.println("Lỗi khi xóa phòng: " + e.getMessage());
        }
    }

    // Update Room
    public void updateRoom(Room room){
        try{
            int result = roomDAO.updateRoom(room);
            if (result > 0) {
                System.out.println("Cập nhật phòng thành công!");
            } else {
                System.out.println("Phòng không tồn tại.");
            }
        }catch (Exception e){
            System.out.println("Lỗi khi cập nhật phòng: " + e.getMessage());
        }
    }

    // Get all list Room
    public List<Room> getAllRooms(){
        try{
            var roomList = roomDAO.getAllRooms();
            return roomList;
        }catch (Exception e){
            System.out.println("Lỗi lấy list all: "+e.getMessage());
            return null;
        }
    }

    // Find Room
    public Room getRoomByNumber(String roomNumber) {
        try {
            if (roomNumber == null || roomNumber.isEmpty()) {
                throw new IllegalArgumentException("Số phòng không hợp lệ!");
            }

            Room room = roomDAO.findRoomByNumber(roomNumber);
            if (room == null) {
                System.out.println("Không tìm thấy phòng: " + roomNumber);
            }
            return room;
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm phòng: " + e.getMessage());
            return null;
        }
    }

    // Check
    public boolean checkOnRoom(String roomNumber) throws Exception {
        Room room = roomDAO.findRoomByNumber(roomNumber);
        if (room == null) throw new Exception("Phòng không tồn tại");
        if (room.isBooked()) throw new Exception("Phòng này đã có người đặt");
        roomDAO.updateRoomStatus(roomNumber, true);
        return true;
    }

    public boolean checkoutRoom(String roomNumber) throws Exception {
        Room room = roomDAO.findRoomByNumber(roomNumber);
        if (room == null) throw new Exception("Phòng không tồn tại");
        if (!room.isBooked()) throw new Exception("Phòng này chưa được đặt");
        roomDAO.updateRoomStatus(roomNumber, false);
        return true;
    }
}
