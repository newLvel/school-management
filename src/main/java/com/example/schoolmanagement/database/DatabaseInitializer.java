package com.example.schoolmanagement.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DatabaseInitializer {
    
    public static void initialize() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 1. Users Table (for Authentication)
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "username TEXT UNIQUE NOT NULL," +
                         "password_hash TEXT NOT NULL," +
                         "role TEXT NOT NULL)");

            // 2. Parents Table (Create first as Students depend on it)
            stmt.execute("CREATE TABLE IF NOT EXISTS parents (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "father_name TEXT," +
                         "mother_name TEXT," +
                         "email TEXT," +
                         "phone TEXT," +
                         "address TEXT)");

            // 3. Teachers Table (Create before Classes)
            stmt.execute("CREATE TABLE IF NOT EXISTS teachers (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "first_name TEXT NOT NULL," +
                         "last_name TEXT NOT NULL," +
                         "email TEXT," +
                         "phone TEXT," +
                         "specialization TEXT," +
                         "hire_date TEXT)");

            // 4. Classes Table
            stmt.execute("CREATE TABLE IF NOT EXISTS classes (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "class_name TEXT NOT NULL," +
                         "section TEXT NOT NULL," +
                         "teacher_id INTEGER," +
                         "FOREIGN KEY(teacher_id) REFERENCES teachers(id))");

            // 5. Students Table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "first_name TEXT NOT NULL," +
                         "last_name TEXT NOT NULL," +
                         "dob TEXT," +
                         "gender TEXT," +
                         "address TEXT," +
                         "parent_id INTEGER," +
                         "class_id INTEGER," +
                         "transport_id INTEGER," +
                         "FOREIGN KEY(parent_id) REFERENCES parents(id)," +
                         "FOREIGN KEY(class_id) REFERENCES classes(id))");

            // 6. Subjects Table
            stmt.execute("CREATE TABLE IF NOT EXISTS subjects (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "subject_name TEXT NOT NULL," +
                         "code TEXT)");

            // 7. Exams Table
            stmt.execute("CREATE TABLE IF NOT EXISTS exams (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "exam_name TEXT NOT NULL," +
                         "exam_date TEXT," +
                         "class_id INTEGER," +
                         "FOREIGN KEY(class_id) REFERENCES classes(id))");

            // 8. Grades Table
            stmt.execute("CREATE TABLE IF NOT EXISTS grades (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "exam_id INTEGER," +
                         "student_id INTEGER," +
                         "subject_id INTEGER," +
                         "score REAL," +
                         "remarks TEXT," +
                         "FOREIGN KEY(exam_id) REFERENCES exams(id)," +
                         "FOREIGN KEY(student_id) REFERENCES students(id)," +
                         "FOREIGN KEY(subject_id) REFERENCES subjects(id))");

            // 9. Transport Routes Table
            stmt.execute("CREATE TABLE IF NOT EXISTS transport_routes (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "route_name TEXT NOT NULL," +
                         "vehicle_number TEXT," +
                         "driver_name TEXT," +
                         "cost REAL)");

            // 10. Notices Table
            stmt.execute("CREATE TABLE IF NOT EXISTS notices (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "title TEXT NOT NULL," +
                         "category TEXT," + // e.g., Sports, Academic
                         "date TEXT)");

            // 11. Attendance Table
            stmt.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "student_id INTEGER," +
                         "date TEXT," +
                         "status TEXT," + // Present, Absent, Late
                         "FOREIGN KEY(student_id) REFERENCES students(id))");

            // 12. Fees Table (for Earnings)
            stmt.execute("CREATE TABLE IF NOT EXISTS fees (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "student_id INTEGER," +
                         "amount REAL," +
                         "payment_date TEXT," +
                         "FOREIGN KEY(student_id) REFERENCES students(id))");

            // 13. Assignments Table
            stmt.execute("CREATE TABLE IF NOT EXISTS assignments (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "subject_id INTEGER," +
                         "title TEXT," +
                         "due_date TEXT," +
                         "FOREIGN KEY(subject_id) REFERENCES subjects(id))");

            // 14. Class-Subjects Linking Table
            stmt.execute("CREATE TABLE IF NOT EXISTS class_subjects (" +
                         "class_id INTEGER," +
                         "subject_id INTEGER," +
                         "teacher_id INTEGER," + // Optional: which teacher teaches this subject in this class
                         "FOREIGN KEY(class_id) REFERENCES classes(id)," +
                         "FOREIGN KEY(subject_id) REFERENCES subjects(id)," +
                         "FOREIGN KEY(teacher_id) REFERENCES teachers(id)," +
                         "PRIMARY KEY(class_id, subject_id))");

            System.out.println("Database tables initialized successfully.");

            // Add sample data if tables are empty
            addSampleData(conn);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private static void addSampleData(Connection conn) throws SQLException {
        // Check if notices table is empty
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM notices")) {
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO notices (title, category, date) VALUES ('Inter-school competition', 'Sports', '2023-02-10')");
                stmt.execute("INSERT INTO notices (title, category, date) VALUES ('Disciplinary action if school discipline is not followed', 'Academic', '2023-02-06')");
                stmt.execute("INSERT INTO notices (title, category, date) VALUES ('School Annual function', 'Events', '2023-02-02')");
            }
        }
    }
}
