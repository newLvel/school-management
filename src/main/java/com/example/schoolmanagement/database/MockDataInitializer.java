package com.example.schoolmanagement.database;

import com.example.schoolmanagement.dao.*;
import com.example.schoolmanagement.models.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Random;

public class MockDataInitializer {

    public static void initialize() {
        // 1. Reset Database by deleting the file
        File dbFile = new File("school_management.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }

        // 2. Re-create schema
        DatabaseInitializer.initialize();

        // 3. Populate with Mock Data
        try {
            populateUsers();
            populateTeachers();
            populateClasses();
            populateParents();
            populateStudents();
            populateSubjects();
            populateExams();
            populateFees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void populateUsers() {
        UserDAO userDAO = new UserDAO();
        userDAO.addUser("admin", "admin", "Admin");
    }

    private static void populateTeachers() {
        TeacherDAO teacherDAO = new TeacherDAO();
        teacherDAO.addTeacher(new Teacher("Carla", "Peter", "carla.p@example.com", "111-222-3333", "History", LocalDate.of(2020, 8, 15)));
        teacherDAO.addTeacher(new Teacher("John", "Doe", "john.d@example.com", "222-333-4444", "Mathematics", LocalDate.of(2018, 5, 20)));
        teacherDAO.addTeacher(new Teacher("Jane", "Smith", "jane.s@example.com", "333-444-5555", "Science", LocalDate.of(2022, 1, 10)));
    }

    private static void populateClasses() {
        ClassDAO classDAO = new ClassDAO();
        classDAO.addClass(new SchoolClass("Class 5", "A", 2)); // John Doe
        classDAO.addClass(new SchoolClass("Class 6", "A", 1)); // Carla Peter
        classDAO.addClass(new SchoolClass("Class 7", "B", 3)); // Jane Smith
    }

    private static void populateParents() {
        ParentDAO parentDAO = new ParentDAO();
        parentDAO.addParent(new Parent("Leo Miller Sr.", "Mia Miller", "miller@example.com", "555-123-4567", "123 Main St"));
        parentDAO.addParent(new Parent("John Black", "Sarah Black", "black@example.com", "555-987-6543", "456 Oak Ave"));
    }

    private static void populateStudents() {
        StudentDAO studentDAO = new StudentDAO();
        studentDAO.addStudent(new Student("Leo", "Miller", LocalDate.of(2013, 5, 12), "Male", "123 Main St", 1, 1, 0));
        studentDAO.addStudent(new Student("Jason", "Black", LocalDate.of(2012, 9, 22), "Male", "456 Oak Ave", 2, 2, 0));
    }
    
    private static void populateSubjects() {
        SubjectDAO subjectDAO = new SubjectDAO();
        subjectDAO.addSubject(new Subject("Mathematics", "MAT101"));
        subjectDAO.addSubject(new Subject("History", "HIS202"));
        subjectDAO.addSubject(new Subject("Science", "SCI303"));
    }

    private static void populateExams() {
        ExamDAO examDAO = new ExamDAO();
        examDAO.addExam(new Exam("Midterm", LocalDate.of(2024, 10, 15), 1));
        examDAO.addExam(new Exam("Final", LocalDate.of(2024, 12, 10), 1));
    }
    
    private static void populateFees() {
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            Random rand = new Random();
            for (int i = 1; i <= 12; i++) {
                // Generate earnings data for the chart
                double earnings = 20000 + (30000 * rand.nextDouble());
                String date = String.format("2023-%02d-15", i);
                stmt.execute(String.format("INSERT INTO fees (student_id, amount, payment_date) VALUES (1, %f, '%s')", earnings, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
