package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import com.example.schoolmanagement.models.Exam;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExamDAO {
    
    public List<Exam> getAllExams() {
        List<Exam> exams = new ArrayList<>();
        String query = "SELECT e.*, c.class_name, c.section FROM exams e " +
                       "LEFT JOIN classes c ON e.class_id = c.id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String className = "Unassigned";
                if (rs.getString("class_name") != null) {
                    className = rs.getString("class_name") + " " + rs.getString("section");
                }
                
                exams.add(new Exam(
                    rs.getInt("id"),
                    rs.getString("exam_name"),
                    rs.getString("exam_date") != null ? LocalDate.parse(rs.getString("exam_date")) : null,
                    rs.getInt("class_id"),
                    className
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    public void addExam(Exam exam) {
        String query = "INSERT INTO exams (exam_name, exam_date, class_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, exam.getExamName());
            pstmt.setString(2, exam.getExamDate() != null ? exam.getExamDate().toString() : null);
            if (exam.getClassId() > 0) pstmt.setInt(3, exam.getClassId());
            else pstmt.setNull(3, Types.INTEGER);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExam(int id) {
        String query = "DELETE FROM exams WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Exam> getExamsByClassId(int classId) {
        List<Exam> exams = new ArrayList<>();
        String query = "SELECT e.*, c.class_name, c.section FROM exams e " +
                       "LEFT JOIN classes c ON e.class_id = c.id WHERE e.class_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String className = rs.getString("class_name") + " " + rs.getString("section");
                exams.add(new Exam(
                    rs.getInt("id"),
                    rs.getString("exam_name"),
                    rs.getString("exam_date") != null ? LocalDate.parse(rs.getString("exam_date")) : null,
                    rs.getInt("class_id"),
                    className
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }
}
