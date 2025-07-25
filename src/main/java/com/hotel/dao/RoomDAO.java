package com.hotel.dao;

import com.hotel.model.enity.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private final Connection connection;

    public RoomDAO(Connection connection) {
        this.connection = connection;
    }

    // Add Room
    public void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, type, is_booked, price_per_night, description, floor) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getType());
            stmt.setBoolean(3, room.isBooked());
            stmt.setDouble(4, room.getPriceNight());
            stmt.setString(5, room.getDescription());
            stmt.setInt(6, room.getFloor());
            stmt.executeUpdate();
        }
    }


    // Delete Room
    public int deleteRoom(String roomNumber) throws SQLException {
        String sql = "DELETE FROM rooms WHERE room_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected;
        }
    }

    // Update Room
    public int updateRoom(Room room) throws SQLException {
        String sql = "UPDATE rooms SET type=?, is_booked=?, price_per_night=?, description=?, floor=? WHERE room_number=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, room.getType());
            stmt.setBoolean(2, room.isBooked());
            stmt.setDouble(3, room.getPriceNight());
            stmt.setString(4, room.getDescription());
            stmt.setInt(5, room.getFloor());
            stmt.setString(6, room.getRoomNumber());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected;
        }
    }


    // Get All Rooms
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getBoolean("is_booked"),
                        rs.getDouble("price_per_night"),
                        rs.getString("description"),
                        rs.getInt("floor")
                );
                rooms.add(room);
            }
        }
        return rooms;
    }

    public Room findRoomByNumber(String roomNumber) throws SQLException{
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Mapping dữ liệu từ ResultSet sang Room
                Room room = new Room();
                room.setRoomNumber(rs.getString("room_number"));
                room.setType(rs.getString("type"));
                room.setBooked(rs.getBoolean("is_booked"));
                room.setPriceNight(rs.getDouble("price_per_night"));
                room.setDescription(rs.getString("description"));
                room.setFloor(rs.getInt("floor"));
                return room;
            } else {
                return null; // Không tìm thấy phòng
            }
        }
    }

    public int updateRoomStatus(String roomNumber, boolean isBooked) throws SQLException{
        String sql = "UPDATE rooms SET is_booked=? WHERE room_number=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isBooked);
            stmt.setString(2, roomNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected;
        }
    }
}