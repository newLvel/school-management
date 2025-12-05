package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendanceDAO {

    public double getAttendancePercentage(int studentId) {
        // This is a simplified calculation. A real one would be more complex.
        String query = "SELECT CAST(SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*) " +
                       "FROM attendance WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Default if no records
    }
}
