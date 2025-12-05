package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import com.example.schoolmanagement.models.Student;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public int getStudentCount() {
        String query = "SELECT COUNT(*) FROM students";
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

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT s.*, p.father_name, p.mother_name, c.class_name, c.section " +
                       "FROM students s " +
                       "LEFT JOIN parents p ON s.parent_id = p.id " +
                       "LEFT JOIN classes c ON s.class_id = c.id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public Student getStudentById(int studentId) {
        String query = "SELECT s.*, p.father_name, p.mother_name, c.class_name, c.section " +
                       "FROM students s " +
                       "LEFT JOIN parents p ON s.parent_id = p.id " +
                       "LEFT JOIN classes c ON s.class_id = c.id " +
                       "WHERE s.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Student> getStudentsByClassId(int classId) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT s.*, p.father_name, p.mother_name, c.class_name, c.section " +
                       "FROM students s " +
                       "LEFT JOIN parents p ON s.parent_id = p.id " +
                       "LEFT JOIN classes c ON s.class_id = c.id " +
                       "WHERE s.class_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public int getStudentCountByGender(String gender) {
        String query = "SELECT COUNT(*) FROM students WHERE gender = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, gender);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Student> searchStudents(String searchText) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT s.*, p.father_name, p.mother_name, c.class_name, c.section " +
                       "FROM students s " +
                       "LEFT JOIN parents p ON s.parent_id = p.id " +
                       "LEFT JOIN classes c ON s.class_id = c.id " +
                       "WHERE s.first_name LIKE ? OR s.last_name LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            String likeText = "%" + searchText + "%";
            pstmt.setString(1, likeText);
            pstmt.setString(2, likeText);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void addStudent(Student student) {
        String query = "INSERT INTO students (first_name, last_name, dob, gender, address, parent_id, class_id, transport_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getDob() != null ? student.getDob().toString() : null);
            pstmt.setString(4, student.getGender());
            pstmt.setString(5, student.getAddress());
            pstmt.setInt(6, student.getParentId());
            pstmt.setInt(7, student.getClassId());
            pstmt.setInt(8, student.getTransportId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(Student student) {
        String query = "UPDATE students SET first_name=?, last_name=?, dob=?, gender=?, address=?, parent_id=?, class_id=?, transport_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getDob() != null ? student.getDob().toString() : null);
            pstmt.setString(4, student.getGender());
            pstmt.setString(5, student.getAddress());
            pstmt.setInt(6, student.getParentId());
            pstmt.setInt(7, student.getClassId());
            pstmt.setInt(8, student.getTransportId());
            pstmt.setInt(9, student.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        String parentName = "N/A";
        String father = rs.getString("father_name");
        String mother = rs.getString("mother_name");
        if (father != null && !father.isEmpty()) parentName = father;
        else if (mother != null && !mother.isEmpty()) parentName = mother;

        String className = "N/A";
        String cName = rs.getString("class_name");
        String cSec = rs.getString("section");
        if (cName != null) className = cName + " (" + cSec + ")";

        return new Student(
            rs.getInt("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("dob") != null ? LocalDate.parse(rs.getString("dob")) : null,
            rs.getString("gender"),
            rs.getString("address"),
            rs.getInt("parent_id"),
            parentName,
            rs.getInt("class_id"),
            className,
            rs.getInt("transport_id")
        );
    }
}