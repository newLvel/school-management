package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AssignmentDAO {

    public int getUpcomingAssignmentsCount(int studentId) {
        // This query finds the student's class, then finds subjects for that class,
        // then counts assignments for those subjects that are due in the future.
        String query = "SELECT COUNT(*) FROM assignments a " +
                       "JOIN class_subjects cs ON a.subject_id = cs.subject_id " +
                       "JOIN students s ON cs.class_id = s.class_id " +
                       "WHERE s.id = ? AND date(a.due_date) >= date('now')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
