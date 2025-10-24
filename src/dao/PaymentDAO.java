package dao;

import database.DatabaseManager;
import models.TuitionPayment;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    // Thêm khoản thu học phí
    public boolean addPayment(TuitionPayment payment) {
        String sql = "INSERT INTO tuition_payments (student_id, class_id, amount, " +
                    "payment_date, month_year, payment_method, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, payment.getStudentId());
            pstmt.setInt(2, payment.getClassId());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setString(4, payment.getPaymentDate().toString());
            pstmt.setString(5, payment.getMonthYear());
            pstmt.setString(6, payment.getPaymentMethod());
            pstmt.setString(7, payment.getStatus());
            pstmt.setString(8, payment.getNotes());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin thanh toán
    public boolean updatePayment(TuitionPayment payment) {
        String sql = "UPDATE tuition_payments SET student_id = ?, class_id = ?, " +
                    "amount = ?, payment_date = ?, month_year = ?, payment_method = ?, " +
                    "status = ?, notes = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, payment.getStudentId());
            pstmt.setInt(2, payment.getClassId());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setString(4, payment.getPaymentDate().toString());
            pstmt.setString(5, payment.getMonthYear());
            pstmt.setString(6, payment.getPaymentMethod());
            pstmt.setString(7, payment.getStatus());
            pstmt.setString(8, payment.getNotes());
            pstmt.setInt(9, payment.getPaymentId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa khoản thanh toán
    public boolean deletePayment(int paymentId) {
        String sql = "UPDATE tuition_payments SET status = 'cancelled' WHERE payment_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy thanh toán theo ID
    public TuitionPayment getPaymentById(int paymentId) {
        String sql = "SELECT tp.*, s.full_name as student_name, c.class_name " +
                    "FROM tuition_payments tp " +
                    "LEFT JOIN students s ON tp.student_id = s.student_id " +
                    "LEFT JOIN classes c ON tp.class_id = c.class_id " +
                    "WHERE tp.payment_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy tất cả thanh toán
    public List<TuitionPayment> getAllPayments() {
        List<TuitionPayment> payments = new ArrayList<>();
        String sql = "SELECT tp.*, s.full_name as student_name, c.class_name " +
                    "FROM tuition_payments tp " +
                    "LEFT JOIN students s ON tp.student_id = s.student_id " +
                    "LEFT JOIN classes c ON tp.class_id = c.class_id " +
                    "ORDER BY tp.payment_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }
    
    // Lấy thanh toán theo học viên
    public List<TuitionPayment> getPaymentsByStudent(int studentId) {
        List<TuitionPayment> payments = new ArrayList<>();
        String sql = "SELECT tp.*, s.full_name as student_name, c.class_name " +
                    "FROM tuition_payments tp " +
                    "LEFT JOIN students s ON tp.student_id = s.student_id " +
                    "LEFT JOIN classes c ON tp.class_id = c.class_id " +
                    "WHERE tp.student_id = ? " +
                    "ORDER BY tp.payment_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }
    
    // Lấy thanh toán theo khoảng thời gian
    public List<TuitionPayment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<TuitionPayment> payments = new ArrayList<>();
        String sql = "SELECT tp.*, s.full_name as student_name, c.class_name " +
                    "FROM tuition_payments tp " +
                    "LEFT JOIN students s ON tp.student_id = s.student_id " +
                    "LEFT JOIN classes c ON tp.class_id = c.class_id " +
                    "WHERE tp.payment_date BETWEEN ? AND ? " +
                    "ORDER BY tp.payment_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }
    
    // TÍNH TỔNG DOANH THU (toàn bộ)
    public double getTotalRevenue() {
        String sql = "SELECT SUM(amount) FROM tuition_payments WHERE status = 'paid'";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // TÍNH TỔNG DOANH THU THEO KHOẢNG THỜI GIAN
    public double getTotalRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(amount) FROM tuition_payments " +
                    "WHERE status = 'paid' AND payment_date BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // TÍNH TỔNG DOANH THU THEO THÁNG
    public double getTotalRevenueByMonth(String monthYear) {
        String sql = "SELECT SUM(amount) FROM tuition_payments " +
                    "WHERE status = 'paid' AND month_year = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, monthYear);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // Đếm số lượng thanh toán
    public int getTotalPayments() {
        String sql = "SELECT COUNT(*) FROM tuition_payments WHERE status = 'paid'";
        
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
    private TuitionPayment extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        TuitionPayment payment = new TuitionPayment(
            rs.getInt("payment_id"),
            rs.getInt("student_id"),
            rs.getInt("class_id"),
            rs.getDouble("amount"),
            LocalDate.parse(rs.getString("payment_date")),
            rs.getString("month_year"),
            rs.getString("payment_method"),
            rs.getString("status"),
            rs.getString("notes")
        );
        
        // Set tên học viên và lớp
        String studentName = rs.getString("student_name");
        String className = rs.getString("class_name");
        if (studentName != null) payment.setStudentName(studentName);
        if (className != null) payment.setClassName(className);
        
        return payment;
    }
}