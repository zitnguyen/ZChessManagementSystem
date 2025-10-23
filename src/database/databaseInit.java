package database;

import java.sql.*;

public class DatabaseInit {
    public static void initDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Tạo tất cả các bảng theo schema ở trên
            String[] createTableQueries = {
                // Students table
                "CREATE TABLE IF NOT EXISTS students (" +
                "student_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_name TEXT NOT NULL, " +
                "birth_date DATE, " +
                "phone TEXT, " +
                "parent_phone TEXT, " +
                "email TEXT, " +
                "address TEXT, " +
                "enrollment_date DATE DEFAULT CURRENT_DATE, " +
                "status TEXT DEFAULT 'active', " +
                "notes TEXT)",
                
                // Teachers table
                "CREATE TABLE IF NOT EXISTS teachers (" +
                "teacher_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_name TEXT NOT NULL, " +
                "phone TEXT, " +
                "email TEXT, " +
                "salary_per_session REAL DEFAULT 0, " +
                "hire_date DATE DEFAULT CURRENT_DATE, " +
                "status TEXT DEFAULT 'active', " +
                "specialization TEXT, " +
                "notes TEXT)",
                
                // Classes table
                "CREATE TABLE IF NOT EXISTS classes (" +
                "class_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "class_name TEXT NOT NULL, " +
                "teacher_id INTEGER, " +
                "schedule TEXT, " +
                "room TEXT, " +
                "max_students INTEGER DEFAULT 10, " +
                "fee_per_month REAL DEFAULT 0, " +
                "start_date DATE, " +
                "status TEXT DEFAULT 'active', " +
                "FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id))",
                
                // Class enrollments
                "CREATE TABLE IF NOT EXISTS class_enrollments (" +
                "enrollment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "class_id INTEGER, " +
                "enrollment_date DATE DEFAULT CURRENT_DATE, " +
                "status TEXT DEFAULT 'active', " +
                "FOREIGN KEY (student_id) REFERENCES students(student_id), " +
                "FOREIGN KEY (class_id) REFERENCES classes(class_id))",
                
                // Tuition payments
                "CREATE TABLE IF NOT EXISTS tuition_payments (" +
                "payment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "class_id INTEGER, " +
                "amount REAL NOT NULL, " +
                "payment_date DATE DEFAULT CURRENT_DATE, " +
                "month_year TEXT, " +
                "payment_method TEXT, " +
                "status TEXT DEFAULT 'paid', " +
                "notes TEXT, " +
                "FOREIGN KEY (student_id) REFERENCES students(student_id), " +
                "FOREIGN KEY (class_id) REFERENCES classes(class_id))",
                
                // Expenses
                "CREATE TABLE IF NOT EXISTS expenses (" +
                "expense_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category TEXT NOT NULL, " +
                "description TEXT, " +
                "amount REAL NOT NULL, " +
                "expense_date DATE DEFAULT CURRENT_DATE, " +
                "payment_method TEXT, " +
                "paid_to TEXT, " +
                "notes TEXT)",
                
                // Attendance
                "CREATE TABLE IF NOT EXISTS attendance (" +
                "attendance_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "class_id INTEGER, " +
                "student_id INTEGER, " +
                "attendance_date DATE NOT NULL, " +
                "status TEXT DEFAULT 'present', " +
                "notes TEXT, " +
                "FOREIGN KEY (class_id) REFERENCES classes(class_id), " +
                "FOREIGN KEY (student_id) REFERENCES students(student_id))",
                
                // Student progress
                "CREATE TABLE IF NOT EXISTS student_progress (" +
                "progress_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "assessment_date DATE DEFAULT CURRENT_DATE, " +
                "skill_level TEXT, " +
                "elo_rating INTEGER, " +
                "achievements TEXT, " +
                "notes TEXT, " +
                "FOREIGN KEY (student_id) REFERENCES students(student_id))"
            };
            
            for (String query : createTableQueries) {
                stmt.execute(query);
            }
            
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}