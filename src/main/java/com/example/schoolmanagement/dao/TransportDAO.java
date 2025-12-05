package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import com.example.schoolmanagement.models.TransportRoute;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportDAO {
    
    public List<TransportRoute> getAllRoutes() {
        List<TransportRoute> routes = new ArrayList<>();
        String query = "SELECT * FROM transport_routes";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                routes.add(new TransportRoute(
                    rs.getInt("id"),
                    rs.getString("route_name"),
                    rs.getString("vehicle_number"),
                    rs.getString("driver_name"),
                    rs.getDouble("cost")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    public void addRoute(TransportRoute route) {
        String query = "INSERT INTO transport_routes (route_name, vehicle_number, driver_name, cost) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, route.getRouteName());
            pstmt.setString(2, route.getVehicleNumber());
            pstmt.setString(3, route.getDriverName());
            pstmt.setDouble(4, route.getCost());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRoute(TransportRoute route) {
        String query = "UPDATE transport_routes SET route_name=?, vehicle_number=?, driver_name=?, cost=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, route.getRouteName());
            pstmt.setString(2, route.getVehicleNumber());
            pstmt.setString(3, route.getDriverName());
            pstmt.setDouble(4, route.getCost());
            pstmt.setInt(5, route.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoute(int id) {
        String query = "DELETE FROM transport_routes WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
