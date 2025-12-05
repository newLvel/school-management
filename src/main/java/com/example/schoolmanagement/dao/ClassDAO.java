package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import com.example.schoolmanagement.models.SchoolClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    
    public int getClassCount() {
        String query = "SELECT COUNT(*) FROM classes";
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

    public List<SchoolClass> getAllClasses() {
        List<SchoolClass> classes = new ArrayList<>();
        String query = "SELECT c.*, t.first_name, t.last_name FROM classes c " +
                       "LEFT JOIN teachers t ON c.teacher_id = t.id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String teacherName = "Unassigned";
                if (rs.getObject("teacher_id") != null) {
                    teacherName = rs.getString("first_name") + " " + rs.getString("last_name");
                }
                
                SchoolClass schoolClass = new SchoolClass(
                    rs.getInt("id"),
                    rs.getString("class_name"),
                    rs.getString("section"),
                    rs.getInt("teacher_id"),
                    teacherName
                );
                classes.add(schoolClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public void addClass(SchoolClass schoolClass) {
        String query = "INSERT INTO classes (class_name, section, teacher_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, schoolClass.getClassName());
            pstmt.setString(2, schoolClass.getSection());
            if (schoolClass.getTeacherId() > 0) {
                pstmt.setInt(3, schoolClass.getTeacherId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClass(SchoolClass schoolClass) {
        String query = "UPDATE classes SET class_name=?, section=?, teacher_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, schoolClass.getClassName());
            pstmt.setString(2, schoolClass.getSection());
            if (schoolClass.getTeacherId() > 0) {
                pstmt.setInt(3, schoolClass.getTeacherId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setInt(4, schoolClass.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClass(int id) {
        String query = "DELETE FROM classes WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
