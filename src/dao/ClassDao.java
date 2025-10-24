package dao;

import database.DatabaseManager;
import models.ChessClass;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    
    // Thêm lớp học mới
    public boolean addClass(ChessClass chessClass) {
        String sql = "INSERT INTO classes (class_name, teacher_id, schedule, room, " +
                    "max_students, fee_per_month, start_date, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, chessClass.getClassName());
            pstmt.setInt(2, chessClass.getTeacherId());
            pstmt.setString(3, chessClass.getSchedule());
            pstmt.setString(4, chessClass.getRoom());
            pstmt.setInt(5, chessClass.getMaxStudents());
            pstmt.setDouble(6, chessClass.getFeePerMonth());
            pstmt.setString(7, chessClass.getStartDate().toString());
            pstmt.setString(8, chessClass.getStatus());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin lớp học
    public boolean updateClass(ChessClass chessClass) {
        String sql = "UPDATE classes SET class_name = ?, teacher_id = ?, schedule = ?, " +
                    "room = ?, max_students = ?, fee_per_month = ?, start_date = ?, status = ? " +
                    "WHERE class_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, chessClass.getClassName());
            pstmt.setInt(2, chessClass.getTeacherId());
            pstmt.setString(3, chessClass.getSchedule());
            pstmt.setString(4, chessClass.getRoom());
            pstmt.setInt(5, chessClass.getMaxStudents());
            pstmt.setDouble(6, chessClass.getFeePerMonth());
            pstmt.setString(7, chessClass.getStartDate().toString());
            pstmt.setString(8, chessClass.getStatus());
            pstmt.setInt(9, chessClass.getClassId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa lớp học
    public boolean deleteClass(int classId) {
        String sql = "UPDATE classes SET status = 'cancelled' WHERE class_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy lớp học theo ID
    public ChessClass getClassById(int classId) {
        String sql = "SELECT c.*, t.full_name as teacher_name " +
                    "FROM classes c " +
                    "LEFT JOIN teachers t ON c.teacher_id = t.teacher_id " +
                    "WHERE c.class_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractClassFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy tất cả lớp học
    public List<ChessClass> getAllClasses() {
        List<ChessClass> classes = new ArrayList<>();
        String sql = "SELECT c.*, t.full_name as teacher_name " +
                    "FROM classes c " +
                    "LEFT JOIN teachers t ON c.teacher_id = t.teacher_id " +
                    "ORDER BY c.class_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                classes.add(extractClassFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }
    
    // Lấy lớp học đang hoạt động
    public List<ChessClass> getActiveClasses() {
        List<ChessClass> classes = new ArrayList<>();
        String sql = "SELECT c.*, t.full_name as teacher_name " +
                    "FROM classes c " +
                    "LEFT JOIN teachers t ON c.teacher_id = t.teacher_id " +
                    "WHERE c.status = 'active' " +
                    "ORDER BY c.class_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                classes.add(extractClassFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }
    
    // Lấy lớp học của giáo viên
    public List<ChessClass> getClassesByTeacher(int teacherId) {
        List<ChessClass> classes = new ArrayList<>();
        String sql = "SELECT c.*, t.full_name as teacher_name " +
                    "FROM classes c " +
                    "LEFT JOIN teachers t ON c.teacher_id = t.teacher_id " +
                    "WHERE c.teacher_id = ? " +
                    "ORDER BY c.class_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                classes.add(extractClassFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }
    
    // Đếm số học viên trong lớp
    public int getStudentCountInClass(int classId) {
        String sql = "SELECT COUNT(*) FROM class_enrollments " +
                    "WHERE class_id = ? AND status = 'active'";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Đếm tổng số lớp học
    public int getTotalClasses() {
        String sql = "SELECT COUNT(*) FROM classes WHERE status = 'active'";
        
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
    private ChessClass extractClassFromResultSet(ResultSet rs) throws SQLException {
        ChessClass chessClass = new ChessClass(
            rs.getInt("class_id"),
            rs.getString("class_name"),
            rs.getInt("teacher_id"),
            rs.getString("schedule"),
            rs.getString("room"),
            rs.getInt("max_students"),
            rs.getDouble("fee_per_month"),
            LocalDate.parse(rs.getString("start_date")),
            rs.getString("status")
        );
        
        // Set teacher name nếu có
        String teacherName = rs.getString("teacher_name");
        if (teacherName != null) {
            chessClass.setTeacherName(teacherName);
        }
        
        return chessClass;
    }
}