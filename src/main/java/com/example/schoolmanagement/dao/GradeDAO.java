package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import com.example.schoolmanagement.models.Grade;
import com.example.schoolmanagement.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    
    // Check if a grade record exists
    public boolean gradeExists(int examId, int studentId, int subjectId) {
        String query = "SELECT 1 FROM grades WHERE exam_id=? AND student_id=? AND subject_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, examId);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, subjectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveOrUpdateGrade(int examId, int studentId, int subjectId, double score, String remarks) {
        if (gradeExists(examId, studentId, subjectId)) {
            updateGrade(examId, studentId, subjectId, score, remarks);
        } else {
            addGrade(examId, studentId, subjectId, score, remarks);
        }
    }

    private void addGrade(int examId, int studentId, int subjectId, double score, String remarks) {
        String query = "INSERT INTO grades (exam_id, student_id, subject_id, score, remarks) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, examId);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, subjectId);
            pstmt.setDouble(4, score);
            pstmt.setString(5, remarks);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateGrade(int examId, int studentId, int subjectId, double score, String remarks) {
        String query = "UPDATE grades SET score=?, remarks=? WHERE exam_id=? AND student_id=? AND subject_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, score);
            pstmt.setString(2, remarks);
            pstmt.setInt(3, examId);
            pstmt.setInt(4, studentId);
            pstmt.setInt(5, subjectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Fetch grades for a specific Exam and Subject (returns list of Grade objects wrapping students)
    // We want to return ALL students in the class, with their grades if they exist, or empty/0 if not.
    public List<Grade> getGradesForExamSubject(int examId, int classId, int subjectId) {
        List<Grade> grades = new ArrayList<>();
        
        // This query fetches ALL students in the class, and joins their grade for the specific exam/subject
        String query = "SELECT s.id as student_id, s.first_name, s.last_name, g.score, g.remarks " +
                       "FROM students s " +
                       "LEFT JOIN grades g ON s.id = g.student_id AND g.exam_id = ? AND g.subject_id = ? " +
                       "WHERE s.class_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, examId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, classId);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                // If grade is null (not found), score will be 0 by default getDouble, check wasNull if needed
                double score = rs.getDouble("score");
                if (rs.wasNull()) score = -1; // Use -1 to indicate not graded yet
                
                grades.add(new Grade(
                    rs.getInt("student_id"),
                    fullName,
                    score,
                    rs.getString("remarks")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    public double getOverallGradeAverage(int studentId) {
        String query = "SELECT AVG(score) FROM grades WHERE student_id = ?";
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
        return 0.0;
    }
}
