package dao;

import database.DatabaseManager;
import models.Student;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    // Thêm học viên mới
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (full_name, birth_date, phone, parent_phone, " +
                    "email, address, enrollment_date, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getBirthDate() != null ? student.getBirthDate().toString() : null);
            pstmt.setString(3, student.getPhone());
            pstmt.setString(4, student.getParentPhone());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getAddress());
            pstmt.setString(7, student.getEnrollmentDate().toString());
            pstmt.setString(8, student.getStatus());
            pstmt.setString(9, student.getNotes());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin học viên
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET full_name = ?, birth_date = ?, phone = ?, " +
                    "parent_phone = ?, email = ?, address = ?, status = ?, notes = ? " +
                    "WHERE student_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getBirthDate() != null ? student.getBirthDate().toString() : null);
            pstmt.setString(3, student.getPhone());
            pstmt.setString(4, student.getParentPhone());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getAddress());
            pstmt.setString(7, student.getStatus());
            pstmt.setString(8, student.getNotes());
            pstmt.setInt(9, student.getStudentId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa học viên (soft delete - chuyển status)
    public boolean deleteStudent(int studentId) {
        String sql = "UPDATE students SET status = 'inactive' WHERE student_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa vĩnh viễn (hard delete)
    public boolean permanentDeleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy học viên theo ID
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy tất cả học viên
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY full_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    // Lấy học viên theo trạng thái
    public List<Student> getStudentsByStatus(String status) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE status = ? ORDER BY full_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    // Tìm kiếm học viên theo tên
    public List<Student> searchStudentsByName(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE full_name LIKE ? ORDER BY full_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    // Đếm tổng số học viên
    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) FROM students WHERE status = 'active'";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Helper method: Chuyển ResultSet thành Student object
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("student_id");
        String fullName = rs.getString("full_name");
        String birthDateStr = rs.getString("birth_date");
        LocalDate birthDate = birthDateStr != null ? LocalDate.parse(birthDateStr) : null;
        String phone = rs.getString("phone");
        String parentPhone = rs.getString("parent_phone");
        String email = rs.getString("email");
        String address = rs.getString("address");
        LocalDate enrollmentDate = LocalDate.parse(rs.getString("enrollment_date"));
        String status = rs.getString("status");
        String notes = rs.getString("notes");
        
        return new Student(id, fullName, birthDate, phone, parentPhone, 
                          email, address, enrollmentDate, status, notes);
    }
}