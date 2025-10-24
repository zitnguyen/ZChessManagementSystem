package dao;

import database.DatabaseManager;
import models.Teacher;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    
    // Thêm giáo viên mới
    public boolean addTeacher(Teacher teacher) {
        String sql = "INSERT INTO teachers (full_name, phone, email, salary_per_session, " +
                    "hire_date, status, specialization, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teacher.getFullName());
            pstmt.setString(2, teacher.getPhone());
            pstmt.setString(3, teacher.getEmail());
            pstmt.setDouble(4, teacher.getSalaryPerSession());
            pstmt.setString(5, teacher.getHireDate().toString());
            pstmt.setString(6, teacher.getStatus());
            pstmt.setString(7, teacher.getSpecialization());
            pstmt.setString(8, teacher.getNotes());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin giáo viên
    public boolean updateTeacher(Teacher teacher) {
        String sql = "UPDATE teachers SET full_name = ?, phone = ?, email = ?, " +
                    "salary_per_session = ?, status = ?, specialization = ?, notes = ? " +
                    "WHERE teacher_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teacher.getFullName());
            pstmt.setString(2, teacher.getPhone());
            pstmt.setString(3, teacher.getEmail());
            pstmt.setDouble(4, teacher.getSalaryPerSession());
            pstmt.setString(5, teacher.getStatus());
            pstmt.setString(6, teacher.getSpecialization());
            pstmt.setString(7, teacher.getNotes());
            pstmt.setInt(8, teacher.getTeacherId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa giáo viên
    public boolean deleteTeacher(int teacherId) {
        String sql = "UPDATE teachers SET status = 'inactive' WHERE teacher_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teacherId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy giáo viên theo ID
    public Teacher getTeacherById(int teacherId) {
        String sql = "SELECT * FROM teachers WHERE teacher_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTeacherFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy tất cả giáo viên
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY full_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                teachers.add(extractTeacherFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }
    
    // Lấy giáo viên đang hoạt động
    public List<Teacher> getActiveTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE status = 'active' ORDER BY full_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                teachers.add(extractTeacherFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }
    
    // Đếm tổng số giáo viên
    public int getTotalTeachers() {
        String sql = "SELECT COUNT(*) FROM teachers WHERE status = 'active'";
        
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
    
    // Helper method
    private Teacher extractTeacherFromResultSet(ResultSet rs) throws SQLException {
        return new Teacher(
            rs.getInt("teacher_id"),
            rs.getString("full_name"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getDouble("salary_per_session"),
            LocalDate.parse(rs.getString("hire_date")),
            rs.getString("status"),
            rs.getString("specialization"),
            rs.getString("notes")
        );
    }
}