package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import com.example.schoolmanagement.models.Parent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParentDAO {
    
    public List<Parent> getAllParents() {
        List<Parent> parents = new ArrayList<>();
        String query = "SELECT * FROM parents";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Parent parent = new Parent(
                    rs.getInt("id"),
                    rs.getString("father_name"),
                    rs.getString("mother_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
                );
                parents.add(parent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parents;
    }

    public void addParent(Parent parent) {
        String query = "INSERT INTO parents (father_name, mother_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, parent.getFatherName());
            pstmt.setString(2, parent.getMotherName());
            pstmt.setString(3, parent.getEmail());
            pstmt.setString(4, parent.getPhone());
            pstmt.setString(5, parent.getAddress());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateParent(Parent parent) {
        String query = "UPDATE parents SET father_name=?, mother_name=?, email=?, phone=?, address=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, parent.getFatherName());
            pstmt.setString(2, parent.getMotherName());
            pstmt.setString(3, parent.getEmail());
            pstmt.setString(4, parent.getPhone());
            pstmt.setString(5, parent.getAddress());
            pstmt.setInt(6, parent.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteParent(int id) {
        String query = "DELETE FROM parents WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getParentCount() {
        String query = "SELECT COUNT(*) FROM parents";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
